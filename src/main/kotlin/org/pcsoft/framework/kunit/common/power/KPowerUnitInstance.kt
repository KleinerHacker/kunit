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

package org.pcsoft.framework.kunit.common.power

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **power**, i.e. exactly three terms in the canonical normal
 * form `mass¹ · distance² · time⁻³` (`kg·m²·s⁻³`). The [value] is always normalized internally to watts
 * ([KPowerUnit.BASE]), regardless of which [KPowerUnit], SI [KUnitPrefix], or mass/length/time combination
 * it was constructed from.
 *
 * Power is a *constructed* unit group: unlike a single-term wrapper it holds a three-term instance.
 * Instances are created via the bare tokens in `KPowerUnitBareValues.kt` (e.g. `5 of watts`), the prefixed
 * templates in `KPowerUnitExtensions.kt` (e.g. `2 of kilo.watts`), or [toPower] on a canonical
 * `mass·length²·time⁻³` expression.
 *
 * Example:
 * ```kotlin
 * val p = 2 of kilo.watts   // 2000 W
 * p.value                   // 2000.0 (normalized to watts)
 * p into watts              // 2000.0 (read back in watts)
 * ```
 */
class KPowerUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KPowerUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new power value with [value] (watts) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.watts`).
     */
    override fun scaledBy(factor: Double): KPowerUnitInstance = powerInstanceOf(value * factor)

    /**
     * Adds two powers, automatically converting between different [KPowerUnit]s since both operands are
     * always normalized to [KPowerUnit.BASE] (watts) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.watts) + (500 of watts)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KPowerUnitInstance): KPowerUnitInstance = powerInstanceOf(value + other.value)

    /** Subtracts two powers. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KPowerUnitInstance): KPowerUnitInstance = powerInstanceOf(value - other.value)

    /** Multiplies two powers, producing a new [KMixedUnitInstance] (no longer a "pure" power). */
    operator fun times(other: KPowerUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two powers, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KPowerUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two powers by their normalized [value] (watts). */
    override operator fun compareTo(other: KPowerUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two powers are equal iff they represent the same power
     * (e.g. `(1 of kilo.watts) == (1000 of watts)`).
     */
    override fun equals(other: Any?): Boolean = other is KPowerUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 W"`. */
    override fun toString(): String = "$value ${KPowerUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The watt's SI definition uses the *kilogram* as its mass dimension (`1 W = 1 kg·m²·s⁻³`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to watts. The mass exponent is
// *positive* (+1) here, so the bridge divides (like the ohm).
internal val WATT_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KPowerUnitInstance] from a value already expressed in watts ([KPowerUnit.BASE]).
 *
 * This is the single creation source that every power decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the three terms `mass¹`, `distance²`, `time⁻³` (each in
 * its group's base unit).
 */
internal fun powerInstanceOf(watts: Double): KPowerUnitInstance =
    KPowerUnitInstance(
        KMixedUnitInstance(
            watts,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" power value, as long as it matches the canonical power normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2` and one
 * [KTimeUnit] term at exponent `-3` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting power is expressed in watts regardless of
 * which concrete mass/length/time units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻³` power.
 *
 * Example:
 * ```kotlin
 * val raw = 60 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
 * raw.toPower().value // 60.0
 * ```
 */
fun KMixedUnitInstance.toPower(): KPowerUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure power (expected KMassUnit^1, KDistanceUnit^2 and KTimeUnit^-3)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, 2.0) *
            Math.pow(timeTerm.unit.baseValue, -3.0)
    return powerInstanceOf(gramBaseProduct / WATT_MASS_REFERENCE)
}
