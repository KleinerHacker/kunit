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

package org.pcsoft.framework.kunit.electric.permeability

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **permeability**, i.e. exactly four terms in the canonical
 * normal form `mass¹ · distance¹ · time⁻² · current⁻²` (`kg·m·s⁻²·A⁻²` = `H/m`). The [value] is always
 * normalized internally to henries per meter ([KPermeabilityUnit.BASE]), regardless of which
 * [KPermeabilityUnit], SI [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Permeability is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KPermeabilityUnitBareValues.kt` (e.g. `5 of henriesPerMeter`),
 * the prefixed templates in `KPermeabilityUnitExtensions.kt` (e.g. `2 of micro.henriesPerMeter`), the typed
 * operators (`inductance / length`, `magneticFluxDensity / magneticFieldStrength`), or [toPermeability] on a
 * canonical `mass·length·time⁻²·current⁻²` expression.
 *
 * Example:
 * ```kotlin
 * val mu = KPermeabilityUnit.VACUUM_PERMEABILITY of henriesPerMeter
 * mu.value // 1.25663706127e-6
 * ```
 */
class KPermeabilityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KPermeabilityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new permeability with [value] (henries per meter) scaled by [factor]. Backs number-times-unit
     * construction (`10 of micro.henriesPerMeter`).
     */
    override fun scaledBy(factor: Double): KPermeabilityUnitInstance = permeabilityInstanceOf(value * factor)

    /**
     * Adds two permeabilities, automatically converting between different [KPermeabilityUnit]s since both
     * operands are always normalized to [KPermeabilityUnit.BASE] (henries per meter) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of henriesPerMeter) + (1 of henriesPerCentimeter)).value // 101.0
     * ```
     */
    override operator fun plus(other: KPermeabilityUnitInstance): KPermeabilityUnitInstance =
        permeabilityInstanceOf(value + other.value)

    /** Subtracts two permeabilities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KPermeabilityUnitInstance): KPermeabilityUnitInstance =
        permeabilityInstanceOf(value - other.value)

    /** Multiplies two permeabilities, producing a new [KMixedUnitInstance] (no longer a "pure" permeability). */
    operator fun times(other: KPermeabilityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two permeabilities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KPermeabilityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two permeabilities by their normalized [value] (henries per meter). */
    override operator fun compareTo(other: KPermeabilityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two permeabilities are equal iff they represent the same
     * material constant (e.g. `(1 of henriesPerCentimeter) == (100 of henriesPerMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KPermeabilityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1.25663706127E-6 H/m"`. */
    override fun toString(): String = "$value ${KPermeabilityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The henry per meter's SI definition uses the *kilogram* as its mass dimension
// (`1 H/m = 1 kg·m·s⁻²·A⁻²`), whereas the mass group's base unit is the gram. The canonical normal form is
// therefore stored with the mass group's base term (gram), and this factor bridges a gram-based canonical
// product to henries per meter. The mass exponent is *positive* (+1) here, so the bridge divides (like the
// henry).
internal val HENRY_PER_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KPermeabilityUnitInstance] from a value already expressed in henries per meter
 * ([KPermeabilityUnit.BASE]).
 *
 * This is the single creation source that every permeability decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance¹`, `time⁻²`, `current⁻²`
 * (each in its group's base unit).
 */
internal fun permeabilityInstanceOf(henriesPerMeter: Double): KPermeabilityUnitInstance =
    KPermeabilityUnitInstance(
        KMixedUnitInstance(
            henriesPerMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KElectricCurrentUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" permeability, as long as it matches the canonical permeability normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+1`, one
 * [KTimeUnit] term at exponent `-2` and one [KElectricCurrentUnit] term at exponent `-2` (order independent).
 * The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting
 * permeability is expressed in henries per meter regardless of which concrete mass/length/time/current units
 * the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance¹·time⁻²·current⁻²`
 * permeability.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (kilo.grams * meters) / ((seconds pow 2) * (amperes pow 2))
 * raw.toPermeability().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toPermeability(): KPermeabilityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -2 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure permeability (expected KMassUnit^1, KDistanceUnit^1, KTimeUnit^-2 and KElectricCurrentUnit^-2)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue *
            Math.pow(timeTerm.unit.baseValue, -2.0) *
            Math.pow(currentTerm.unit.baseValue, -2.0)
    return permeabilityInstanceOf(gramBaseProduct / HENRY_PER_METER_MASS_REFERENCE)
}
