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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.thermalConductivityInstanceOf
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.KHeatFluxDensityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.heatFluxDensityInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the two decompositions of a heat transfer coefficient -
// `heatFluxDensity / temperatureDifference` and `thermalConductivity / length` - plus their inverses.

// --- Decomposition 1: heat flux density per temperature difference -------------------------------

/**
 * Divides a heat flux density by a temperature difference to obtain a
 * [KHeatTransferCoefficientUnitInstance] (`heatFluxDensity / temperatureDifference = U-value`).
 */
operator fun KHeatFluxDensityUnitInstance.div(
    other: KTemperatureDifferenceUnitInstance,
): KHeatTransferCoefficientUnitInstance = heatTransferCoefficientInstanceOf(value / other.value)

/**
 * Multiplies a heat transfer coefficient by a temperature difference to obtain the heat flux density
 * (`U-value * temperatureDifference = heatFluxDensity`).
 */
operator fun KHeatTransferCoefficientUnitInstance.times(
    other: KTemperatureDifferenceUnitInstance,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value * other.value)

/**
 * Multiplies a temperature difference by a heat transfer coefficient to obtain the heat flux density; the
 * commutative counterpart of [KHeatTransferCoefficientUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(
    other: KHeatTransferCoefficientUnitInstance,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value * other.value)

/**
 * Divides a heat flux density by a heat transfer coefficient to obtain the driving temperature difference
 * (`heatFluxDensity / U-value = temperatureDifference`).
 */
operator fun KHeatFluxDensityUnitInstance.div(
    other: KHeatTransferCoefficientUnitInstance,
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value / other.value)

// --- Decomposition 2: thermal conductivity per thickness -----------------------------------------

/**
 * Divides a thermal conductivity by a thickness to obtain a [KHeatTransferCoefficientUnitInstance]
 * (`thermalConductivity / length = U-value`). This is the second, equivalent decomposition; it yields the
 * same typed, value-equal result as `heatFluxDensity / temperatureDifference`.
 */
operator fun KThermalConductivityUnitInstance.div(
    other: KLengthUnitInstance,
): KHeatTransferCoefficientUnitInstance = heatTransferCoefficientInstanceOf(value / other.value)

/**
 * Multiplies a heat transfer coefficient by a thickness to obtain the material's thermal conductivity
 * (`U-value * length = thermalConductivity`).
 */
operator fun KHeatTransferCoefficientUnitInstance.times(
    other: KLengthUnitInstance,
): KThermalConductivityUnitInstance = thermalConductivityInstanceOf(value * other.value)

/**
 * Multiplies a thickness by a heat transfer coefficient to obtain the thermal conductivity; the
 * commutative counterpart of [KHeatTransferCoefficientUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(
    other: KHeatTransferCoefficientUnitInstance,
): KThermalConductivityUnitInstance = thermalConductivityInstanceOf(value * other.value)

/**
 * Divides a thermal conductivity by a heat transfer coefficient to obtain the corresponding thickness
 * (`thermalConductivity / U-value = length`).
 */
operator fun KThermalConductivityUnitInstance.div(
    other: KHeatTransferCoefficientUnitInstance,
): KLengthUnitInstance = lengthOf(value / other.value)
