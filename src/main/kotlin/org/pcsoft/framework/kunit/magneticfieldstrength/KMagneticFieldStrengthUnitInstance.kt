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

package org.pcsoft.framework.kunit.magneticfieldstrength

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **magnetic field strength**, i.e. exactly two terms in the
 * canonical normal form `current¹ · distance⁻¹` (`A/m`). The [value] is always normalized internally to
 * amperes per meter ([KMagneticFieldStrengthUnit.BASE]), regardless of which
 * [KMagneticFieldStrengthUnit], SI [org.pcsoft.framework.kunit.KUnitPrefix], or current/length combination
 * it was constructed from.
 *
 * Magnetic field strength is a *constructed* unit group: unlike a single-term wrapper it holds a two-term
 * instance. Instances are created via the bare tokens in `KMagneticFieldStrengthUnitBareValues.kt` (e.g.
 * `5 of amperesPerMeter`), the prefixed templates in `KMagneticFieldStrengthUnitExtensions.kt` (e.g.
 * `2 of kilo.amperesPerMeter`), or [toMagneticFieldStrength] on a canonical current·length⁻¹ expression.
 *
 * Example:
 * ```kotlin
 * val h = 2 of kilo.amperesPerMeter   // 2000 A/m
 * h.value                             // 2000.0 (normalized to A/m)
 * h into amperesPerMeter              // 2000.0 (read back in A/m)
 * ```
 */
class KMagneticFieldStrengthUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMagneticFieldStrengthUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new magnetic field strength value with [value] (amperes per meter) scaled by [factor].
     * Backs number-times-unit construction (`10 of kilo.amperesPerMeter`).
     */
    override fun scaledBy(factor: Double): KMagneticFieldStrengthUnitInstance =
        magneticFieldStrengthInstanceOf(value * factor)

    /**
     * Adds two magnetic field strengths, automatically converting between different
     * [KMagneticFieldStrengthUnit]s since both operands are always normalized to
     * [KMagneticFieldStrengthUnit.BASE] (A/m) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.amperesPerMeter) + (500 of amperesPerMeter)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KMagneticFieldStrengthUnitInstance): KMagneticFieldStrengthUnitInstance =
        magneticFieldStrengthInstanceOf(value + other.value)

    /** Subtracts two magnetic field strengths. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMagneticFieldStrengthUnitInstance): KMagneticFieldStrengthUnitInstance =
        magneticFieldStrengthInstanceOf(value - other.value)

    /**
     * Multiplies two magnetic field strengths, producing a new [KMixedUnitInstance] (no longer a "pure"
     * magnetic field strength).
     */
    operator fun times(other: KMagneticFieldStrengthUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two magnetic field strengths, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMagneticFieldStrengthUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two magnetic field strengths by their normalized [value] (A/m). */
    override operator fun compareTo(other: KMagneticFieldStrengthUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two magnetic field strengths are equal iff they represent
     * the same field strength (e.g. `(1 of kilo.amperesPerMeter) == (1000 of amperesPerMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KMagneticFieldStrengthUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 A/m"`. */
    override fun toString(): String = "$value ${KMagneticFieldStrengthUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMagneticFieldStrengthUnitInstance] from a value already expressed in amperes per meter
 * ([KMagneticFieldStrengthUnit.BASE]).
 *
 * This is the single creation source that every magnetic field strength decomposition must funnel into:
 * it assembles the canonical normal-form [KMixedUnitInstance] with the two terms `current¹` and
 * `distance⁻¹` (each in its group's base unit).
 */
internal fun magneticFieldStrengthInstanceOf(amperesPerMeter: Double): KMagneticFieldStrengthUnitInstance =
    KMagneticFieldStrengthUnitInstance(
        KMixedUnitInstance(
            amperesPerMeter,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" magnetic field strength value, as long as it matches the canonical
 * normal form: exactly one [KElectricCurrentUnit] term at exponent `+1` and one [KDistanceUnit] term at
 * exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting field strength is expressed in amperes
 * per meter regardless of which concrete current/length units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·distance⁻¹` magnetic field
 * strength.
 *
 * Example:
 * ```kotlin
 * val raw = 250 of (amperes pow 1) / (meters pow 1)
 * raw.toMagneticFieldStrength().value // 250.0
 * ```
 */
fun KMixedUnitInstance.toMagneticFieldStrength(): KMagneticFieldStrengthUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    check(units.size == 2 && currentTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure magnetic field strength (expected KElectricCurrentUnit^1 and KDistanceUnit^-1)"
    }
    val normalizedValue = value *
        currentTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, -1.0)
    return magneticFieldStrengthInstanceOf(normalizedValue)
}
