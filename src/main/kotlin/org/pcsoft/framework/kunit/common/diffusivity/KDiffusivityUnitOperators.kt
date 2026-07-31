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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.KGM3_IN_BASE
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.mechanic.viscosity.KViscosityUnitInstance
import org.pcsoft.framework.kunit.mechanic.viscosity.viscosityInstanceOf
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.thermalConductivityInstanceOf
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.specificHeatCapacityInstanceOf

// This group carries two field-specific readings of the same quantity (see the `diffusivity` page in
// thermodynamics and the `kinematicviscosity` page in mechanics):
//
// * the **kinematic viscosity** `ν = η / ρ` - a plain binary relation, hence the typed operators at the
//   bottom of this file, and
// * the **thermal diffusivity** `α = λ / (ρ · c_p)` - a ternary relation, hence the named functions below.
//
// The defining relation of thermal diffusivity is `α = λ / (ρ · c_p)` - a **ternary** relation. Unlike
// every other group here it cannot be expressed as a single binary operator without inventing an
// intermediate type for the volumetric heat capacity `ρ · c_p` (J/(m³·K)), which is not part of this
// library. The relation is therefore exposed as named, strongly typed functions rather than operators;
// they funnel into the same normalizing factory as everything else, and the equivalent native
// `distance²·time⁻¹` expression stays available through `toDiffusivity()`.
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
): KDiffusivityUnitInstance =
    diffusivityInstanceOf(value / ((density.value / KGM3_IN_BASE) * specificHeatCapacity.value))

/**
 * The thermal conductivity implied by a diffusivity, density and specific heat capacity
 * (`λ = α · ρ · c_p`) - the inverse of [KThermalConductivityUnitInstance.diffusivityWith].
 */
fun KDiffusivityUnitInstance.conductivityWith(
    density: KDensityUnitInstance,
    specificHeatCapacity: KSpecificHeatCapacityUnitInstance,
): KThermalConductivityUnitInstance =
    thermalConductivityInstanceOf(value * (density.value / KGM3_IN_BASE) * specificHeatCapacity.value)

/**
 * The density implied by a diffusivity, conductivity and specific heat capacity
 * (`ρ = λ / (α · c_p)`).
 */
fun KDiffusivityUnitInstance.densityWith(
    conductivity: KThermalConductivityUnitInstance,
    specificHeatCapacity: KSpecificHeatCapacityUnitInstance,
): KDensityUnitInstance =
    densityUnitInstanceOf(conductivity.value / (value * specificHeatCapacity.value) * KGM3_IN_BASE)

/**
 * The specific heat capacity implied by a diffusivity, conductivity and density
 * (`c_p = λ / (α · ρ)`).
 */
fun KDiffusivityUnitInstance.specificHeatCapacityWith(
    conductivity: KThermalConductivityUnitInstance,
    density: KDensityUnitInstance,
): KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityInstanceOf(conductivity.value / (value * (density.value / KGM3_IN_BASE)))


// --- Kinematic-viscosity decomposition (ν = η / ρ) -----------------------------------------------

// Both the viscosity and the density group store gram-based raw components (`g·m⁻¹·s⁻¹` and `g·m⁻³`), so
// their quotient is already the canonical m²/s value of this group - no bridging factor is needed.

/**
 * Divides a dynamic viscosity by a density to obtain the **kinematic viscosity**
 * (`viscosity / density = diffusivity`, `ν = η / ρ`).
 *
 * Example:
 * ```kotlin
 * val nu = (1 of milli.pascalSeconds) / water // 1e-6 m²/s = 1 cSt (water: 1000 kg/m³)
 * nu into centistokes                                                    // 1.0
 * ```
 */
operator fun KViscosityUnitInstance.div(density: KDensityUnitInstance): KDiffusivityUnitInstance =
    diffusivityInstanceOf(value / density.value)

/**
 * Multiplies a kinematic viscosity by a density to obtain the dynamic viscosity
 * (`diffusivity * density = viscosity`, `η = ν · ρ`).
 */
operator fun KDiffusivityUnitInstance.times(density: KDensityUnitInstance): KViscosityUnitInstance =
    viscosityInstanceOf(value * density.value)

/**
 * Multiplies a density by a kinematic viscosity to obtain the dynamic viscosity
 * (`density * diffusivity = viscosity`); the commutative counterpart of [KDiffusivityUnitInstance.times].
 */
operator fun KDensityUnitInstance.times(diffusivity: KDiffusivityUnitInstance): KViscosityUnitInstance =
    viscosityInstanceOf(value * diffusivity.value)

/**
 * Divides a dynamic viscosity by the kinematic viscosity to obtain the density
 * (`viscosity / diffusivity = density`, `ρ = η / ν`).
 */
operator fun KViscosityUnitInstance.div(diffusivity: KDiffusivityUnitInstance): KDensityUnitInstance =
    densityUnitInstanceOf(value / diffusivity.value)
