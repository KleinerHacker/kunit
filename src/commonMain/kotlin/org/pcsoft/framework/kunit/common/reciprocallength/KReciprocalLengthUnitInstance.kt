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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **reciprocal length**, i.e. exactly one term in the
 * canonical normal form - [KDistanceUnit.BASE] (meter) at exponent `-1` (`m⁻¹`).
 *
 * The group is deliberately named neutrally: it carries the **dioptre** of optics (refractive power
 * `D = 1/f`) and the **wavenumber** of spectroscopy (`ṽ = 1/λ`) alike, because both share the normal form
 * `distance⁻¹` and a single type keeps [toReciprocalLength] deterministic.
 *
 * Reciprocal length is a *constructed* unit group with one decomposition, funnelling into
 * [reciprocalLengthInstanceOf]:
 * * `count / length` (typed operator, see `KReciprocalLengthUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KReciprocalLengthUnitBareValues.kt` (e.g.
 * `2.5 of dioptres`), the prefixed templates in `KReciprocalLengthUnitExtensions.kt`, or
 * [toReciprocalLength].
 *
 * Example:
 * ```kotlin
 * val d = 1 / (0.4 of meters) // reading glasses: 2.5 dpt
 * d into dioptres
 * ```
 */
class KReciprocalLengthUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KReciprocalLengthUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new reciprocal length with [value] (m⁻¹) scaled by [factor]. Backs number-times-unit
     * construction (`2.5 of dioptres`).
     */
    override fun scaledBy(factor: Double): KReciprocalLengthUnitInstance =
        reciprocalLengthInstanceOf(value * factor)

    /**
     * Adds two reciprocal lengths, automatically converting between different [KReciprocalLengthUnit]s
     * since both operands are always normalized to [KReciprocalLengthUnit.BASE] (m⁻¹) internally. This is
     * exactly how the powers of two thin lenses in contact combine.
     */
    override operator fun plus(other: KReciprocalLengthUnitInstance): KReciprocalLengthUnitInstance =
        reciprocalLengthInstanceOf(value + other.value)

    /** Subtracts two reciprocal lengths. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KReciprocalLengthUnitInstance): KReciprocalLengthUnitInstance =
        reciprocalLengthInstanceOf(value - other.value)

    /** Multiplies two reciprocal lengths, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KReciprocalLengthUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two reciprocal lengths, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KReciprocalLengthUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two reciprocal lengths by their normalized [value] (m⁻¹). */
    override operator fun compareTo(other: KReciprocalLengthUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two reciprocal lengths are equal iff they represent the
     * same quantity (e.g. `(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KReciprocalLengthUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in reciprocal meters, e.g. `"2.5 1/m"`. */
    override fun toString(): String = "${renderDouble(value)} ${KReciprocalLengthUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KReciprocalLengthUnitInstance] from a value already expressed in reciprocal meters
 * ([KReciprocalLengthUnit.BASE]).
 *
 * This is the single creation source that every reciprocal length decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the single term `distance⁻¹`.
 */
internal fun reciprocalLengthInstanceOf(reciprocalMeters: Double): KReciprocalLengthUnitInstance =
    KReciprocalLengthUnitInstance(
        KMixedUnitInstance(reciprocalMeters, listOf(KUnitTerm(KDistanceUnit.BASE, -1))),
    )

/**
 * Builds a value-1 [KReciprocalLengthUnitInstance] for the given [unit] (its
 * [KReciprocalLengthUnit.baseValue] m⁻¹).
 */
internal fun reciprocalLengthOfUnit(unit: KReciprocalLengthUnit): KReciprocalLengthUnitInstance =
    reciprocalLengthInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" reciprocal length, as long as it matches the canonical reciprocal
 * length normal form: exactly one [KDistanceUnit] term at exponent `-1`. The term is normalized over its
 * [org.pcsoft.framework.kunit.KUnit.baseValue], so the result is expressed in m⁻¹ regardless of which
 * concrete unit the term was tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance⁻¹` reciprocal length.
 */
fun KMixedUnitInstance.toReciprocalLength(): KReciprocalLengthUnitInstance {
    val term = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    check(units.size == 1 && term != null) {
        "KMixedUnitInstance $this does not represent a pure reciprocal length " +
                "(expected exactly one KDistanceUnit^-1 term)"
    }
    return reciprocalLengthInstanceOf(value / term.unit.baseValue)
}
