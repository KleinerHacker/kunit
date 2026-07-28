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

package org.pcsoft.framework.kunit.electric.fluxdensity

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf

// Cross-unit operators between charge, area and electric flux density (`D = Q / A`, equally the surface
// charge density `σ`). They live in the electricFluxDensity package because the group depends on charge and
// distance (never the other way round), so placing them here avoids a dependency cycle. Every typed
// decomposition funnels into a group factory (`electricFluxDensityInstanceOf` / `chargeInstanceOf` /
// `areaOf`), keeping all decomposition paths value-equal.
//
// This set is additively extensible: further density-adjacent decompositions (e.g. the permittivity pair
// with the electric field strength, see the permittivity package) can be added later without touching the
// existing operators.

/**
 * Decomposition A - electric flux density from a charge spread over an area: `charge / area =
 * electricFluxDensity` (`D = Q / A`).
 *
 * @return the typed [KElectricFluxDensityUnitInstance] (`this.value / area.value` C/m²).
 */
operator fun KChargeUnitInstance.div(area: KAreaUnitInstance): KElectricFluxDensityUnitInstance =
    electricFluxDensityInstanceOf(value / area.value)

/**
 * Solved for the charge - `electricFluxDensity * area = charge` (`Q = D · A`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value * area.value` coulombs).
 */
operator fun KElectricFluxDensityUnitInstance.times(area: KAreaUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * area.value)

/**
 * Commutative form of [KElectricFluxDensityUnitInstance.times] - `area · electricFluxDensity = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`density.value * this.value` coulombs).
 */
operator fun KAreaUnitInstance.times(density: KElectricFluxDensityUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(density.value * value)

/**
 * Solved for the area - `charge / electricFluxDensity = area` (`A = Q / D`).
 *
 * @return the typed [KAreaUnitInstance] (`this.value / density.value` square meters).
 */
operator fun KChargeUnitInstance.div(density: KElectricFluxDensityUnitInstance): KAreaUnitInstance =
    areaOf(value / density.value)
