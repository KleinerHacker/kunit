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

package org.pcsoft.framework.kunit.electric.mobility

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric mobility**, i.e. exactly three terms in the
 * canonical normal form `mass⁻¹ · time² · current¹` (`kg⁻¹·s²·A` = `m²/(V·s)`). The [value] is always
 * normalized internally to square meters per volt second ([KElectricMobilityUnit.BASE]), regardless of which
 * [KElectricMobilityUnit], SI [KUnitPrefix], or mass/time/current combination it was constructed from.
 *
 * Electric mobility is a *constructed* unit group: unlike a single-term wrapper it holds a three-term
 * instance. Instances are created via the bare tokens in `KElectricMobilityUnitBareValues.kt` (e.g.
 * `1400 of squareCentimetersPerVoltSecond`), the prefixed templates in
 * `KElectricMobilityUnitExtensions.kt`, the typed operator (`speed / electricFieldStrength`), or
 * [toElectricMobility] on a canonical `mass⁻¹·time²·current` expression.
 *
 * Example:
 * ```kotlin
 * val mu = 1400 of squareCentimetersPerVoltSecond // electron mobility in silicon
 * mu.value // 0.14 (normalized to m²/(V·s))
 * ```
 */
class KElectricMobilityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElectricMobilityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new electric mobility with [value] (square meters per volt second) scaled by [factor]. Backs
     * number-times-unit construction (`1400 of squareCentimetersPerVoltSecond`).
     */
    override fun scaledBy(factor: Double): KElectricMobilityUnitInstance =
        electricMobilityInstanceOf(value * factor)

    /**
     * Adds two electric mobilities, automatically converting between different [KElectricMobilityUnit]s since
     * both operands are always normalized to [KElectricMobilityUnit.BASE] internally.
     *
     * Example:
     * ```kotlin
     * ((1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)).value // 1.0001
     * ```
     */
    override operator fun plus(other: KElectricMobilityUnitInstance): KElectricMobilityUnitInstance =
        electricMobilityInstanceOf(value + other.value)

    /** Subtracts two electric mobilities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricMobilityUnitInstance): KElectricMobilityUnitInstance =
        electricMobilityInstanceOf(value - other.value)

    /**
     * Multiplies two electric mobilities, producing a new [KMixedUnitInstance] (no longer a "pure" electric
     * mobility).
     */
    operator fun times(other: KElectricMobilityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two electric mobilities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElectricMobilityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two electric mobilities by their normalized [value] (square meters per volt second). */
    override operator fun compareTo(other: KElectricMobilityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric mobilities are equal iff they represent the
     * same drift behaviour (e.g. `(1 of squareMetersPerVoltSecond) == (10000 of
     * squareCentimetersPerVoltSecond)`).
     */
    override fun equals(other: Any?): Boolean = other is KElectricMobilityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"0.14 m²/(V·s)"`. */
    override fun toString(): String = "${renderDouble(value)} ${KElectricMobilityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The square meter per volt second's SI definition uses the *kilogram* as its mass dimension
// (`1 m²/(V·s) = 1 kg⁻¹·s²·A`), whereas the mass group's base unit is the gram. The canonical normal form is
// therefore stored with the mass group's base term (gram), and this factor bridges a gram-based canonical
// product to the base unit. The mass exponent is *negative* (-1) here, so the bridge multiplies.
internal val SQUARE_METER_PER_VOLT_SECOND_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElectricMobilityUnitInstance] from a value already expressed in square meters per volt second
 * ([KElectricMobilityUnit.BASE]).
 *
 * This is the single creation source that every electric mobility decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `mass⁻¹`, `time²`, `current¹`
 * (each in its group's base unit). The distance dimension cancels out: `m²/(V·s)` reduces to
 * `kg⁻¹·s²·A` because the volt already carries `m²`.
 */
internal fun electricMobilityInstanceOf(squareMetersPerVoltSecond: Double): KElectricMobilityUnitInstance =
    KElectricMobilityUnitInstance(
        KMixedUnitInstance(
            squareMetersPerVoltSecond,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KTimeUnit.BASE, 2),
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" electric mobility, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `-1`, one [KTimeUnit] term at exponent `+2` and one
 * [KElectricCurrentUnit] term at exponent `+1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting mobility is expressed in square meters per
 * volt second regardless of which concrete mass/time/current units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·time²·current¹` electric
 * mobility.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of ((seconds pow 2) * amperes) / kilo.grams
 * raw.toElectricMobility().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toElectricMobility(): KElectricMobilityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    check(units.size == 3 && massTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure electric mobility (expected KMassUnit^-1, KTimeUnit^2 and KElectricCurrentUnit^1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue.pow(-1.0) *
            timeTerm.unit.baseValue.pow(2.0) *
            currentTerm.unit.baseValue
    return electricMobilityInstanceOf(gramBaseProduct * SQUARE_METER_PER_VOLT_SECOND_MASS_REFERENCE)
}
