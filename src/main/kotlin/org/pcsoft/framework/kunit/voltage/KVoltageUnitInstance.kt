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

package org.pcsoft.framework.kunit.voltage

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
 * Wraps a [KMixedUnitInstance] representing a **voltage**, i.e. exactly four terms in the canonical
 * normal form `mass¹ · distance² · time⁻³ · current⁻¹` (`kg·m²·s⁻³·A⁻¹`). The [value] is always
 * normalized internally to volts ([KVoltageUnit.BASE]), regardless of which [KVoltageUnit], SI
 * [org.pcsoft.framework.kunit.KUnitPrefix], or mass/length/time/current combination it was constructed
 * from.
 *
 * Voltage is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KVoltageUnitBareValues.kt` (e.g. `5 of volts`), the
 * prefixed templates in `KVoltageUnitExtensions.kt` (e.g. `2 of kilo.volts`), or [toVoltage] on a
 * canonical mass·length²·time⁻³·current⁻¹ expression.
 *
 * Example:
 * ```kotlin
 * val u = 2 of kilo.volts   // 2000 V
 * u.value                   // 2000.0 (normalized to volts)
 * u into volts              // 2000.0 (read back in volts)
 * ```
 */
class KVoltageUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KVoltageUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new voltage value with [value] (volts) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.volts`).
     */
    override fun scaledBy(factor: Double): KVoltageUnitInstance = voltageInstanceOf(value * factor)

    /**
     * Adds two voltages, automatically converting between different [KVoltageUnit]s since both operands
     * are always normalized to [KVoltageUnit.BASE] (volts) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.volts) + (500 of volts)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KVoltageUnitInstance): KVoltageUnitInstance = voltageInstanceOf(value + other.value)

    /** Subtracts two voltages. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KVoltageUnitInstance): KVoltageUnitInstance = voltageInstanceOf(value - other.value)

    /**
     * Multiplies two voltages, producing a new [KMixedUnitInstance] (no longer a "pure" voltage).
     */
    operator fun times(other: KVoltageUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two voltages, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KVoltageUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two voltages by their normalized [value] (volts). */
    override operator fun compareTo(other: KVoltageUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two voltages are equal iff they represent the same
     * potential difference (e.g. `(1 of kilo.volts) == (1000 of volts)`).
     */
    override fun equals(other: Any?): Boolean = other is KVoltageUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 V"`. */
    override fun toString(): String = "$value ${KVoltageUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The volt's SI definition uses the *kilogram* as its mass dimension (`1 V = 1 kg·m²·s⁻³·A⁻¹`), whereas
// the mass group's base unit is the gram. The canonical normal form is therefore stored with the mass
// group's base term (gram), and this factor bridges a gram-based canonical product to volts.
private val VOLT_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KVoltageUnitInstance] from a value already expressed in volts ([KVoltageUnit.BASE]).
 *
 * This is the single creation source that every voltage decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻³`,
 * `current⁻¹` (each in its group's base unit).
 */
internal fun voltageInstanceOf(volts: Double): KVoltageUnitInstance =
    KVoltageUnitInstance(
        KMixedUnitInstance(
            volts,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KElectricCurrentUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" voltage value, as long as it matches the canonical voltage normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2`, one
 * [KTimeUnit] term at exponent `-3` and one [KElectricCurrentUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting voltage is expressed in volts regardless of which concrete mass/length/time/current units the
 * terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻³·current⁻¹`
 * voltage.
 *
 * Example:
 * ```kotlin
 * val raw = (10 of kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
 * raw.toVoltage().value // 10.0
 * ```
 */
fun KMixedUnitInstance.toVoltage(): KVoltageUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure voltage (expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-3 and KElectricCurrentUnit^-1)"
    }
    val gramBaseProduct = value *
        massTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, 2.0) *
        Math.pow(timeTerm.unit.baseValue, -3.0) *
        Math.pow(currentTerm.unit.baseValue, -1.0)
    return voltageInstanceOf(gramBaseProduct / VOLT_MASS_REFERENCE)
}
