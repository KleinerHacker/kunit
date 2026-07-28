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

package org.pcsoft.framework.kunit.electric.inductance

import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.electricCurrentOf
import org.pcsoft.framework.kunit.kinematic.frequency.KFrequencyUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.magneticFluxInstanceOf
import org.pcsoft.framework.kunit.electric.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.electric.resistance.resistanceInstanceOf

// Cross-unit operators between magnetic flux, electric current, resistance, frequency and inductance
// (`L = Φ / I`, reactance form `L = X / ω`). They live in the inductance package because inductance depends
// on magnetic flux, electric current, resistance and frequency (never the other way round), so placing them
// here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`inductanceInstanceOf` / `magneticFluxInstanceOf` / `electricCurrentOf` / `resistanceInstanceOf`),
// keeping all decomposition paths value-equal.
//
// This set is additively extensible: further inductance-adjacent decompositions (e.g. energy / current² =
// inductance) can be added here later without touching the existing operators.

/**
 * Decomposition A - the definition of inductance: `flux / current = inductance` (`L = Φ / I`).
 *
 * @return the typed [KInductanceUnitInstance] (`this.value / current.value` henries).
 */
operator fun KMagneticFluxUnitInstance.div(current: KElectricCurrentUnitInstance): KInductanceUnitInstance =
    inductanceInstanceOf(value / current.value)

/**
 * Inverse of the definition - `inductance · current = flux` (`Φ = L · I`).
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value * current.value` webers).
 */
operator fun KInductanceUnitInstance.times(current: KElectricCurrentUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value * current.value)

/**
 * Commutative form of [times] - `current · inductance = flux`.
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value * inductance.value` webers).
 */
operator fun KElectricCurrentUnitInstance.times(inductance: KInductanceUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value * inductance.value)

/**
 * Solved for current - `flux / inductance = current` (`I = Φ / L`).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value / inductance.value` amperes).
 */
operator fun KMagneticFluxUnitInstance.div(inductance: KInductanceUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value / inductance.value)

/**
 * Decomposition B - the reactance form: `resistance / frequency = inductance` (`L = X / ω`, since
 * `Ω/Hz = Ω·s = H`).
 *
 * @return the typed [KInductanceUnitInstance] (`this.value / frequency.value` henries).
 */
operator fun KResistanceUnitInstance.div(frequency: KFrequencyUnitInstance): KInductanceUnitInstance =
    inductanceInstanceOf(value / frequency.value)

/**
 * Inverse of the reactance form - `inductance · frequency = resistance` (`X = ω · L`).
 *
 * @return the typed [KResistanceUnitInstance] (`this.value * frequency.value` ohms).
 */
operator fun KInductanceUnitInstance.times(frequency: KFrequencyUnitInstance): KResistanceUnitInstance =
    resistanceInstanceOf(value * frequency.value)
