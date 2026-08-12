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

package org.pcsoft.framework.kunit.electric.capacitance

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electrical capacitance**, i.e. exactly four terms in the
 * canonical normal form `mass⁻¹ · distance⁻² · time⁴ · current²` (`kg⁻¹·m⁻²·s⁴·A²`). The [value] is always
 * normalized internally to farads ([KCapacitanceUnit.BASE]), regardless of which [KCapacitanceUnit], SI
 * [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Capacitance is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KCapacitanceUnitBareValues.kt` (e.g. `5 of farads`), the
 * prefixed templates in `KCapacitanceUnitExtensions.kt` (e.g. `2 of micro.farads`), or [toCapacitance] on a
 * canonical `mass⁻¹·length⁻²·time⁴·current²` expression.
 *
 * Example:
 * ```kotlin
 * val c = 470 of micro.farads   // 0.00047 F
 * c.value                       // 4.7e-4 (normalized to farads)
 * c into farads                 // 4.7e-4 (read back in farads)
 * ```
 */
class KCapacitanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KCapacitanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new capacitance value with [value] (farads) scaled by [factor]. Backs number-times-unit
     * construction (`10 of micro.farads`).
     */
    override fun scaledBy(factor: Double): KCapacitanceUnitInstance = capacitanceInstanceOf(value * factor)

    /**
     * Adds two capacitances, automatically converting between different [KCapacitanceUnit]s since both
     * operands are always normalized to [KCapacitanceUnit.BASE] (farads) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of milli.farads) + (500 of micro.farads)).value // 0.0015
     * ```
     */
    override operator fun plus(other: KCapacitanceUnitInstance): KCapacitanceUnitInstance =
        capacitanceInstanceOf(value + other.value)

    /** Subtracts two capacitances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KCapacitanceUnitInstance): KCapacitanceUnitInstance =
        capacitanceInstanceOf(value - other.value)

    /**
     * Multiplies two capacitances, producing a new [KMixedUnitInstance] (no longer a "pure" capacitance).
     */
    operator fun times(other: KCapacitanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two capacitances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KCapacitanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two capacitances by their normalized [value] (farads). */
    override operator fun compareTo(other: KCapacitanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two capacitances are equal iff they represent the same
     * capacitance (e.g. `(1 of milli.farads) == (1000 of micro.farads)`).
     */
    override fun equals(other: Any?): Boolean = other is KCapacitanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 F"`. */
    override fun toString(): String = "${renderDouble(value)} ${KCapacitanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The farad's SI definition uses the *kilogram* as its mass dimension (`1 F = 1 kg⁻¹·m⁻²·s⁴·A²`), whereas
// the mass group's base unit is the gram. The canonical normal form is therefore stored with the mass
// group's base term (gram), and this factor bridges a gram-based canonical product to farads. Because the
// mass exponent is *negative* (-1) here, the bridge multiplies (`1 kg⁻¹ = 0.001 g⁻¹`) instead of dividing.
private val FARAD_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KCapacitanceUnitInstance] from a value already expressed in farads ([KCapacitanceUnit.BASE]).
 *
 * This is the single creation source that every capacitance decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻²`, `time⁴`,
 * `current²` (each in its group's base unit).
 */
internal fun capacitanceInstanceOf(farads: Double): KCapacitanceUnitInstance =
    KCapacitanceUnitInstance(
        KMixedUnitInstance(
            farads,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 4),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" capacitance value, as long as it matches the canonical capacitance
 * normal form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-2`,
 * one [KTimeUnit] term at exponent `+4` and one [KElectricCurrentUnit] term at exponent `+2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting capacitance is expressed in farads regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance⁻²·time⁴·current²`
 * capacitance.
 *
 * Example:
 * ```kotlin
 * val raw = 115 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
 * raw.toCapacitance().value // 115.0
 * ```
 */
fun KMixedUnitInstance.toCapacitance(): KCapacitanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 4 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure capacitance (expected KMassUnit^-1, KDistanceUnit^-2, KTimeUnit^4 and KElectricCurrentUnit^2)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue.pow(-1.0) *
            distanceTerm.unit.baseValue.pow(-2.0) *
            timeTerm.unit.baseValue.pow(4.0) *
            currentTerm.unit.baseValue.pow(2.0)
    return capacitanceInstanceOf(gramBaseProduct * FARAD_MASS_REFERENCE)
}
