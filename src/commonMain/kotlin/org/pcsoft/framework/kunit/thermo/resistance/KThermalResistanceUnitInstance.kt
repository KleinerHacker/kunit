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

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **absolute thermal resistance** (temperature difference per
 * heat flow), i.e. exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent
 * `-1`, [KDistanceUnit.BASE] (meter) at exponent `-2`, [KTimeUnit.BASE] (second) at exponent `+3` and
 * [KTemperatureDifferenceUnit.BASE] at exponent `+1` (`kg⁻¹·m⁻²·s³·K` = `K/W`).
 *
 * Absolute thermal resistance is a *constructed* unit group with one decomposition, funnelling into
 * [thermalResistanceInstanceOf]:
 * * `temperatureDifference / power` (typed operator, see `KThermalResistanceUnitOperators.kt`)
 *
 * Resistances in series **add up**, which is exactly what the same-type `+` does - the standard way to sum
 * a junction-to-case, case-to-heatsink and heatsink-to-air chain.
 *
 * Instances are additionally created via the bare tokens in `KThermalResistanceUnitBareValues.kt` (e.g.
 * `2.5 of kelvinsPerWatt`), the prefixed templates in `KThermalResistanceUnitExtensions.kt`, or
 * [toThermalResistance].
 *
 * Example:
 * ```kotlin
 * val r = KTemperatureDifference.ofKelvin(30) / (12 of watts) // 2.5 K/W
 * r into kelvinsPerWatt
 * ```
 */
class KThermalResistanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KThermalResistanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new thermal resistance with [value] (K/W) scaled by [factor]. Backs number-times-unit
     * construction (`2.5 of kelvinsPerWatt`).
     */
    override fun scaledBy(factor: Double): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value * factor)

    /**
     * Adds two thermal resistances - the series connection of a thermal chain. Automatically converts
     * between different [KThermalResistanceUnit]s since both operands are always normalized to
     * [KThermalResistanceUnit.BASE] (K/W) internally.
     */
    override operator fun plus(other: KThermalResistanceUnitInstance): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value + other.value)

    /** Subtracts two thermal resistances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KThermalResistanceUnitInstance): KThermalResistanceUnitInstance =
        thermalResistanceInstanceOf(value - other.value)

    /** Multiplies two thermal resistances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KThermalResistanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two thermal resistances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KThermalResistanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two thermal resistances by their normalized [value] (K/W). */
    override operator fun compareTo(other: KThermalResistanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two thermal resistances are equal iff they represent the
     * same quantity (e.g. `(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KThermalResistanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in K/W, e.g. `"2.5 K/W"`. */
    override fun toString(): String = "${renderDouble(value)} ${KThermalResistanceUnit.BASE.symbol}"
}

// The watt's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit is
// the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and this
// factor bridges a gram-based canonical product to kelvins per watt. The mass exponent is *negative* (-1)
// here, so the bridge is applied as `pow(reference, -1.0)` (like the siemens).
internal val KELVIN_PER_WATT_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KThermalResistanceUnitInstance] from a value already expressed in kelvins per watt
 * ([KThermalResistanceUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `distance⁻²`, `time³` and `temperature¹`
 * (each in its group's base unit).
 */
internal fun thermalResistanceInstanceOf(kelvinsPerWatt: Double): KThermalResistanceUnitInstance =
    KThermalResistanceUnitInstance(
        KMixedUnitInstance(
            kelvinsPerWatt,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 3),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, 1),
            ),
        ),
    )

/**
 * Builds a value-1 [KThermalResistanceUnitInstance] for the given [unit] (its
 * [KThermalResistanceUnit.baseValue] K/W).
 */
internal fun thermalResistanceOfUnit(unit: KThermalResistanceUnit): KThermalResistanceUnitInstance =
    thermalResistanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" absolute thermal resistance, as long as it matches the canonical
 * normal form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-2`,
 * one [KTimeUnit] term at exponent `+3` and one [KTemperatureDifferenceUnit] term at exponent `+1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s and
 * bridged from the gram-based product to the kilogram-based watt.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass⁻¹·distance⁻²·time³·temperature` thermal resistance.
 */
fun KMixedUnitInstance.toThermalResistance(): KThermalResistanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == 1 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && temperatureTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure thermal resistance (expected one KMassUnit^-1, " +
                "one KDistanceUnit^-2, one KTimeUnit^3 and one KTemperatureDifferenceUnit^1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue.pow(-1.0) *
            distanceTerm.unit.baseValue.pow(-2.0) *
            timeTerm.unit.baseValue.pow(3.0) *
            temperatureTerm.unit.baseValue
    return thermalResistanceInstanceOf(gramBaseProduct / KELVIN_PER_WATT_MASS_REFERENCE.pow(-1.0))
}
