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

package org.pcsoft.framework.kunit.electric.conductivity

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
 * Wraps a [KMixedUnitInstance] representing an **electrical conductivity**, i.e. exactly four terms in the
 * canonical normal form `mass⁻¹ · distance⁻³ · time³ · current²` (`kg⁻¹·m⁻³·s³·A²`). The [value] is always
 * normalized internally to siemens per meter ([KConductivityUnit.BASE]), regardless of which
 * [KConductivityUnit], SI [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Conductivity is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KConductivityUnitBareValues.kt` (e.g. `5 of siemensPerMeter`),
 * the prefixed templates in `KConductivityUnitExtensions.kt` (e.g. `58 of mega.siemensPerMeter`), or
 * [toConductivity] on a canonical `mass⁻¹·length⁻³·time³·current²` expression.
 *
 * Example:
 * ```kotlin
 * val sigma = 58 of mega.siemensPerMeter   // copper, 5.8e7 S/m
 * sigma.value                              // 5.8e7 (normalized to S/m)
 * sigma into siemensPerMeter               // 5.8e7 (read back in S/m)
 * ```
 */
class KConductivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KConductivityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new conductivity value with [value] (S/m) scaled by [factor]. Backs number-times-unit
     * construction (`58 of mega.siemensPerMeter`).
     */
    override fun scaledBy(factor: Double): KConductivityUnitInstance = conductivityInstanceOf(value * factor)

    /**
     * Adds two conductivities, automatically converting between different [KConductivityUnit]s since both
     * operands are always normalized to [KConductivityUnit.BASE] (S/m) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of siemensPerCentimeter) + (100 of siemensPerMeter)).value // 200.0
     * ```
     */
    override operator fun plus(other: KConductivityUnitInstance): KConductivityUnitInstance =
        conductivityInstanceOf(value + other.value)

    /** Subtracts two conductivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KConductivityUnitInstance): KConductivityUnitInstance =
        conductivityInstanceOf(value - other.value)

    /**
     * Multiplies two conductivities, producing a new [KMixedUnitInstance] (no longer a "pure" conductivity).
     */
    operator fun times(other: KConductivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two conductivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KConductivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two conductivities by their normalized [value] (S/m). */
    override operator fun compareTo(other: KConductivityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two conductivities are equal iff they represent the same
     * conductivity (e.g. `(1 of siemensPerCentimeter) == (100 of siemensPerMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KConductivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 S/m"`. */
    override fun toString(): String = "$value ${KConductivityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The siemens per meter's SI definition uses the *kilogram* as its mass dimension
// (`1 S/m = 1 kg⁻¹·m⁻³·s³·A²`), whereas the mass group's base unit is the gram. The canonical normal form is
// therefore stored with the mass group's base term (gram), and this factor bridges a gram-based canonical
// product to siemens per meter. Because the mass exponent is *negative* (-1) here, the bridge multiplies
// (`1 kg⁻¹ = 0.001 g⁻¹`) instead of dividing.
internal val SIEMENS_PER_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KConductivityUnitInstance] from a value already expressed in siemens per meter
 * ([KConductivityUnit.BASE]).
 *
 * This is the single creation source that every conductivity decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻³`, `time³`,
 * `current²` (each in its group's base unit).
 */
internal fun conductivityInstanceOf(siemensPerMeter: Double): KConductivityUnitInstance =
    KConductivityUnitInstance(
        KMixedUnitInstance(
            siemensPerMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -3),
                KUnitTerm(KTimeUnit.BASE, 3),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" conductivity value, as long as it matches the canonical conductivity
 * normal form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-3`, one
 * [KTimeUnit] term at exponent `+3` and one [KElectricCurrentUnit] term at exponent `+2` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting conductivity is expressed in siemens per meter regardless of which concrete
 * mass/length/time/current units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance⁻³·time³·current²`
 * conductivity.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
 * raw.toConductivity().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toConductivity(): KConductivityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -3 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure conductivity (expected KMassUnit^-1, KDistanceUnit^-3, KTimeUnit^3 and KElectricCurrentUnit^2)"
    }
    val gramBaseProduct = value *
        Math.pow(massTerm.unit.baseValue, -1.0) *
        Math.pow(distanceTerm.unit.baseValue, -3.0) *
        Math.pow(timeTerm.unit.baseValue, 3.0) *
        Math.pow(currentTerm.unit.baseValue, 2.0)
    return conductivityInstanceOf(gramBaseProduct * SIEMENS_PER_METER_MASS_REFERENCE)
}
