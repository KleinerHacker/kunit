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

package org.pcsoft.framework.kunit.areadensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg/m²) and the raw component storage (`g·m⁻²`):
 * 1 kg/m² = 1000 g/m². It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KAreaDensityUnitInstance.value] is always the raw component value; readings in kg/m²
 * divide by this factor.
 */
internal const val KGM2_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing an area density (surface mass), i.e. exactly two terms -
 * [KMassUnit.BASE] (gram) at exponent `+1` and [KDistanceUnit.BASE] (meter) at exponent `-2` (`g·m⁻²`).
 * The [value] is the raw component value; readings in kg/m² ([KAreaDensityUnit.BASE]) divide by
 * [KGM2_IN_BASE].
 *
 * Area density has no bare tokens (every spelling is a ratio); it is produced by the typed `mass / area`
 * operator in `KAreaDensityUnitOperators.kt`, built as an expression (`kilo.grams / (meters pow 2)`), or
 * via [toAreaDensity], and read back with `into` against such an expression.
 *
 * Example:
 * ```kotlin
 * val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
 * q into (grams / (milli.meters pow 2))                        // 0.005
 * ```
 */
class KAreaDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAreaDensityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new area-density value with the component [value] scaled by [factor]. */
    override fun scaledBy(factor: Double): KAreaDensityUnitInstance = areaDensityUnitInstanceOf(value * factor)

    /**
     * Adds two area densities, automatically converting between the constructing units since both
     * operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KAreaDensityUnitInstance): KAreaDensityUnitInstance =
        KAreaDensityUnitInstance(instance + other.instance)

    /** Subtracts two area densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAreaDensityUnitInstance): KAreaDensityUnitInstance =
        KAreaDensityUnitInstance(instance - other.instance)

    /** Multiplies two area densities, producing a new [KMixedUnitInstance] (no longer a "pure" area density). */
    operator fun times(other: KAreaDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two area densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAreaDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two area densities by their normalized component [value]. */
    override operator fun compareTo(other: KAreaDensityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value]. */
    override fun equals(other: Any?): Boolean = other is KAreaDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg/m², e.g. `"5.0 kg/m²"`. */
    override fun toString(): String = "${value / KGM2_IN_BASE} ${KAreaDensityUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" area-density value, as long as it consists of exactly two terms:
 * one [KMassUnit] term at exponent `+1` and one [KDistanceUnit] term at exponent `-2` (order
 * independent). The terms are normalized to their component base units.
 *
 * @throws IllegalStateException if this instance is not a mass·length⁻² area density.
 */
fun KMixedUnitInstance.toAreaDensity(): KAreaDensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 2 && massTerm != null && lengthTerm != null) {
        "KMixedUnitInstance $this does not represent a pure area density (expected one KMassUnit^1 and one KDistanceUnit^-2 term)"
    }
    val component = value * massTerm.unit.baseValue * Math.pow(lengthTerm.unit.baseValue, -2.0)
    return areaDensityUnitInstanceOf(component)
}

/** Builds a [KAreaDensityUnitInstance] from a raw component value (`g·m⁻²`). */
internal fun areaDensityUnitInstanceOf(componentValue: Double): KAreaDensityUnitInstance =
    KAreaDensityUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -2)),
        ),
    )
