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

package org.pcsoft.framework.kunit.reluctance

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
 * Wraps a [KMixedUnitInstance] representing a **magnetic reluctance**, i.e. exactly four terms in the
 * canonical normal form `mass⁻¹ · distance⁻² · time² · current²` (`kg⁻¹·m⁻²·s²·A²` = `A/Wb`). The [value] is
 * always normalized internally to amperes per weber ([KReluctanceUnit.BASE]), regardless of which
 * [KReluctanceUnit], SI [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Reluctance is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KReluctanceUnitBareValues.kt` (e.g. `5 of amperesPerWeber`),
 * the prefixed templates in `KReluctanceUnitExtensions.kt` (e.g. `2 of mega.amperesPerWeber`), the typed
 * operators (`current / magneticFlux`, `1 / inductance`), or [toReluctance] on a canonical
 * `mass⁻¹·length⁻²·time²·current²` expression.
 *
 * Example:
 * ```kotlin
 * val rm = 2 of mega.amperesPerWeber
 * rm.value // 2.0e6 (normalized to amperes per weber)
 * ```
 */
class KReluctanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KReluctanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new reluctance with [value] (amperes per weber) scaled by [factor]. Backs number-times-unit
     * construction (`10 of mega.amperesPerWeber`).
     */
    override fun scaledBy(factor: Double): KReluctanceUnitInstance = reluctanceInstanceOf(value * factor)

    /**
     * Adds two reluctances - the series connection of a magnetic circuit. Both operands are always normalized
     * to [KReluctanceUnit.BASE] (amperes per weber) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of amperesPerWeber) + (1 of inverseHenries)).value // 2.0
     * ```
     */
    override operator fun plus(other: KReluctanceUnitInstance): KReluctanceUnitInstance =
        reluctanceInstanceOf(value + other.value)

    /** Subtracts two reluctances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KReluctanceUnitInstance): KReluctanceUnitInstance =
        reluctanceInstanceOf(value - other.value)

    /** Multiplies two reluctances, producing a new [KMixedUnitInstance] (no longer a "pure" reluctance). */
    operator fun times(other: KReluctanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two reluctances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KReluctanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two reluctances by their normalized [value] (amperes per weber). */
    override operator fun compareTo(other: KReluctanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two reluctances are equal iff they represent the same
     * magnetic resistance (e.g. `(1 of amperesPerWeber) == (1 of inverseHenries)`).
     */
    override fun equals(other: Any?): Boolean = other is KReluctanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2000000.0 A/Wb"`. */
    override fun toString(): String = "$value ${KReluctanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The ampere per weber's SI definition uses the *kilogram* as its mass dimension
// (`1 A/Wb = 1 kg⁻¹·m⁻²·s²·A²`), whereas the mass group's base unit is the gram. The canonical normal form is
// therefore stored with the mass group's base term (gram), and this factor bridges a gram-based canonical
// product to amperes per weber. The mass exponent is *negative* (-1) here, so the bridge multiplies.
internal val AMPERE_PER_WEBER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KReluctanceUnitInstance] from a value already expressed in amperes per weber
 * ([KReluctanceUnit.BASE]).
 *
 * This is the single creation source that every reluctance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻²`, `time²`, `current²`
 * (each in its group's base unit).
 */
internal fun reluctanceInstanceOf(amperesPerWeber: Double): KReluctanceUnitInstance =
    KReluctanceUnitInstance(
        KMixedUnitInstance(
            amperesPerWeber,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 2),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" reluctance, as long as it matches the canonical reluctance normal
 * form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-2`, one
 * [KTimeUnit] term at exponent `+2` and one [KElectricCurrentUnit] term at exponent `+2` (order independent).
 * The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting
 * reluctance is expressed in amperes per weber regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance⁻²·time²·current²`
 * reluctance.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
 * raw.toReluctance().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toReluctance(): KReluctanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure reluctance (expected KMassUnit^-1, KDistanceUnit^-2, KTimeUnit^2 and KElectricCurrentUnit^2)"
    }
    val gramBaseProduct = value *
        Math.pow(massTerm.unit.baseValue, -1.0) *
        Math.pow(distanceTerm.unit.baseValue, -2.0) *
        Math.pow(timeTerm.unit.baseValue, 2.0) *
        Math.pow(currentTerm.unit.baseValue, 2.0)
    return reluctanceInstanceOf(gramBaseProduct * AMPERE_PER_WEBER_MASS_REFERENCE)
}
