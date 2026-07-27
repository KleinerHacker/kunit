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

package org.pcsoft.framework.kunit.currentdensity

import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.areaOf
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.electricCurrentOf

// Cross-group operators between electric current, area and current density (`J = I / A`). They live in the
// currentdensity package because current density depends on electric current and distance (never the other
// way round), so placing them here avoids a dependency cycle. Every typed decomposition funnels into the
// group factory (`currentDensityInstanceOf` / `electricCurrentOf` / `areaOf`), keeping all decomposition
// paths value-equal.

/**
 * Decomposition A - the definition of current density: `current / area = current density` (`J = I / A`).
 *
 * @return the typed [KCurrentDensityUnitInstance] (`this.value / area.value` A/m²).
 */
operator fun KElectricCurrentUnitInstance.div(area: KAreaUnitInstance): KCurrentDensityUnitInstance =
    currentDensityInstanceOf(value / area.value)

/**
 * Solved for the current - `current density · area = current` (`I = J · A`).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * area.value` amperes).
 */
operator fun KCurrentDensityUnitInstance.times(area: KAreaUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * area.value)

/**
 * Commutative form of [KCurrentDensityUnitInstance.times] - `area · current density = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`currentDensity.value * this.value` amperes).
 */
operator fun KAreaUnitInstance.times(currentDensity: KCurrentDensityUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(currentDensity.value * value)

/**
 * Solved for the area - `current / current density = area` (`A = I / J`).
 *
 * @return the typed [KAreaUnitInstance] (`this.value / currentDensity.value` square meters).
 */
operator fun KElectricCurrentUnitInstance.div(currentDensity: KCurrentDensityUnitInstance): KAreaUnitInstance =
    areaOf(value / currentDensity.value)
