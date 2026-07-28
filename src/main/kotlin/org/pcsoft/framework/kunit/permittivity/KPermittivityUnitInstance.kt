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

package org.pcsoft.framework.kunit.permittivity

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
 * Wraps a [KMixedUnitInstance] representing a **permittivity**, i.e. exactly four terms in the canonical
 * normal form `mass⁻¹ · distance⁻³ · time⁴ · current²` (`kg⁻¹·m⁻³·s⁴·A²` = `F/m`). The [value] is always
 * normalized internally to farads per meter ([KPermittivityUnit.BASE]), regardless of which
 * [KPermittivityUnit], SI [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Permittivity is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KPermittivityUnitBareValues.kt` (e.g. `5 of faradsPerMeter`),
 * the prefixed templates in `KPermittivityUnitExtensions.kt` (e.g. `2 of pico.faradsPerMeter`), the typed
 * operators (`capacitance / length`, `electricFluxDensity / electricFieldStrength`), or [toPermittivity] on a
 * canonical `mass⁻¹·length⁻³·time⁴·current²` expression.
 *
 * Example:
 * ```kotlin
 * val eps = KPermittivityUnit.VACUUM_PERMITTIVITY of faradsPerMeter
 * eps.value // 8.8541878188e-12
 * ```
 */
class KPermittivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KPermittivityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new permittivity with [value] (farads per meter) scaled by [factor]. Backs number-times-unit
     * construction (`10 of pico.faradsPerMeter`).
     */
    override fun scaledBy(factor: Double): KPermittivityUnitInstance = permittivityInstanceOf(value * factor)

    /**
     * Adds two permittivities, automatically converting between different [KPermittivityUnit]s since both
     * operands are always normalized to [KPermittivityUnit.BASE] (farads per meter) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of faradsPerMeter) + (1 of faradsPerCentimeter)).value // 101.0
     * ```
     */
    override operator fun plus(other: KPermittivityUnitInstance): KPermittivityUnitInstance =
        permittivityInstanceOf(value + other.value)

    /** Subtracts two permittivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KPermittivityUnitInstance): KPermittivityUnitInstance =
        permittivityInstanceOf(value - other.value)

    /** Multiplies two permittivities, producing a new [KMixedUnitInstance] (no longer a "pure" permittivity). */
    operator fun times(other: KPermittivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two permittivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KPermittivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two permittivities by their normalized [value] (farads per meter). */
    override operator fun compareTo(other: KPermittivityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two permittivities are equal iff they represent the same
     * material constant (e.g. `(1 of faradsPerCentimeter) == (100 of faradsPerMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KPermittivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"8.8541878188E-12 F/m"`. */
    override fun toString(): String = "$value ${KPermittivityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The farad per meter's SI definition uses the *kilogram* as its mass dimension
// (`1 F/m = 1 kg⁻¹·m⁻³·s⁴·A²`), whereas the mass group's base unit is the gram. The canonical normal form is
// therefore stored with the mass group's base term (gram), and this factor bridges a gram-based canonical
// product to farads per meter. The mass exponent is *negative* (-1) here, so the bridge multiplies (like the
// farad).
internal val FARAD_PER_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KPermittivityUnitInstance] from a value already expressed in farads per meter
 * ([KPermittivityUnit.BASE]).
 *
 * This is the single creation source that every permittivity decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻³`, `time⁴`, `current²`
 * (each in its group's base unit).
 */
internal fun permittivityInstanceOf(faradsPerMeter: Double): KPermittivityUnitInstance =
    KPermittivityUnitInstance(
        KMixedUnitInstance(
            faradsPerMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -3),
                KUnitTerm(KTimeUnit.BASE, 4),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" permittivity, as long as it matches the canonical permittivity normal
 * form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-3`, one
 * [KTimeUnit] term at exponent `+4` and one [KElectricCurrentUnit] term at exponent `+2` (order independent).
 * The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting
 * permittivity is expressed in farads per meter regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance⁻³·time⁴·current²`
 * permittivity.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
 * raw.toPermittivity().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toPermittivity(): KPermittivityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -3 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 4 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure permittivity (expected KMassUnit^-1, KDistanceUnit^-3, KTimeUnit^4 and KElectricCurrentUnit^2)"
    }
    val gramBaseProduct = value *
        Math.pow(massTerm.unit.baseValue, -1.0) *
        Math.pow(distanceTerm.unit.baseValue, -3.0) *
        Math.pow(timeTerm.unit.baseValue, 4.0) *
        Math.pow(currentTerm.unit.baseValue, 2.0)
    return permittivityInstanceOf(gramBaseProduct * FARAD_PER_METER_MASS_REFERENCE)
}
