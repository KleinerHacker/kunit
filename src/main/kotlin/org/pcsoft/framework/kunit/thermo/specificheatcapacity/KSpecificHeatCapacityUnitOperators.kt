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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.massOf
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.heatCapacityInstanceOf
import org.pcsoft.framework.kunit.thermo.specificenergy.KSpecificEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.specificenergy.specificEnergyInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the two decompositions of a specific heat capacity - `heatCapacity / mass`
// and `specificEnergy / temperatureDifference` - plus their inverses. They live in this package because it
// may depend on heat capacity / specific energy / mass / temperature (the reverse must never happen).

// --- Decomposition 1: heat capacity per mass -----------------------------------------------------

/**
 * Divides a heat capacity by a mass to obtain a [KSpecificHeatCapacityUnitInstance]
 * (`heatCapacity / mass = specific heat capacity`).
 *
 * Example:
 * ```kotlin
 * val c = (4184 of joulesPerKelvin) / (1 of kilo.grams) // 4184 J/(kg·K), water
 * ```
 */
operator fun KHeatCapacityUnitInstance.div(other: KMassUnitInstance): KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityInstanceOf(value / (other.value / SPECIFIC_HEAT_GRAMS_PER_KILOGRAM))

/**
 * Multiplies a specific heat capacity by a mass to obtain the object's heat capacity
 * (`specific heat capacity * mass = heatCapacity`).
 */
operator fun KSpecificHeatCapacityUnitInstance.times(other: KMassUnitInstance): KHeatCapacityUnitInstance =
    heatCapacityInstanceOf(value * (other.value / SPECIFIC_HEAT_GRAMS_PER_KILOGRAM))

/**
 * Multiplies a mass by a specific heat capacity to obtain the heat capacity; the commutative counterpart
 * of [KSpecificHeatCapacityUnitInstance.times].
 */
operator fun KMassUnitInstance.times(other: KSpecificHeatCapacityUnitInstance): KHeatCapacityUnitInstance =
    heatCapacityInstanceOf((value / SPECIFIC_HEAT_GRAMS_PER_KILOGRAM) * other.value)

/**
 * Divides a heat capacity by a specific heat capacity to obtain the mass
 * (`heatCapacity / specific heat capacity = mass`).
 */
operator fun KHeatCapacityUnitInstance.div(other: KSpecificHeatCapacityUnitInstance): KMassUnitInstance =
    massOf(value / other.value * SPECIFIC_HEAT_GRAMS_PER_KILOGRAM)

// --- Decomposition 2: specific energy per temperature difference ---------------------------------

/**
 * Divides a specific energy by a temperature difference to obtain a [KSpecificHeatCapacityUnitInstance]
 * (`specificEnergy / temperatureDifference = specific heat capacity`). This is the second, equivalent
 * decomposition; it yields the same typed, value-equal result as `heatCapacity / mass`.
 */
operator fun KSpecificEnergyUnitInstance.div(
    other: KTemperatureDifferenceUnitInstance,
): KSpecificHeatCapacityUnitInstance = specificHeatCapacityInstanceOf(value / other.value)

/**
 * Multiplies a specific heat capacity by a temperature difference to obtain the specific energy required
 * (`specific heat capacity * temperatureDifference = specificEnergy`).
 */
operator fun KSpecificHeatCapacityUnitInstance.times(
    other: KTemperatureDifferenceUnitInstance,
): KSpecificEnergyUnitInstance = specificEnergyInstanceOf(value * other.value)

/**
 * Multiplies a temperature difference by a specific heat capacity to obtain the specific energy; the
 * commutative counterpart of [KSpecificHeatCapacityUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(
    other: KSpecificHeatCapacityUnitInstance,
): KSpecificEnergyUnitInstance = specificEnergyInstanceOf(value * other.value)

/**
 * Divides a specific energy by a specific heat capacity to obtain the achievable temperature difference
 * (`specificEnergy / specific heat capacity = temperatureDifference`).
 */
operator fun KSpecificEnergyUnitInstance.div(
    other: KSpecificHeatCapacityUnitInstance,
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value / other.value)
