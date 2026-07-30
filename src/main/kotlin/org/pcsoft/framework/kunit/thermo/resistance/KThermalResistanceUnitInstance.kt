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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **thermal resistance** (the *R-value*), i.e. exactly three
 * terms in the canonical normal form `mass⁻¹ · time³ · temperature¹` (`kg⁻¹·s³·K` = `m²·K/W`). The [value]
 * is always normalized internally to m²·K/W ([KThermalResistanceUnit.BASE]).
 *
 * Thermal resistance is the exact reciprocal of the heat transfer coefficient (U-value); `1 / u` and
 * `1 / r` convert between the two with a typed result.
 *
 * The temperature dimension is the **difference** group ([KTemperatureDifferenceUnit]).
 *
 * Example:
 * ```kotlin
 * val batt = 5 of squareMeterKelvinPerWatt // a thick insulation batt
 * ```
 */
class KThermalResistanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KThermalResistanceUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new thermal resistance with [value] (m²·K/W) scaled by [factor]. */
    override fun scaledBy(factor: Double): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value * factor)

    /**
     * Adds two thermal resistances - the physically meaningful operation for layers in series, where the
     * R-values simply add up. Different [KThermalResistanceUnit]s are converted automatically.
     */
    override operator fun plus(other: KThermalResistanceUnitInstance): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value + other.value)

    /** Subtracts two thermal resistances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KThermalResistanceUnitInstance): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value - other.value)

    /** Multiplies two thermal resistances, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KThermalResistanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two thermal resistances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KThermalResistanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two thermal resistances by their normalized [value] (m²·K/W). */
    override operator fun compareTo(other: KThermalResistanceUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (m²·K/W). */
    override fun equals(other: Any?): Boolean = other is KThermalResistanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"5.0 m²·K/W"`. */
    override fun toString(): String = "$value ${KThermalResistanceUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// Bridges the gram-based canonical product to m²·K/W. Unlike the other thermodynamic groups the mass
// exponent here is *negative* (-1), so the bridge multiplies rather than divides:
// gramProduct / KILO^-1 == gramProduct * KILO.
internal val THERMAL_RESISTANCE_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KThermalResistanceUnitInstance] from a value already expressed in m²·K/W
 * ([KThermalResistanceUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the three terms `mass⁻¹`, `time³`, `temperature¹`.
 */
internal fun thermalResistanceInstanceOf(squareMeterKelvinPerWatt: Double): KThermalResistanceUnitInstance =
    KThermalResistanceUnitInstance(
        KMixedUnitInstance(
            squareMeterKelvinPerWatt,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KTimeUnit.BASE, 3),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, 1),
            ),
        ),
    )

/** Builds a value-1 [KThermalResistanceUnitInstance] for the given [unit]. */
internal fun thermalResistanceOfUnit(unit: KThermalResistanceUnit): KThermalResistanceUnitInstance =
    thermalResistanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" thermal resistance, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `-1`, one [KTimeUnit] term at `+3` and one
 * [KTemperatureDifferenceUnit] term at `+1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·time³·temperature¹` thermal
 * resistance.
 */
fun KMixedUnitInstance.toThermalResistance(): KThermalResistanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == 1 }
    check(units.size == 3 && massTerm != null && timeTerm != null && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure thermal resistance " +
            "(expected KMassUnit^-1, KTimeUnit^3 and KTemperatureDifferenceUnit^1)"
    }
    val gramBaseProduct = value *
        Math.pow(massTerm.unit.baseValue, -1.0) *
        Math.pow(timeTerm.unit.baseValue, 3.0) *
        temperatureTerm.unit.baseValue
    return thermalResistanceInstanceOf(gramBaseProduct * THERMAL_RESISTANCE_MASS_REFERENCE)
}
