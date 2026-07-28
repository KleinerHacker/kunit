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

package org.pcsoft.framework.kunit.electric.chargedensity

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf

// Cross-group operators between charge, volume and charge density (`ρ = Q / V`). They live in the
// chargedensity package because charge density depends on charge and distance (never the other way round),
// so placing them here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`chargeDensityInstanceOf` / `chargeInstanceOf` / `volumeOf`), keeping all decomposition paths value-equal.

/**
 * Decomposition A - the definition of charge density: `charge / volume = charge density` (`ρ = Q / V`).
 *
 * @return the typed [KChargeDensityUnitInstance] (`this.value / volume.value` C/m³).
 */
operator fun KChargeUnitInstance.div(volume: KVolumeUnitInstance): KChargeDensityUnitInstance =
    chargeDensityInstanceOf(value / volume.value)

/**
 * Solved for charge - `charge density · volume = charge` (`Q = ρ · V`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value * volume.value` coulombs).
 */
operator fun KChargeDensityUnitInstance.times(volume: KVolumeUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * volume.value)

/**
 * Commutative form of [KChargeDensityUnitInstance.times] - `volume · charge density = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`this.value * chargeDensity.value` coulombs).
 */
operator fun KVolumeUnitInstance.times(chargeDensity: KChargeDensityUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * chargeDensity.value)

/**
 * Solved for volume - `charge / charge density = volume` (`V = Q / ρ`).
 *
 * @return the typed [KVolumeUnitInstance] (`this.value / chargeDensity.value` cubic meters).
 */
operator fun KChargeUnitInstance.div(chargeDensity: KChargeDensityUnitInstance): KVolumeUnitInstance =
    volumeOf(value / chargeDensity.value)
