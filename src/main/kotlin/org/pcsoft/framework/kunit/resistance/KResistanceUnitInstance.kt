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

package org.pcsoft.framework.kunit.resistance

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
 * Wraps a [KMixedUnitInstance] representing an **electrical resistance**, i.e. exactly four terms in the
 * canonical normal form `mass¹ · distance² · time⁻³ · current⁻²` (`kg·m²·s⁻³·A⁻²`). The [value] is always
 * normalized internally to ohms ([KResistanceUnit.BASE]), regardless of which [KResistanceUnit], SI
 * [org.pcsoft.framework.kunit.KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Resistance is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KResistanceUnitBareValues.kt` (e.g. `5 of ohms`), the
 * prefixed templates in `KResistanceUnitExtensions.kt` (e.g. `2 of kilo.ohms`), or [toResistance] on a
 * canonical mass·length²·time⁻³·current⁻² expression.
 *
 * Example:
 * ```kotlin
 * val u = 2 of kilo.ohms   // 2000 Ω
 * u.value                  // 2000.0 (normalized to ohms)
 * u into ohms              // 2000.0 (read back in ohms)
 * ```
 */
class KResistanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KResistanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new resistance value with [value] (ohms) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.ohms`).
     */
    override fun scaledBy(factor: Double): KResistanceUnitInstance = resistanceInstanceOf(value * factor)

    /**
     * Adds two resistances, automatically converting between different [KResistanceUnit]s since both
     * operands are always normalized to [KResistanceUnit.BASE] (ohms) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.ohms) + (500 of ohms)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KResistanceUnitInstance): KResistanceUnitInstance = resistanceInstanceOf(value + other.value)

    /** Subtracts two resistances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KResistanceUnitInstance): KResistanceUnitInstance = resistanceInstanceOf(value - other.value)

    /**
     * Multiplies two resistances, producing a new [KMixedUnitInstance] (no longer a "pure" resistance).
     */
    operator fun times(other: KResistanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two resistances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KResistanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two resistances by their normalized [value] (ohms). */
    override operator fun compareTo(other: KResistanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two resistances are equal iff they represent the same
     * resistance (e.g. `(1 of kilo.ohms) == (1000 of ohms)`).
     */
    override fun equals(other: Any?): Boolean = other is KResistanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 Ω"`. */
    override fun toString(): String = "$value ${KResistanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The ohm's SI definition uses the *kilogram* as its mass dimension (`1 Ω = 1 kg·m²·s⁻³·A⁻²`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to ohms.
private val OHM_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KResistanceUnitInstance] from a value already expressed in ohms ([KResistanceUnit.BASE]).
 *
 * This is the single creation source that every resistance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻³`,
 * `current⁻²` (each in its group's base unit).
 */
internal fun resistanceInstanceOf(ohms: Double): KResistanceUnitInstance =
    KResistanceUnitInstance(
        KMixedUnitInstance(
            ohms,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KElectricCurrentUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" resistance value, as long as it matches the canonical resistance
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2`,
 * one [KTimeUnit] term at exponent `-3` and one [KElectricCurrentUnit] term at exponent `-2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting resistance is expressed in ohms regardless of which concrete mass/length/time/current units the
 * terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻³·current⁻²`
 * resistance.
 *
 * Example:
 * ```kotlin
 * val raw = (10 of kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
 * raw.toResistance().value // 10.0
 * ```
 */
fun KMixedUnitInstance.toResistance(): KResistanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure resistance (expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-3 and KElectricCurrentUnit^-2)"
    }
    val gramBaseProduct = value *
        massTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, 2.0) *
        Math.pow(timeTerm.unit.baseValue, -3.0) *
        Math.pow(currentTerm.unit.baseValue, -2.0)
    return resistanceInstanceOf(gramBaseProduct / OHM_MASS_REFERENCE)
}
