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

package org.pcsoft.framework.kunit.electric.inductance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **inductance**, i.e. exactly four terms in the canonical
 * normal form `mass¹ · distance² · time⁻² · current⁻²` (`kg·m²·s⁻²·A⁻²`). The [value] is always normalized
 * internally to henries ([KInductanceUnit.BASE]), regardless of which [KInductanceUnit], SI
 * [org.pcsoft.framework.kunit.KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Inductance is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KInductanceUnitBareValues.kt` (e.g. `5 of henries`), the
 * prefixed templates in `KInductanceUnitExtensions.kt` (e.g. `2 of milli.henries`), or [toInductance] on a
 * canonical mass·length²·time⁻²·current⁻² expression.
 *
 * Example:
 * ```kotlin
 * val l = 2 of milli.henries   // 0.002 H
 * l.value                      // 0.002 (normalized to henries)
 * l into henries               // 0.002 (read back in henries)
 * ```
 */
class KInductanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KInductanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new inductance value with [value] (henries) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.henries`).
     */
    override fun scaledBy(factor: Double): KInductanceUnitInstance = inductanceInstanceOf(value * factor)

    /**
     * Adds two inductances, automatically converting between different [KInductanceUnit]s since both
     * operands are always normalized to [KInductanceUnit.BASE] (henries) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of henries) + (500 of milli.henries)).value // 1.5
     * ```
     */
    override operator fun plus(other: KInductanceUnitInstance): KInductanceUnitInstance =
        inductanceInstanceOf(value + other.value)

    /** Subtracts two inductances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KInductanceUnitInstance): KInductanceUnitInstance =
        inductanceInstanceOf(value - other.value)

    /** Multiplies two inductances, producing a new [KMixedUnitInstance] (no longer a "pure" inductance). */
    operator fun times(other: KInductanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two inductances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KInductanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two inductances by their normalized [value] (henries). */
    override operator fun compareTo(other: KInductanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two inductances are equal iff they represent the same
     * inductance (e.g. `(1 of henries) == (1000 of milli.henries)`).
     */
    override fun equals(other: Any?): Boolean = other is KInductanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 H"`. */
    override fun toString(): String = "$value ${KInductanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The henry's SI definition uses the *kilogram* as its mass dimension (`1 H = 1 kg·m²·s⁻²·A⁻²`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to henries.
private val HENRY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KInductanceUnitInstance] from a value already expressed in henries ([KInductanceUnit.BASE]).
 *
 * This is the single creation source that every inductance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻²`,
 * `current⁻²` (each in its group's base unit).
 */
internal fun inductanceInstanceOf(henries: Double): KInductanceUnitInstance =
    KInductanceUnitInstance(
        KMixedUnitInstance(
            henries,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KElectricCurrentUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" inductance value, as long as it matches the canonical inductance
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2`,
 * one [KTimeUnit] term at exponent `-2` and one [KElectricCurrentUnit] term at exponent `-2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting inductance is expressed in henries regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻²·current⁻²`
 * inductance.
 *
 * Example:
 * ```kotlin
 * val raw = (10 of kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
 * raw.toInductance().value // 10.0
 * ```
 */
fun KMixedUnitInstance.toInductance(): KInductanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure inductance (expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-2 and KElectricCurrentUnit^-2)"
    }
    val gramBaseProduct = value *
        massTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, 2.0) *
        Math.pow(timeTerm.unit.baseValue, -2.0) *
        Math.pow(currentTerm.unit.baseValue, -2.0)
    return inductanceInstanceOf(gramBaseProduct / HENRY_MASS_REFERENCE)
}
