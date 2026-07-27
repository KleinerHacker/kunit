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

package org.pcsoft.framework.kunit.conductance

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
 * Wraps a [KMixedUnitInstance] representing an **electrical conductance**, i.e. exactly four terms in the
 * canonical normal form `mass⁻¹ · distance⁻² · time³ · current²` (`kg⁻¹·m⁻²·s³·A²`). The [value] is always
 * normalized internally to siemens ([KConductanceUnit.BASE]), regardless of which [KConductanceUnit], SI
 * [org.pcsoft.framework.kunit.KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Conductance is a *constructed* unit group and the reciprocal of resistance
 * ([org.pcsoft.framework.kunit.resistance.KResistanceUnitInstance]): unlike a single-term wrapper it holds a
 * four-term instance. Instances are created via the bare tokens in `KConductanceUnitBareValues.kt` (e.g.
 * `5 of siemens`), the prefixed templates in `KConductanceUnitExtensions.kt` (e.g. `2 of milli.siemens`), or
 * [toConductance] on a canonical mass⁻¹·length⁻²·time³·current² expression.
 *
 * Example:
 * ```kotlin
 * val g = 2 of milli.siemens // 0.002 S
 * g.value                    // 0.002 (normalized to siemens)
 * g into siemens             // 0.002 (read back in siemens)
 * ```
 */
class KConductanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KConductanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new conductance value with [value] (siemens) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.siemens`).
     */
    override fun scaledBy(factor: Double): KConductanceUnitInstance = conductanceInstanceOf(value * factor)

    /**
     * Adds two conductances, automatically converting between different [KConductanceUnit]s since both
     * operands are always normalized to [KConductanceUnit.BASE] (siemens) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.siemens) + (500 of siemens)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KConductanceUnitInstance): KConductanceUnitInstance = conductanceInstanceOf(value + other.value)

    /** Subtracts two conductances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KConductanceUnitInstance): KConductanceUnitInstance = conductanceInstanceOf(value - other.value)

    /**
     * Multiplies two conductances, producing a new [KMixedUnitInstance] (no longer a "pure" conductance).
     */
    operator fun times(other: KConductanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two conductances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KConductanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two conductances by their normalized [value] (siemens). */
    override operator fun compareTo(other: KConductanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two conductances are equal iff they represent the same
     * conductance (e.g. `(1 of kilo.siemens) == (1000 of siemens)`).
     */
    override fun equals(other: Any?): Boolean = other is KConductanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 S"`. */
    override fun toString(): String = "$value ${KConductanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The siemens' SI definition uses the *kilogram* as its mass dimension (`1 S = 1 kg⁻¹·m⁻²·s³·A²`), whereas
// the mass group's base unit is the gram. The canonical normal form is therefore stored with the mass
// group's base term (gram), and this factor bridges a gram-based canonical product to siemens. Note that
// the mass exponent is `-1` here, so the bridge is applied as `pow(reference, -1.0)`.
private val SIEMENS_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KConductanceUnitInstance] from a value already expressed in siemens
 * ([KConductanceUnit.BASE]).
 *
 * This is the single creation source that every conductance decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻²`, `time³`,
 * `current²` (each in its group's base unit).
 */
internal fun conductanceInstanceOf(siemens: Double): KConductanceUnitInstance =
    KConductanceUnitInstance(
        KMixedUnitInstance(
            siemens,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 3),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" conductance value, as long as it matches the canonical conductance
 * normal form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-2`,
 * one [KTimeUnit] term at exponent `+3` and one [KElectricCurrentUnit] term at exponent `+2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting conductance is expressed in siemens regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance⁻²·time³·current²`
 * conductance.
 *
 * Example:
 * ```kotlin
 * val raw = 10 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
 * raw.toConductance().value // 10.0
 * ```
 */
fun KMixedUnitInstance.toConductance(): KConductanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure conductance (expected KMassUnit^-1, KDistanceUnit^-2, KTimeUnit^3 and KElectricCurrentUnit^2)"
    }
    val gramBaseProduct = value *
        Math.pow(massTerm.unit.baseValue, -1.0) *
        Math.pow(distanceTerm.unit.baseValue, -2.0) *
        Math.pow(timeTerm.unit.baseValue, 3.0) *
        Math.pow(currentTerm.unit.baseValue, 2.0)
    return conductanceInstanceOf(gramBaseProduct / Math.pow(SIEMENS_MASS_REFERENCE, -1.0))
}
