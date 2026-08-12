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

package org.pcsoft.framework.kunit.electric.linearchargedensity

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf

// Cross-unit operators between charge, length and linear charge density (`λ = Q / l`). They live in the
// linearChargeDensity package because the group depends on charge and distance (never the other way round),
// so placing them here avoids a dependency cycle. Every typed decomposition funnels into a group factory
// (`linearChargeDensityInstanceOf` / `chargeInstanceOf` / `lengthOf`), keeping all decomposition paths
// value-equal.

/**
 * Decomposition A - linear charge density from a charge spread along a length: `charge / length =
 * linearChargeDensity` (`λ = Q / l`).
 *
 * @return the typed [KLinearChargeDensityUnitInstance] (`this.value / length.value` C/m).
 */
operator fun KChargeUnitInstance.div(length: KLengthUnitInstance): KLinearChargeDensityUnitInstance =
    linearChargeDensityInstanceOf(value / length.value)

/**
 * Solved for the charge - `linearChargeDensity * length = charge` (`Q = λ · l`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value * length.value` coulombs).
 */
operator fun KLinearChargeDensityUnitInstance.times(length: KLengthUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * length.value)

/**
 * Commutative form of [KLinearChargeDensityUnitInstance.times] - `length · linearChargeDensity = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`density.value * this.value` coulombs).
 */
operator fun KLengthUnitInstance.times(density: KLinearChargeDensityUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(density.value * value)

/**
 * Solved for the length - `charge / linearChargeDensity = length` (`l = Q / λ`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / density.value` meters).
 */
operator fun KChargeUnitInstance.div(density: KLinearChargeDensityUnitInstance): KLengthUnitInstance =
    lengthOf(value / density.value)
