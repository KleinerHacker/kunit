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

import org.pcsoft.framework.kunit.thermo.heatfluxdensity.KHeatFluxDensityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.heatFluxDensityInstanceOf
import org.pcsoft.framework.kunit.thermo.temperaturegradient.KTemperatureGradientUnitInstance
import org.pcsoft.framework.kunit.thermo.temperaturegradient.temperatureGradientInstanceOf

// Fourier's law as typed operators: heat flux density = thermal conductivity × temperature gradient.
// All operand values are already normalized (W/m², K/m, W/(m·K)), so the arithmetic is exact.

/**
 * Divides a heat flux density by a temperature gradient to obtain a [KThermalConductivityUnitInstance]
 * (`heatFluxDensity / temperatureGradient = thermal conductivity`) - Fourier's law solved for the
 * material property.
 *
 * Example:
 * ```kotlin
 * val k = (40 of wattsPerSquareMeter) / (1 of kelvinPerMeter) // 40 W/(m·K)
 * ```
 */
operator fun KHeatFluxDensityUnitInstance.div(
    other: KTemperatureGradientUnitInstance,
): KThermalConductivityUnitInstance = thermalConductivityInstanceOf(value / other.value)

/**
 * Multiplies a thermal conductivity by a temperature gradient to obtain the heat flux density
 * (`thermal conductivity * temperatureGradient = heatFluxDensity`) - Fourier's law.
 */
operator fun KThermalConductivityUnitInstance.times(
    other: KTemperatureGradientUnitInstance,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value * other.value)

/**
 * Multiplies a temperature gradient by a thermal conductivity to obtain the heat flux density; the
 * commutative counterpart of [KThermalConductivityUnitInstance.times].
 */
operator fun KTemperatureGradientUnitInstance.times(
    other: KThermalConductivityUnitInstance,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value * other.value)

/**
 * Divides a heat flux density by a thermal conductivity to obtain the temperature gradient it implies
 * (`heatFluxDensity / thermal conductivity = temperatureGradient`).
 */
operator fun KHeatFluxDensityUnitInstance.div(
    other: KThermalConductivityUnitInstance,
): KTemperatureGradientUnitInstance = temperatureGradientInstanceOf(value / other.value)
