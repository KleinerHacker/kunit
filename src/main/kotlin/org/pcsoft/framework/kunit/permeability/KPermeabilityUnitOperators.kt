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

package org.pcsoft.framework.kunit.permeability

import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.lengthOf
import org.pcsoft.framework.kunit.inductance.KInductanceUnitInstance
import org.pcsoft.framework.kunit.inductance.inductanceInstanceOf
import org.pcsoft.framework.kunit.magneticfieldstrength.KMagneticFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.magneticfieldstrength.magneticFieldStrengthInstanceOf
import org.pcsoft.framework.kunit.magneticfluxdensity.KMagneticFluxDensityUnitInstance
import org.pcsoft.framework.kunit.magneticfluxdensity.magneticFluxDensityInstanceOf

// Cross-unit operators around the permeability: the inductance decomposition (`μ = L · l / (N² · A)`, where
// the geometry factor is a length) and the field decomposition (`μ = B / H`). They live in the permeability
// package because the group depends on inductance, distance, magnetic flux density and magnetic field
// strength (never the other way round), so placing them here avoids a dependency cycle. Every typed
// decomposition funnels into a group factory (`permeabilityInstanceOf` / `inductanceInstanceOf` / `lengthOf`
// / `magneticFluxDensityInstanceOf` / `magneticFieldStrengthInstanceOf`), keeping all decomposition paths
// value-equal.

// --- Decomposition A: inductance over the coil geometry ------------------------------------------

/**
 * Decomposition A - permeability from an inductance and the coil geometry: `inductance / length =
 * permeability` (`μ = L · l / (N² · A)`; the geometry factor - a core length over turns-squared times the
 * cross-section - is itself a length).
 *
 * @return the typed [KPermeabilityUnitInstance] (`this.value / length.value` H/m).
 */
operator fun KInductanceUnitInstance.div(length: KLengthUnitInstance): KPermeabilityUnitInstance =
    permeabilityInstanceOf(value / length.value)

/**
 * Solved for the inductance - `permeability * length = inductance` (`L = μ · N² · A / l`, multiplying the
 * geometry factor back in).
 *
 * @return the typed [KInductanceUnitInstance] (`this.value * length.value` henries).
 */
operator fun KPermeabilityUnitInstance.times(length: KLengthUnitInstance): KInductanceUnitInstance =
    inductanceInstanceOf(value * length.value)

/**
 * Commutative form of [KPermeabilityUnitInstance.times] - `length · permeability = inductance`.
 *
 * @return the typed [KInductanceUnitInstance] (`permeability.value * this.value` henries).
 */
operator fun KLengthUnitInstance.times(permeability: KPermeabilityUnitInstance): KInductanceUnitInstance =
    inductanceInstanceOf(permeability.value * value)

/**
 * Solved for the geometry factor - `inductance / permeability = length` (`N² · A / l = L / μ`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / permeability.value` meters).
 */
operator fun KInductanceUnitInstance.div(permeability: KPermeabilityUnitInstance): KLengthUnitInstance =
    lengthOf(value / permeability.value)

// --- Decomposition B: flux density over field strength -------------------------------------------

/**
 * Decomposition B - permeability as the ratio of magnetic flux density to field strength:
 * `magneticFluxDensity / magneticFieldStrength = permeability` (`μ = B / H`).
 *
 * @return the typed [KPermeabilityUnitInstance] (`this.value / fieldStrength.value` H/m).
 */
operator fun KMagneticFluxDensityUnitInstance.div(
    fieldStrength: KMagneticFieldStrengthUnitInstance,
): KPermeabilityUnitInstance = permeabilityInstanceOf(value / fieldStrength.value)

/**
 * Solved for the flux density - `permeability * magneticFieldStrength = magneticFluxDensity` (`B = μ · H`).
 *
 * @return the typed [KMagneticFluxDensityUnitInstance] (`this.value * fieldStrength.value` teslas).
 */
operator fun KPermeabilityUnitInstance.times(
    fieldStrength: KMagneticFieldStrengthUnitInstance,
): KMagneticFluxDensityUnitInstance = magneticFluxDensityInstanceOf(value * fieldStrength.value)

/**
 * Commutative form of [KPermeabilityUnitInstance.times] - `magneticFieldStrength · permeability =
 * magneticFluxDensity`.
 *
 * @return the typed [KMagneticFluxDensityUnitInstance] (`permeability.value * this.value` teslas).
 */
operator fun KMagneticFieldStrengthUnitInstance.times(
    permeability: KPermeabilityUnitInstance,
): KMagneticFluxDensityUnitInstance = magneticFluxDensityInstanceOf(permeability.value * value)

/**
 * Solved for the field strength - `magneticFluxDensity / permeability = magneticFieldStrength`
 * (`H = B / μ`).
 *
 * @return the typed [KMagneticFieldStrengthUnitInstance] (`this.value / permeability.value` A/m).
 */
operator fun KMagneticFluxDensityUnitInstance.div(
    permeability: KPermeabilityUnitInstance,
): KMagneticFieldStrengthUnitInstance = magneticFieldStrengthInstanceOf(value / permeability.value)
