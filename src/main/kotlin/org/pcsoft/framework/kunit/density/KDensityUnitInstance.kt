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

package org.pcsoft.framework.kunit.density

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg/m³) and the raw component storage (`g·m⁻³`):
 * 1 kg/m³ = 1000 g/m³. It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KDensityUnitInstance.value] is always the raw component value; readings in kg/m³
 * divide by this factor.
 */
internal const val KGM3_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a (mass-)density, i.e. exactly two terms - [KMassUnit.BASE]
 * (gram) at exponent `+1` and [KDistanceUnit.BASE] (meter) at exponent `-3` (`g·m⁻³`). The [value] is
 * the raw component value; readings in kg/m³ ([KDensityUnit.BASE]) divide by [KGM3_IN_BASE].
 *
 * Density has no bare tokens (every spelling is a ratio); it is produced by the typed `mass / volume`
 * operator in `KDensityUnitOperators.kt`, built as an expression (`kilo.grams / (meters pow 3)`), or via
 * [toDensity], and read back with `into` against such an expression.
 *
 * Example:
 * ```kotlin
 * val d = (6 of kilo.grams) / (2 of liters) // KDensityUnitInstance
 * d into (kilo.grams / (centi.meters pow 3)) // 0.003
 * ```
 */
class KDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KDensityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new density value with the component [value] scaled by [factor]. */
    override fun scaledBy(factor: Double): KDensityUnitInstance = densityUnitInstanceOf(value * factor)

    /**
     * Adds two densities, automatically converting between the constructing units since both operands are
     * always normalized to the same component base internally.
     */
    override operator fun plus(other: KDensityUnitInstance): KDensityUnitInstance = KDensityUnitInstance(instance + other.instance)

    /** Subtracts two densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KDensityUnitInstance): KDensityUnitInstance = KDensityUnitInstance(instance - other.instance)

    /** Multiplies two densities, producing a new [KMixedUnitInstance] (no longer a "pure" density). */
    operator fun times(other: KDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two densities by their normalized component [value]. */
    override operator fun compareTo(other: KDensityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value]. */
    override fun equals(other: Any?): Boolean = other is KDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg/m³, e.g. `"7850.0 kg/m³"`. */
    override fun toString(): String = "${value / KGM3_IN_BASE} ${KDensityUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" density value, as long as it consists of exactly two terms: one
 * [KMassUnit] term at exponent `+1` and one [KDistanceUnit] term at exponent `-3` (order independent).
 * The terms are normalized to their component base units.
 *
 * @throws IllegalStateException if this instance is not a mass·length⁻³ density.
 */
fun KMixedUnitInstance.toDensity(): KDensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -3 }
    check(units.size == 2 && massTerm != null && lengthTerm != null) {
        "KMixedUnitInstance $this does not represent a pure density (expected one KMassUnit^1 and one KDistanceUnit^-3 term)"
    }
    val component = value * massTerm.unit.baseValue * Math.pow(lengthTerm.unit.baseValue, -3.0)
    return densityUnitInstanceOf(component)
}

/** Builds a [KDensityUnitInstance] from a raw component value (`g·m⁻³`). */
internal fun densityUnitInstanceOf(componentValue: Double): KDensityUnitInstance =
    KDensityUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -3)),
        ),
    )
