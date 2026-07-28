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

package org.pcsoft.framework.kunit.electricfieldstrength

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnit
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric field strength**, i.e. exactly four terms in the
 * canonical normal form `mass¹ · distance¹ · time⁻³ · current⁻¹` (`kg·m·s⁻³·A⁻¹`). The [value] is always
 * normalized internally to volts per meter ([KElectricFieldStrengthUnit.BASE]), regardless of which
 * [KElectricFieldStrengthUnit], SI [KUnitPrefix], or mass/length/time/current combination it was constructed
 * from.
 *
 * Electric field strength is a *constructed* unit group: unlike a single-term wrapper it holds a four-term
 * instance. Instances are created via the bare tokens in `KElectricFieldStrengthUnitBareValues.kt` (e.g.
 * `5 of voltsPerMeter`), the prefixed templates in `KElectricFieldStrengthUnitExtensions.kt` (e.g.
 * `2 of kilo.voltsPerMeter`), the typed operators (`voltage / length`, `force / charge`), or
 * [toElectricFieldStrength] on a canonical `mass·length·time⁻³·current⁻¹` expression.
 *
 * Example:
 * ```kotlin
 * val e = 230 of voltsPerMeter
 * e.value              // 230.0 (normalized to volts per meter)
 * e into voltsPerCentimeter // 2.3
 * ```
 */
class KElectricFieldStrengthUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElectricFieldStrengthUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new electric field strength with [value] (volts per meter) scaled by [factor]. Backs
     * number-times-unit construction (`10 of kilo.voltsPerMeter`).
     */
    override fun scaledBy(factor: Double): KElectricFieldStrengthUnitInstance =
        electricFieldStrengthInstanceOf(value * factor)

    /**
     * Adds two electric field strengths, automatically converting between different
     * [KElectricFieldStrengthUnit]s since both operands are always normalized to
     * [KElectricFieldStrengthUnit.BASE] (volts per meter) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of voltsPerMeter) + (1 of voltsPerCentimeter)).value // 101.0
     * ```
     */
    override operator fun plus(other: KElectricFieldStrengthUnitInstance): KElectricFieldStrengthUnitInstance =
        electricFieldStrengthInstanceOf(value + other.value)

    /** Subtracts two electric field strengths. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricFieldStrengthUnitInstance): KElectricFieldStrengthUnitInstance =
        electricFieldStrengthInstanceOf(value - other.value)

    /**
     * Multiplies two electric field strengths, producing a new [KMixedUnitInstance] (no longer a "pure"
     * electric field strength).
     */
    operator fun times(other: KElectricFieldStrengthUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two electric field strengths, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElectricFieldStrengthUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two electric field strengths by their normalized [value] (volts per meter). */
    override operator fun compareTo(other: KElectricFieldStrengthUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric field strengths are equal iff they represent
     * the same field (e.g. `(1 of voltsPerCentimeter) == (100 of voltsPerMeter)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KElectricFieldStrengthUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"230.0 V/m"`. */
    override fun toString(): String = "$value ${KElectricFieldStrengthUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The volt per meter's SI definition uses the *kilogram* as its mass dimension (`1 V/m = 1 kg·m·s⁻³·A⁻¹`),
// whereas the mass group's base unit is the gram. The canonical normal form is therefore stored with the
// mass group's base term (gram), and this factor bridges a gram-based canonical product to volts per meter.
// The mass exponent is *positive* (+1) here, so the bridge divides (like the volt).
internal val VOLT_PER_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElectricFieldStrengthUnitInstance] from a value already expressed in volts per meter
 * ([KElectricFieldStrengthUnit.BASE]).
 *
 * This is the single creation source that every electric field strength decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance¹`,
 * `time⁻³`, `current⁻¹` (each in its group's base unit).
 */
internal fun electricFieldStrengthInstanceOf(voltsPerMeter: Double): KElectricFieldStrengthUnitInstance =
    KElectricFieldStrengthUnitInstance(
        KMixedUnitInstance(
            voltsPerMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KElectricCurrentUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" electric field strength, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+1`, one
 * [KTimeUnit] term at exponent `-3` and one [KElectricCurrentUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting field strength is expressed in volts per meter regardless of which concrete
 * mass/length/time/current units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance¹·time⁻³·current⁻¹`
 * electric field strength.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (kilo.grams * meters) / ((seconds pow 3) * amperes)
 * raw.toElectricFieldStrength().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toElectricFieldStrength(): KElectricFieldStrengthUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure electric field strength (expected KMassUnit^1, KDistanceUnit^1, KTimeUnit^-3 and KElectricCurrentUnit^-1)"
    }
    val gramBaseProduct = value *
        massTerm.unit.baseValue *
        distanceTerm.unit.baseValue *
        Math.pow(timeTerm.unit.baseValue, -3.0) *
        Math.pow(currentTerm.unit.baseValue, -1.0)
    return electricFieldStrengthInstanceOf(gramBaseProduct / VOLT_PER_METER_MASS_REFERENCE)
}
