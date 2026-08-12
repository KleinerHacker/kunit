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

package org.pcsoft.framework.kunit.electric.charge

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric charge**, i.e. exactly two terms in the canonical
 * normal form `current¹ · time¹` (`A·s`). The [value] is always normalized internally to coulombs
 * ([KChargeUnit.BASE]), regardless of which [KChargeUnit], SI [org.pcsoft.framework.kunit.KUnitPrefix], or
 * current/time combination it was constructed from.
 *
 * Charge is a *constructed* unit group: unlike a single-term wrapper it holds a two-term instance.
 * Instances are created via the bare tokens in `KChargeUnitBareValues.kt` (e.g. `5 of coulombs`), the
 * prefixed templates in `KChargeUnitExtensions.kt` (e.g. `2 of milli.ampereHours`), or [toCharge] on a
 * canonical current·time expression.
 *
 * Example:
 * ```kotlin
 * val q = 2 of ampereHours   // 7200 C
 * q.value                    // 7200.0 (normalized to coulombs)
 * q into coulombs            // 7200.0 (read back in coulombs)
 * ```
 */
class KChargeUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KChargeUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new charge value with [value] (coulombs) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.coulombs`).
     */
    override fun scaledBy(factor: Double): KChargeUnitInstance = chargeInstanceOf(value * factor)

    /**
     * Adds two charges, automatically converting between different [KChargeUnit]s since both operands are
     * always normalized to [KChargeUnit.BASE] (coulombs) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of ampereHours) + (600 of coulombs)).value // 4200.0
     * ```
     */
    override operator fun plus(other: KChargeUnitInstance): KChargeUnitInstance = chargeInstanceOf(value + other.value)

    /** Subtracts two charges. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KChargeUnitInstance): KChargeUnitInstance = chargeInstanceOf(value - other.value)

    /** Multiplies two charges, producing a new [KMixedUnitInstance] (no longer a "pure" charge). */
    operator fun times(other: KChargeUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two charges, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KChargeUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two charges by their normalized [value] (coulombs). */
    override operator fun compareTo(other: KChargeUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two charges are equal iff they represent the same charge
     * (e.g. `(1 of ampereHours) == (3600 of coulombs)`).
     */
    override fun equals(other: Any?): Boolean = other is KChargeUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 C"`. */
    override fun toString(): String = "${renderDouble(value)} ${KChargeUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KChargeUnitInstance] from a value already expressed in coulombs ([KChargeUnit.BASE]).
 *
 * This is the single creation source that every charge decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the two terms `current¹` and `time¹` (each in its
 * group's base unit).
 */
internal fun chargeInstanceOf(coulombs: Double): KChargeUnitInstance =
    KChargeUnitInstance(
        KMixedUnitInstance(
            coulombs,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" charge value, as long as it matches the canonical charge normal
 * form: exactly one [KElectricCurrentUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `+1`
 * (order independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s,
 * so the resulting charge is expressed in coulombs regardless of which concrete current/time units the
 * terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·time¹` charge.
 *
 * Example:
 * ```kotlin
 * val raw = (2 of amperes).toUnit() * (3600 of seconds).toUnit()
 * raw.toCharge().value // 7200.0
 * ```
 */
fun KMixedUnitInstance.toCharge(): KChargeUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    check(units.size == 2 && currentTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure charge (expected KElectricCurrentUnit^1 and KTimeUnit^1)"
    }
    return chargeInstanceOf(value * currentTerm.unit.baseValue * timeTerm.unit.baseValue)
}
