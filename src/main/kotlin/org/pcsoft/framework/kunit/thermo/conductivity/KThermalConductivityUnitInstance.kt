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

package org.pcsoft.framework.kunit.thermo.conductivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **thermal conductivity**, i.e. exactly four terms in the
 * canonical normal form `mass¹ · distance¹ · time⁻³ · temperature⁻¹` (`kg·m·s⁻³·K⁻¹` = `W/(m·K)`). The
 * [value] is always normalized internally to W/(m·K) ([KThermalConductivityUnit.BASE]).
 *
 * Thermal conductivity is the material property behind Fourier's law: the heat flux density equals the
 * conductivity times the temperature gradient. Divided by a thickness it becomes a heat transfer
 * coefficient; a thickness divided by it is the R-value.
 *
 * The temperature dimension is the **difference** group ([KTemperatureDifferenceUnit]).
 *
 * Example:
 * ```kotlin
 * val copper = 401 of wattsPerMeterKelvin
 * ```
 */
class KThermalConductivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KThermalConductivityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new thermal conductivity with [value] (W/(m·K)) scaled by [factor]. */
    override fun scaledBy(factor: Double): KThermalConductivityUnitInstance =
        thermalConductivityInstanceOf(value * factor)

    /**
     * Adds two thermal conductivities, automatically converting between different
     * [KThermalConductivityUnit]s since both operands are always normalized to W/(m·K) internally.
     */
    override operator fun plus(other: KThermalConductivityUnitInstance): KThermalConductivityUnitInstance =
        thermalConductivityInstanceOf(value + other.value)

    /** Subtracts two thermal conductivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KThermalConductivityUnitInstance): KThermalConductivityUnitInstance =
        thermalConductivityInstanceOf(value - other.value)

    /** Multiplies two thermal conductivities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KThermalConductivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two thermal conductivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KThermalConductivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two thermal conductivities by their normalized [value] (W/(m·K)). */
    override operator fun compareTo(other: KThermalConductivityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (W/(m·K)). */
    override fun equals(other: Any?): Boolean = other is KThermalConductivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"401.0 W/(m·K)"`. */
    override fun toString(): String = "$value ${KThermalConductivityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// Bridges the gram-based canonical product to W/(m·K); the mass exponent is +1, so the bridge divides.
internal val THERMAL_CONDUCTIVITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KThermalConductivityUnitInstance] from a value already expressed in W/(m·K)
 * ([KThermalConductivityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance¹`, `time⁻³`, `temperature⁻¹`.
 */
internal fun thermalConductivityInstanceOf(wattsPerMeterKelvin: Double): KThermalConductivityUnitInstance =
    KThermalConductivityUnitInstance(
        KMixedUnitInstance(
            wattsPerMeterKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KThermalConductivityUnitInstance] for the given [unit]. */
internal fun thermalConductivityOfUnit(unit: KThermalConductivityUnit): KThermalConductivityUnitInstance =
    thermalConductivityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" thermal conductivity, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at `+1`, one [KTimeUnit]
 * term at `-3` and one [KTemperatureDifferenceUnit] term at `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass¹·distance¹·time⁻³·temperature⁻¹` thermal conductivity.
 */
fun KMixedUnitInstance.toThermalConductivity(): KThermalConductivityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure thermal conductivity " +
                "(expected KMassUnit^1, KDistanceUnit^1, KTimeUnit^-3 and KTemperatureDifferenceUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue *
            Math.pow(timeTerm.unit.baseValue, -3.0) *
            Math.pow(temperatureTerm.unit.baseValue, -1.0)
    return thermalConductivityInstanceOf(gramBaseProduct / THERMAL_CONDUCTIVITY_MASS_REFERENCE)
}
