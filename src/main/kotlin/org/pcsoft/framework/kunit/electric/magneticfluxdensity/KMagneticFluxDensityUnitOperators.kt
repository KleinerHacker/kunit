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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.magneticFluxInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf

// Cross-unit operators between magnetic flux, area and magnetic flux density (`B = Φ / A`). They live in
// the magneticfluxdensity package because flux density depends on magnetic flux and distance/area (never
// the other way round), so placing them here avoids a dependency cycle. Every typed decomposition funnels
// into the group factories (`magneticFluxDensityInstanceOf` / `magneticFluxInstanceOf` / `areaOf`),
// keeping all decomposition paths value-equal.
//
// This set is additively extensible: further flux-density-adjacent decompositions can be added here later
// without touching the existing operators.

/**
 * Decomposition A - the definition of magnetic flux density: `flux / area = flux density` (`B = Φ / A`).
 *
 * @return the typed [KMagneticFluxDensityUnitInstance] (`this.value / area.value` teslas).
 */
operator fun KMagneticFluxUnitInstance.div(area: KAreaUnitInstance): KMagneticFluxDensityUnitInstance =
    magneticFluxDensityInstanceOf(value / area.value)

/**
 * Solved for the flux - `flux density · area = flux` (`Φ = B · A`).
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value * area.value` webers).
 */
operator fun KMagneticFluxDensityUnitInstance.times(area: KAreaUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value * area.value)

/**
 * Commutative form of [KMagneticFluxDensityUnitInstance.times] - `area · flux density = flux`.
 *
 * @return the typed [KMagneticFluxUnitInstance] (`fluxDensity.value * this.value` webers).
 */
operator fun KAreaUnitInstance.times(fluxDensity: KMagneticFluxDensityUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(fluxDensity.value * value)

/**
 * Solved for the area - `flux / flux density = area` (`A = Φ / B`).
 *
 * @return the typed [KAreaUnitInstance] (`this.value / fluxDensity.value` square meters).
 */
operator fun KMagneticFluxUnitInstance.div(fluxDensity: KMagneticFluxDensityUnitInstance): KAreaUnitInstance =
    areaOf(value / fluxDensity.value)
