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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.heatCapacityInstanceOf
import org.pcsoft.framework.kunit.thermo.molarenergy.KMolarEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.molarenergy.molarEnergyInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the two decompositions of a molar heat capacity -
// `heatCapacity / amountOfSubstance` and `molarEnergy / temperatureDifference` - plus their inverses.
// All operand values are already normalized (J/K, mol, J/mol, K), so the arithmetic is exact.

// --- Decomposition 1: heat capacity per amount of substance --------------------------------------

/**
 * Divides a heat capacity by an amount of substance to obtain a [KMolarHeatCapacityUnitInstance]
 * (`heatCapacity / amountOfSubstance = molar heat capacity`).
 */
operator fun KHeatCapacityUnitInstance.div(
    other: KAmountOfSubstanceUnitInstance,
): KMolarHeatCapacityUnitInstance = molarHeatCapacityInstanceOf(value / other.value)

/**
 * Multiplies a molar heat capacity by an amount of substance to obtain the sample's heat capacity
 * (`molar heat capacity * amountOfSubstance = heatCapacity`).
 */
operator fun KMolarHeatCapacityUnitInstance.times(
    other: KAmountOfSubstanceUnitInstance,
): KHeatCapacityUnitInstance = heatCapacityInstanceOf(value * other.value)

/**
 * Multiplies an amount of substance by a molar heat capacity to obtain the heat capacity; the commutative
 * counterpart of [KMolarHeatCapacityUnitInstance.times].
 */
operator fun KAmountOfSubstanceUnitInstance.times(
    other: KMolarHeatCapacityUnitInstance,
): KHeatCapacityUnitInstance = heatCapacityInstanceOf(value * other.value)

/**
 * Divides a heat capacity by a molar heat capacity to obtain the amount of substance
 * (`heatCapacity / molar heat capacity = amountOfSubstance`).
 */
operator fun KHeatCapacityUnitInstance.div(
    other: KMolarHeatCapacityUnitInstance,
): KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(value / other.value)

// --- Decomposition 2: molar energy per temperature difference ------------------------------------

/**
 * Divides a molar energy by a temperature difference to obtain a [KMolarHeatCapacityUnitInstance]
 * (`molarEnergy / temperatureDifference = molar heat capacity`). This is the second, equivalent
 * decomposition; it yields the same typed, value-equal result as `heatCapacity / amountOfSubstance`.
 */
operator fun KMolarEnergyUnitInstance.div(
    other: KTemperatureDifferenceUnitInstance,
): KMolarHeatCapacityUnitInstance = molarHeatCapacityInstanceOf(value / other.value)

/**
 * Multiplies a molar heat capacity by a temperature difference to obtain the molar energy required
 * (`molar heat capacity * temperatureDifference = molarEnergy`).
 */
operator fun KMolarHeatCapacityUnitInstance.times(
    other: KTemperatureDifferenceUnitInstance,
): KMolarEnergyUnitInstance = molarEnergyInstanceOf(value * other.value)

/**
 * Multiplies a temperature difference by a molar heat capacity to obtain the molar energy; the
 * commutative counterpart of [KMolarHeatCapacityUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(
    other: KMolarHeatCapacityUnitInstance,
): KMolarEnergyUnitInstance = molarEnergyInstanceOf(value * other.value)

/**
 * Divides a molar energy by a molar heat capacity to obtain the achievable temperature difference
 * (`molarEnergy / molar heat capacity = temperatureDifference`).
 */
operator fun KMolarEnergyUnitInstance.div(
    other: KMolarHeatCapacityUnitInstance,
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value / other.value)
