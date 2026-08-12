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

package org.pcsoft.framework.kunit.electric.reluctance

import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.electricCurrentOf
import org.pcsoft.framework.kunit.electric.inductance.KInductanceUnitInstance
import org.pcsoft.framework.kunit.electric.inductance.inductanceInstanceOf
import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.magneticFluxInstanceOf

// Cross-unit operators around the magnetic reluctance: Hopkinson's law (`Rm = Θ / Φ`, the magnetic circuit's
// counterpart to Ohm's law) and the reciprocal pair with the permeance, which is measured in henries and
// therefore lives in the inductance group. They live in the reluctance package because the group depends on
// electric current, magnetic flux and inductance (never the other way round), so placing them here avoids a
// dependency cycle. Every typed decomposition funnels into a group factory (`reluctanceInstanceOf` /
// `electricCurrentOf` / `magneticFluxInstanceOf` / `inductanceInstanceOf`), keeping all decomposition paths
// value-equal.

// --- Hopkinson's law (Rm = Θ / Φ) -----------------------------------------------------------------

/**
 * Decomposition A - reluctance from the magnetomotive force over the resulting flux: `current / magneticFlux
 * = reluctance` (`Rm = Θ / Φ`; the magnetomotive force `Θ = N · I` is measured in amperes, respectively
 * ampere turns).
 *
 * @return the typed [KReluctanceUnitInstance] (`this.value / flux.value` A/Wb).
 */
operator fun KElectricCurrentUnitInstance.div(flux: KMagneticFluxUnitInstance): KReluctanceUnitInstance =
    reluctanceInstanceOf(value / flux.value)

/**
 * Solved for the magnetomotive force - `reluctance * magneticFlux = current` (`Θ = Rm · Φ`).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * flux.value` amperes).
 */
operator fun KReluctanceUnitInstance.times(flux: KMagneticFluxUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * flux.value)

/**
 * Commutative form of [KReluctanceUnitInstance.times] - `magneticFlux · reluctance = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`reluctance.value * this.value` amperes).
 */
operator fun KMagneticFluxUnitInstance.times(reluctance: KReluctanceUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(reluctance.value * value)

/**
 * Solved for the flux - `current / reluctance = magneticFlux` (`Φ = Θ / Rm`).
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value / reluctance.value` webers).
 */
operator fun KElectricCurrentUnitInstance.div(reluctance: KReluctanceUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value / reluctance.value)

// --- Reciprocal pair with the permeance (Λ = 1 / Rm, measured in henries) -------------------------

/**
 * Decomposition B - the reciprocal of a permeance: `1 / inductance = reluctance` (`Rm = 1 / Λ`). The
 * permeance `Λ` of a magnetic circuit is measured in henries and therefore carried by the inductance group.
 *
 * The numerator is a plain [Number] (typically `1`), mirroring the conductance/resistance reciprocal pair.
 *
 * @return the typed [KReluctanceUnitInstance] (`this / permeance.value` A/Wb).
 */
operator fun Number.div(permeance: KInductanceUnitInstance): KReluctanceUnitInstance =
    reluctanceInstanceOf(this.toDouble() / permeance.value)

/**
 * The mirrored reciprocal - `1 / reluctance = inductance` (`Λ = 1 / Rm`).
 *
 * @return the typed [KInductanceUnitInstance] (`this / reluctance.value` henries).
 */
operator fun Number.div(reluctance: KReluctanceUnitInstance): KInductanceUnitInstance =
    inductanceInstanceOf(this.toDouble() / reluctance.value)
