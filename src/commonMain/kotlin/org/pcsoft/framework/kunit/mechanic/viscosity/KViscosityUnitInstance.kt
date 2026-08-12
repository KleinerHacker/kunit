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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (Pa·s) and the raw component storage
 * (`g·m⁻¹·s⁻¹`): 1 Pa·s = 1000 g/(m·s). It exists because the mass component of this library is normalized
 * to grams, not kilograms. The [KViscosityUnitInstance.value] is always the raw component value; readings
 * in Pa·s divide by this factor.
 */
internal const val PAS_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **dynamic viscosity**, i.e. exactly three terms in the
 * canonical normal form - [KMassUnit.BASE] (gram) at `+1`, [KDistanceUnit.BASE] (meter) at `-1` and
 * [KTimeUnit.BASE] (second) at `-1` (`g·m⁻¹·s⁻¹`). The [value] is the raw component value; readings in Pa·s
 * ([KViscosityUnit.BASE]) divide by [PAS_IN_BASE].
 *
 * Dynamic viscosity is a *constructed* unit group produced by the cross-group operators in
 * `KViscosityUnitOperators.kt` (`pressure * time`), the bare tokens in `KViscosityUnitBareValues.kt` (e.g.
 * `1 of centi.poises`), the prefixed templates in `KViscosityUnitExtensions.kt`, or [toViscosity].
 *
 * Example:
 * ```kotlin
 * val eta = (1 of milli.pascalSeconds) // water at 20 °C ≈ 1 mPa·s = 1 cP
 * eta into centi.poises                // 1.0
 * ```
 */
class KViscosityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KViscosityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new viscosity with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`5 of pascalSeconds`).
     */
    override fun scaledBy(factor: Double): KViscosityUnitInstance = viscosityInstanceOf(value * factor)

    /**
     * Adds two viscosities, automatically converting between different [KViscosityUnit]s since both
     * operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KViscosityUnitInstance): KViscosityUnitInstance =
        viscosityInstanceOf(value + other.value)

    /** Subtracts two viscosities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KViscosityUnitInstance): KViscosityUnitInstance =
        viscosityInstanceOf(value - other.value)

    /** Multiplies two viscosities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KViscosityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two viscosities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KViscosityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two viscosities by their normalized component [value]. */
    override operator fun compareTo(other: KViscosityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of poises) == (100 of milli.pascalSeconds)`). */
    override fun equals(other: Any?): Boolean = other is KViscosityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in Pa·s, e.g. `"0.001 Pa*s"`. */
    override fun toString(): String = "${renderDouble(value / PAS_IN_BASE)} ${KViscosityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KViscosityUnitInstance] from a raw component value (`g·m⁻¹·s⁻¹`) - the single normalizing
 * creation source every decomposition funnels into.
 */
internal fun viscosityInstanceOf(componentValue: Double): KViscosityUnitInstance =
    KViscosityUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -1), KUnitTerm(KTimeUnit.BASE, -1)),
        ),
    )

/** Builds a value-1 [KViscosityUnitInstance] for the given [unit] (its [KViscosityUnit.baseValue] Pa·s). */
internal fun viscosityOfUnit(unit: KViscosityUnit): KViscosityUnitInstance =
    viscosityInstanceOf(unit.baseValue * PAS_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" dynamic viscosity, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `-1` and one
 * [KTimeUnit] term at exponent `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a mass·length⁻¹·time⁻¹ dynamic viscosity.
 */
fun KMixedUnitInstance.toViscosity(): KViscosityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure dynamic viscosity (expected one KMassUnit^1, one KDistanceUnit^-1 and one KTimeUnit^-1 term)"
    }
    val component = value * massTerm.unit.baseValue / lengthTerm.unit.baseValue / timeTerm.unit.baseValue
    return viscosityInstanceOf(component)
}
