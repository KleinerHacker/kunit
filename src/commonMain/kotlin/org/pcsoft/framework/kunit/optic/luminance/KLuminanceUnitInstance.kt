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

package org.pcsoft.framework.kunit.optic.luminance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **luminance** (luminous intensity per emitting area), i.e.
 * exactly two terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at exponent
 * `+1` and [KDistanceUnit.BASE] (meter) at exponent `-2` (`cd·m⁻²`). Both components are stored in their
 * group's base unit, so the raw component base *is* the named base unit
 * ([KLuminanceUnit.CANDELA_PER_SQUARE_METER]) and no bridging factor is needed.
 *
 * Luminance is a *constructed* unit group with two decompositions, both funnelling into
 * [luminanceInstanceOf]:
 * * `luminousIntensity / area` (typed operator, see `KLuminanceUnitOperators.kt`)
 * * `illuminance / solidAngle` (typed operator, same file)
 *
 * Instances are additionally created via the bare tokens in `KLuminanceUnitBareValues.kt` (e.g.
 * `250 of candelasPerSquareMeter`), the prefixed templates in `KLuminanceUnitExtensions.kt`, or
 * [toLuminance].
 *
 * Example:
 * ```kotlin
 * val l = (250 of candelas) / ((1 of meters) * (1 of meters)) // monitor: 250 cd/m² (nits)
 * l into candelasPerSquareMeter
 * ```
 */
class KLuminanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLuminanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new luminance with [value] (cd/m²) scaled by [factor]. Backs number-times-unit
     * construction (`250 of candelasPerSquareMeter`).
     */
    override fun scaledBy(factor: Double): KLuminanceUnitInstance = luminanceInstanceOf(value * factor)

    /**
     * Adds two luminances, automatically converting between different [KLuminanceUnit]s since both
     * operands are always normalized to [KLuminanceUnit.BASE] (cd/m²) internally.
     */
    override operator fun plus(other: KLuminanceUnitInstance): KLuminanceUnitInstance =
        luminanceInstanceOf(value + other.value)

    /** Subtracts two luminances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminanceUnitInstance): KLuminanceUnitInstance =
        luminanceInstanceOf(value - other.value)

    /** Multiplies two luminances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KLuminanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two luminances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLuminanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two luminances by their normalized [value] (cd/m²). */
    override operator fun compareTo(other: KLuminanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminances are equal iff they represent the same
     * quantity (e.g. `(1 of stilbs) == (10000 of candelasPerSquareMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KLuminanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in cd/m², e.g. `"250.0 cd/m^2"`. */
    override fun toString(): String = "${renderDouble(value)} ${KLuminanceUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminanceUnitInstance] from a value already expressed in candelas per square meter
 * ([KLuminanceUnit.BASE]).
 *
 * This is the single creation source that every luminance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the two terms `luminousIntensity¹` and `distance⁻²`
 * (each in its group's base unit).
 */
internal fun luminanceInstanceOf(candelasPerSquareMeter: Double): KLuminanceUnitInstance =
    KLuminanceUnitInstance(
        KMixedUnitInstance(
            candelasPerSquareMeter,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
            ),
        ),
    )

/** Builds a value-1 [KLuminanceUnitInstance] for the given [unit] (its [KLuminanceUnit.baseValue] cd/m²). */
internal fun luminanceOfUnit(unit: KLuminanceUnit): KLuminanceUnitInstance = luminanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" luminance, as long as it matches the canonical luminance normal
 * form: exactly one [KLuminousIntensityUnit] term at exponent `+1` and one [KDistanceUnit] term at
 * exponent `-2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in cd/m² regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `luminousIntensity·distance⁻²`
 * luminance.
 */
fun KMixedUnitInstance.toLuminance(): KLuminanceUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 2 && intensityTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure luminance " +
                "(expected one KLuminousIntensityUnit^1 and one KDistanceUnit^-2 term)"
    }
    val distanceBase = distanceTerm.unit.baseValue
    return luminanceInstanceOf(value * intensityTerm.unit.baseValue / (distanceBase * distanceBase))
}
