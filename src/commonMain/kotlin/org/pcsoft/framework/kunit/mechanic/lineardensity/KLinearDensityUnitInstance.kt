/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg/m) and the raw component storage (`g·m⁻¹`):
 * 1 kg/m = 1000 g/m. It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KLinearDensityUnitInstance.value] is always the raw component value; readings in kg/m
 * divide by this factor.
 */
internal const val KGM_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **linear density** (mass per length), i.e. exactly two terms
 * in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1` and [KDistanceUnit.BASE] (meter)
 * at exponent `-1` (`g·m⁻¹`). The [value] is the raw component value; readings in kg/m
 * ([KLinearDensityUnit.BASE]) divide by [KGM_IN_BASE].
 *
 * Linear density is the one-dimensional sibling of area density (`kg/m²`) and density (`kg/m³`). It is a
 * *constructed* unit group produced by the cross-group operators in `KLinearDensityUnitOperators.kt`
 * (`mass / length`), the bare tokens in `KLinearDensityUnitBareValues.kt` (e.g. `5 of gramsPerMeter`), the
 * prefixed templates in `KLinearDensityUnitExtensions.kt`, or [toLinearDensity].
 *
 * Example:
 * ```kotlin
 * val rho = (2 of kilo.grams) / (4 of meters) // KLinearDensityUnitInstance, 0.5 kg/m
 * rho into gramsPerMeter                      // 500.0
 * ```
 */
class KLinearDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLinearDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new linear density with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`5 of gramsPerMeter`).
     */
    override fun scaledBy(factor: Double): KLinearDensityUnitInstance = linearDensityInstanceOf(value * factor)

    /**
     * Adds two linear densities, automatically converting between different [KLinearDensityUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KLinearDensityUnitInstance): KLinearDensityUnitInstance =
        linearDensityInstanceOf(value + other.value)

    /** Subtracts two linear densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLinearDensityUnitInstance): KLinearDensityUnitInstance =
        linearDensityInstanceOf(value - other.value)

    /** Multiplies two linear densities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KLinearDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two linear densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLinearDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two linear densities by their normalized component [value]. */
    override operator fun compareTo(other: KLinearDensityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)`). */
    override fun equals(other: Any?): Boolean = other is KLinearDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg/m, e.g. `"0.5 kg/m"`. */
    override fun toString(): String = "${renderDouble(value / KGM_IN_BASE)} ${KLinearDensityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLinearDensityUnitInstance] from a raw component value (`g·m⁻¹`) - the single normalizing
 * creation source every decomposition funnels into.
 */
internal fun linearDensityInstanceOf(componentValue: Double): KLinearDensityUnitInstance =
    KLinearDensityUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -1)),
        ),
    )

/**
 * Builds a value-1 [KLinearDensityUnitInstance] for the given [unit] (its [KLinearDensityUnit.baseValue]
 * kg/m).
 */
internal fun linearDensityOfUnit(unit: KLinearDensityUnit): KLinearDensityUnitInstance =
    linearDensityInstanceOf(unit.baseValue * KGM_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" linear density, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1` and one [KDistanceUnit] term at exponent `-1` (order
 * independent).
 *
 * @throws IllegalStateException if this instance is not a mass-per-length linear density.
 */
fun KMixedUnitInstance.toLinearDensity(): KLinearDensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    check(units.size == 2 && massTerm != null && lengthTerm != null) {
        "KMixedUnitInstance $this does not represent a pure linear density (expected one KMassUnit^1 and one KDistanceUnit^-1 term)"
    }
    return linearDensityInstanceOf(value * massTerm.unit.baseValue / lengthTerm.unit.baseValue)
}
