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

package org.pcsoft.framework.kunit.thermo.diffusivity

import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.KGM3_IN_BASE
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.thermalConductivityInstanceOf
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.specificHeatCapacityInstanceOf

// The defining relation of thermal diffusivity is `α = λ / (ρ · c_p)` - a **ternary** relation. Unlike
// every other group here it cannot be expressed as a single binary operator without inventing an
// intermediate type for the volumetric heat capacity `ρ · c_p` (J/(m³·K)), which is not part of this
// library. The relation is therefore exposed as named, strongly typed functions rather than operators;
// they funnel into the same normalizing factory as everything else, and the equivalent native
// `distance²·time⁻¹` expression stays available through `toThermalDiffusivity()`.
//
// The density group stores its value as the raw component `g·m⁻³`, so every function below bridges to
// kg/m³ via KGM3_IN_BASE.

/**
 * The thermal diffusivity of a material from its conductivity, density and specific heat capacity
 * (`α = λ / (ρ · c_p)`).
 *
 * Example:
 * ```kotlin
 * val copper = (401 of wattsPerMeterKelvin)
 *     .diffusivityWith(8960 of (kilo.grams / (meters pow 3)), 385 of joulesPerKilogramKelvin)
 * copper into squareMillimetersPerSecond // ≈ 116.2
 * ```
 */
fun KThermalConductivityUnitInstance.diffusivityWith(
    density: KDensityUnitInstance,
    specificHeatCapacity: KSpecificHeatCapacityUnitInstance,
): KThermalDiffusivityUnitInstance =
    thermalDiffusivityInstanceOf(value / ((density.value / KGM3_IN_BASE) * specificHeatCapacity.value))

/**
 * The thermal conductivity implied by a diffusivity, density and specific heat capacity
 * (`λ = α · ρ · c_p`) - the inverse of [KThermalConductivityUnitInstance.diffusivityWith].
 */
fun KThermalDiffusivityUnitInstance.conductivityWith(
    density: KDensityUnitInstance,
    specificHeatCapacity: KSpecificHeatCapacityUnitInstance,
): KThermalConductivityUnitInstance =
    thermalConductivityInstanceOf(value * (density.value / KGM3_IN_BASE) * specificHeatCapacity.value)

/**
 * The density implied by a diffusivity, conductivity and specific heat capacity
 * (`ρ = λ / (α · c_p)`).
 */
fun KThermalDiffusivityUnitInstance.densityWith(
    conductivity: KThermalConductivityUnitInstance,
    specificHeatCapacity: KSpecificHeatCapacityUnitInstance,
): KDensityUnitInstance =
    densityUnitInstanceOf(conductivity.value / (value * specificHeatCapacity.value) * KGM3_IN_BASE)

/**
 * The specific heat capacity implied by a diffusivity, conductivity and density
 * (`c_p = λ / (α · ρ)`).
 */
fun KThermalDiffusivityUnitInstance.specificHeatCapacityWith(
    conductivity: KThermalConductivityUnitInstance,
    density: KDensityUnitInstance,
): KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityInstanceOf(conductivity.value / (value * (density.value / KGM3_IN_BASE)))
