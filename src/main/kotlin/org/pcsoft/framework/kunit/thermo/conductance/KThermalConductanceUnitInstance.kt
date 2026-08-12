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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **thermal conductance** (heat flow per temperature
 * difference), i.e. exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent
 * `+1`, [KDistanceUnit.BASE] (meter) at exponent `+2`, [KTimeUnit.BASE] (second) at exponent `-3` and
 * [KTemperatureDifferenceUnit.BASE] at exponent `-1` (`kg·m²·s⁻³·K⁻¹` = `W/K`).
 *
 * Thermal conductance is a *constructed* unit group with one decomposition, funnelling into
 * [thermalConductanceInstanceOf]:
 * * `power / temperatureDifference` (typed operator, see `KThermalConductanceUnitOperators.kt`)
 *
 * It is the exact reciprocal of the
 * [absolute thermal resistance][org.pcsoft.framework.kunit.thermo.resistance.KThermalResistanceUnitInstance],
 * which is why `1 / thermalResistance` yields a conductance and `1 / conductance` a resistance. Conductances
 * in **parallel** add up, which is what the same-type `+` does.
 *
 * Instances are additionally created via the bare tokens in `KThermalConductanceUnitBareValues.kt` (e.g.
 * `0.4 of wattsPerKelvin`), the prefixed templates in `KThermalConductanceUnitExtensions.kt`, or
 * [toThermalConductance].
 *
 * Example:
 * ```kotlin
 * val g = (12 of watts) / KTemperatureDifference.ofKelvin(30) // 0.4 W/K
 * g into wattsPerKelvin
 * ```
 */
class KThermalConductanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KThermalConductanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new thermal conductance with [value] (W/K) scaled by [factor]. Backs number-times-unit
     * construction (`0.4 of wattsPerKelvin`).
     */
    override fun scaledBy(factor: Double): KThermalConductanceUnitInstance =
        thermalConductanceInstanceOf(value * factor)

    /**
     * Adds two thermal conductances - the parallel connection of two heat paths. Automatically converts
     * between different [KThermalConductanceUnit]s since both operands are always normalized to
     * [KThermalConductanceUnit.BASE] (W/K) internally.
     */
    override operator fun plus(other: KThermalConductanceUnitInstance): KThermalConductanceUnitInstance =
        thermalConductanceInstanceOf(value + other.value)

    /** Subtracts two thermal conductances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KThermalConductanceUnitInstance): KThermalConductanceUnitInstance =
        thermalConductanceInstanceOf(value - other.value)

    /** Multiplies two thermal conductances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KThermalConductanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two thermal conductances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KThermalConductanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two thermal conductances by their normalized [value] (W/K). */
    override operator fun compareTo(other: KThermalConductanceUnitInstance): Int =
        value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two thermal conductances are equal iff they represent the
     * same quantity (e.g. `(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KThermalConductanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in W/K, e.g. `"0.4 W/K"`. */
    override fun toString(): String = "$value ${KThermalConductanceUnit.BASE.symbol}"
}

// The watt's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit is
// the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and this
// factor bridges a gram-based canonical product to watts per kelvin. The mass exponent is *positive* (+1)
// here, so the bridge divides (like the watt itself).
internal val WATT_PER_KELVIN_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KThermalConductanceUnitInstance] from a value already expressed in watts per kelvin
 * ([KThermalConductanceUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻³` and `temperature⁻¹`
 * (each in its group's base unit).
 */
internal fun thermalConductanceInstanceOf(wattsPerKelvin: Double): KThermalConductanceUnitInstance =
    KThermalConductanceUnitInstance(
        KMixedUnitInstance(
            wattsPerKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KThermalConductanceUnitInstance] for the given [unit] (its
 * [KThermalConductanceUnit.baseValue] W/K).
 */
internal fun thermalConductanceOfUnit(unit: KThermalConductanceUnit): KThermalConductanceUnitInstance =
    thermalConductanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" thermal conductance, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2`, one
 * [KTimeUnit] term at exponent `-3` and one [KTemperatureDifferenceUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s and
 * bridged from the gram-based product to the kilogram-based watt.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass·distance²·time⁻³·temperature⁻¹` thermal conductance.
 */
fun KMixedUnitInstance.toThermalConductance(): KThermalConductanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && temperatureTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure thermal conductance (expected one KMassUnit^1, " +
                "one KDistanceUnit^2, one KTimeUnit^-3 and one KTemperatureDifferenceUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, 2.0) *
            Math.pow(timeTerm.unit.baseValue, -3.0) *
            Math.pow(temperatureTerm.unit.baseValue, -1.0)
    return thermalConductanceInstanceOf(gramBaseProduct / WATT_PER_KELVIN_MASS_REFERENCE)
}
