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

package org.pcsoft.framework.kunit.thermo.insulance

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.thermalConductivityInstanceOf
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.KHeatFluxDensityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.heatFluxDensityInstanceOf
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.KHeatTransferCoefficientUnitInstance
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.heatTransferCoefficientInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the two decompositions of a thermal insulance -
// `temperatureDifference / heatFluxDensity` and `length / thermalConductivity` - plus their inverses and
// the reciprocal relation to the heat transfer coefficient (U-value).

// --- Decomposition 1: temperature difference per heat flux density -------------------------------

/**
 * Divides a temperature difference by a heat flux density to obtain a [KThermalInsulanceUnitInstance]
 * (`temperatureDifference / heatFluxDensity = R-value`).
 */
operator fun KTemperatureDifferenceUnitInstance.div(
    other: KHeatFluxDensityUnitInstance,
): KThermalInsulanceUnitInstance = thermalInsulanceInstanceOf(value / other.value)

/**
 * Multiplies a thermal insulance by a heat flux density to obtain the temperature difference it sustains
 * (`R-value * heatFluxDensity = temperatureDifference`).
 */
operator fun KThermalInsulanceUnitInstance.times(
    other: KHeatFluxDensityUnitInstance,
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value * other.value)

/**
 * Multiplies a heat flux density by a thermal insulance to obtain the temperature difference; the
 * commutative counterpart of [KThermalInsulanceUnitInstance.times].
 */
operator fun KHeatFluxDensityUnitInstance.times(
    other: KThermalInsulanceUnitInstance,
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value * other.value)

/**
 * Divides a temperature difference by a thermal insulance to obtain the heat flux density
 * (`temperatureDifference / R-value = heatFluxDensity`).
 */
operator fun KTemperatureDifferenceUnitInstance.div(
    other: KThermalInsulanceUnitInstance,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value / other.value)

// --- Decomposition 2: thickness per thermal conductivity -----------------------------------------

/**
 * Divides a thickness by a thermal conductivity to obtain a [KThermalInsulanceUnitInstance]
 * (`length / thermalConductivity = R-value`) - the way an insulation layer's R-value is computed. This is
 * the second, equivalent decomposition; it yields the same typed, value-equal result as
 * `temperatureDifference / heatFluxDensity`.
 */
operator fun KLengthUnitInstance.div(
    other: KThermalConductivityUnitInstance,
): KThermalInsulanceUnitInstance = thermalInsulanceInstanceOf(value / other.value)

/**
 * Multiplies a thermal insulance by a thermal conductivity to obtain the corresponding thickness
 * (`R-value * thermalConductivity = length`).
 */
operator fun KThermalInsulanceUnitInstance.times(
    other: KThermalConductivityUnitInstance,
): KLengthUnitInstance = lengthOf(value * other.value)

/**
 * Multiplies a thermal conductivity by a thermal insulance to obtain the thickness; the commutative
 * counterpart of [KThermalInsulanceUnitInstance.times].
 */
operator fun KThermalConductivityUnitInstance.times(
    other: KThermalInsulanceUnitInstance,
): KLengthUnitInstance = lengthOf(value * other.value)

/**
 * Divides a thickness by a thermal insulance to obtain the material's thermal conductivity
 * (`length / R-value = thermalConductivity`).
 */
operator fun KLengthUnitInstance.div(
    other: KThermalInsulanceUnitInstance,
): KThermalConductivityUnitInstance = thermalConductivityInstanceOf(value / other.value)

// --- Reciprocal relation to the heat transfer coefficient (U-value) ------------------------------

/**
 * Inverts a heat transfer coefficient into the corresponding thermal insulance (`R = 1 / U`), returning a
 * **typed** [KThermalInsulanceUnitInstance]. This narrower overload wins over the group-agnostic
 * `Number.div(KUnitMeasurable)`, which would only produce a raw mixed unit.
 *
 * Example:
 * ```kotlin
 * val r = 1 / (1.3 of wattsPerSquareMeterKelvin) // ≈ 0.769 m²·K/W
 * ```
 */
operator fun Number.div(unit: KHeatTransferCoefficientUnitInstance): KThermalInsulanceUnitInstance =
    thermalInsulanceInstanceOf(this.toDouble() / unit.value)

/**
 * Inverts a thermal insulance into the corresponding heat transfer coefficient (`U = 1 / R`), returning a
 * **typed** [KHeatTransferCoefficientUnitInstance] - the counterpart of the [Number.div] above.
 *
 * Example:
 * ```kotlin
 * val u = 1 / (5 of squareMeterKelvinPerWatt) // 0.2 W/(m²·K)
 * ```
 */
operator fun Number.div(unit: KThermalInsulanceUnitInstance): KHeatTransferCoefficientUnitInstance =
    heatTransferCoefficientInstanceOf(this.toDouble() / unit.value)
