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

package org.pcsoft.framework.kunit.electric.resistivity

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electrical resistivity**, i.e. exactly four terms in the
 * canonical normal form `mass¹ · distance³ · time⁻³ · current⁻²` (`kg·m³·s⁻³·A⁻²`). The [value] is always
 * normalized internally to ohm meters ([KResistivityUnit.BASE]), regardless of which [KResistivityUnit], SI
 * [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Resistivity is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KResistivityUnitBareValues.kt` (e.g. `5 of ohmMeters`), the
 * prefixed templates in `KResistivityUnitExtensions.kt` (e.g. `2 of micro.ohmMeters`), or [toResistivity] on
 * a canonical `mass·length³·time⁻³·current⁻²` expression.
 *
 * Example:
 * ```kotlin
 * val rho = 17 of nano.ohmMeters   // copper, 1.7e-8 Ω·m
 * rho.value                        // 1.7e-8 (normalized to ohm meters)
 * rho into ohmMeters               // 1.7e-8 (read back in ohm meters)
 * ```
 */
class KResistivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KResistivityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new resistivity value with [value] (ohm meters) scaled by [factor]. Backs number-times-unit
     * construction (`10 of micro.ohmMeters`).
     */
    override fun scaledBy(factor: Double): KResistivityUnitInstance = resistivityInstanceOf(value * factor)

    /**
     * Adds two resistivities, automatically converting between different [KResistivityUnit]s since both
     * operands are always normalized to [KResistivityUnit.BASE] (ohm meters) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of ohmMeters) + (100 of ohmCentimeters)).value // 2.0
     * ```
     */
    override operator fun plus(other: KResistivityUnitInstance): KResistivityUnitInstance =
        resistivityInstanceOf(value + other.value)

    /** Subtracts two resistivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KResistivityUnitInstance): KResistivityUnitInstance =
        resistivityInstanceOf(value - other.value)

    /** Multiplies two resistivities, producing a new [KMixedUnitInstance] (no longer a "pure" resistivity). */
    operator fun times(other: KResistivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two resistivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KResistivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two resistivities by their normalized [value] (ohm meters). */
    override operator fun compareTo(other: KResistivityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two resistivities are equal iff they represent the same
     * resistivity (e.g. `(1 of ohmMeters) == (100 of ohmCentimeters)`).
     */
    override fun equals(other: Any?): Boolean = other is KResistivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 Ω·m"`. */
    override fun toString(): String = "${renderDouble(value)} ${KResistivityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The ohm meter's SI definition uses the *kilogram* as its mass dimension (`1 Ω·m = 1 kg·m³·s⁻³·A⁻²`),
// whereas the mass group's base unit is the gram. The canonical normal form is therefore stored with the
// mass group's base term (gram), and this factor bridges a gram-based canonical product to ohm meters. The
// mass exponent is *positive* (+1) here, so the bridge divides (like the ohm).
internal val OHM_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KResistivityUnitInstance] from a value already expressed in ohm meters
 * ([KResistivityUnit.BASE]).
 *
 * This is the single creation source that every resistivity decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance³`, `time⁻³`, `current⁻²`
 * (each in its group's base unit).
 */
internal fun resistivityInstanceOf(ohmMeters: Double): KResistivityUnitInstance =
    KResistivityUnitInstance(
        KMixedUnitInstance(
            ohmMeters,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 3),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KElectricCurrentUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" resistivity value, as long as it matches the canonical resistivity
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+3`, one
 * [KTimeUnit] term at exponent `-3` and one [KElectricCurrentUnit] term at exponent `-2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting resistivity is expressed in ohm meters regardless of which concrete mass/length/time/current
 * units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance³·time⁻³·current⁻²`
 * resistivity.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
 * raw.toResistivity().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toResistivity(): KResistivityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 3 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure resistivity (expected KMassUnit^1, KDistanceUnit^3, KTimeUnit^-3 and KElectricCurrentUnit^-2)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(3.0) *
            timeTerm.unit.baseValue.pow(-3.0) *
            currentTerm.unit.baseValue.pow(-2.0)
    return resistivityInstanceOf(gramBaseProduct / OHM_METER_MASS_REFERENCE)
}
