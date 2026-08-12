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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **magnetic dipole moment** (current times the area it
 * encloses), i.e. exactly two terms in the canonical normal form - [KElectricCurrentUnit.BASE] (ampere) at
 * exponent `+1` and [KDistanceUnit.BASE] (meter) at exponent `+2` (`A·m²`). Both components are stored in
 * their group's base unit, so the raw component base *is* the named base unit
 * ([KMagneticMomentUnit.AMPERE_SQUARE_METER]) and no bridging factor is needed.
 *
 * Magnetic moment is a *constructed* unit group with one decomposition, funnelling into
 * [magneticMomentInstanceOf]:
 * * `current * area` (typed operator, see `KMagneticMomentUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KMagneticMomentUnitBareValues.kt` (e.g.
 * `2 of ampereSquareMeters`), the prefixed templates in `KMagneticMomentUnitExtensions.kt`, or
 * [toMagneticMoment].
 *
 * Example:
 * ```kotlin
 * val m = (2 of amperes) * ((0.1 of meters) * (0.05 of meters)) // a coil loop: 0.01 A·m²
 * m into ampereSquareMeters
 * ```
 */
class KMagneticMomentUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMagneticMomentUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new magnetic moment with [value] (A·m²) scaled by [factor]. Backs number-times-unit
     * construction (`2 of ampereSquareMeters`).
     */
    override fun scaledBy(factor: Double): KMagneticMomentUnitInstance =
        magneticMomentInstanceOf(value * factor)

    /**
     * Adds two magnetic moments, automatically converting between different [KMagneticMomentUnit]s since
     * both operands are always normalized to [KMagneticMomentUnit.BASE] (A·m²) internally.
     */
    override operator fun plus(other: KMagneticMomentUnitInstance): KMagneticMomentUnitInstance =
        magneticMomentInstanceOf(value + other.value)

    /** Subtracts two magnetic moments. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMagneticMomentUnitInstance): KMagneticMomentUnitInstance =
        magneticMomentInstanceOf(value - other.value)

    /** Multiplies two magnetic moments, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMagneticMomentUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two magnetic moments, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMagneticMomentUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two magnetic moments by their normalized [value] (A·m²). */
    override operator fun compareTo(other: KMagneticMomentUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two magnetic moments are equal iff they represent the same
     * quantity (e.g. `(1 of ampereSquareMeters) == (1 of joulesPerTesla)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KMagneticMomentUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in A·m², e.g. `"0.01 A*m^2"`. */
    override fun toString(): String = "$value ${KMagneticMomentUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMagneticMomentUnitInstance] from a value already expressed in ampere square meters
 * ([KMagneticMomentUnit.BASE]).
 *
 * This is the single creation source that every magnetic moment decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the two terms `current¹` and `distance²`
 * (each in its group's base unit).
 */
internal fun magneticMomentInstanceOf(ampereSquareMeters: Double): KMagneticMomentUnitInstance =
    KMagneticMomentUnitInstance(
        KMixedUnitInstance(
            ampereSquareMeters,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
            ),
        ),
    )

/**
 * Builds a value-1 [KMagneticMomentUnitInstance] for the given [unit] (its
 * [KMagneticMomentUnit.baseValue] A·m²).
 */
internal fun magneticMomentOfUnit(unit: KMagneticMomentUnit): KMagneticMomentUnitInstance =
    magneticMomentInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" magnetic moment, as long as it matches the canonical magnetic
 * moment normal form: exactly one [KElectricCurrentUnit] term at exponent `+1` and one [KDistanceUnit]
 * term at exponent `+2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in A·m² regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current·distance²` magnetic moment.
 */
fun KMixedUnitInstance.toMagneticMoment(): KMagneticMomentUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    check(units.size == 2 && currentTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure magnetic moment " +
                "(expected one KElectricCurrentUnit^1 and one KDistanceUnit^2 term)"
    }
    val distanceBase = distanceTerm.unit.baseValue
    return magneticMomentInstanceOf(value * currentTerm.unit.baseValue * distanceBase * distanceBase)
}
