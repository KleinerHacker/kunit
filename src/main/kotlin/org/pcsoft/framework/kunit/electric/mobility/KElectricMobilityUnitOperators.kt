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

package org.pcsoft.framework.kunit.electric.mobility

import org.pcsoft.framework.kunit.electric.fieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electric.fieldstrength.electricFieldStrengthInstanceOf
import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.speedUnitInstanceOf

// Cross-unit operators between drift speed, electric field strength and electric mobility (`v = μ · E`).
// They live in the electricMobility package because the group depends on speed and electric field strength
// (never the other way round), so placing them here avoids a dependency cycle. Every typed decomposition
// funnels into a group factory (`electricMobilityInstanceOf` / `speedUnitInstanceOf` /
// `electricFieldStrengthInstanceOf`), keeping all decomposition paths value-equal.

/**
 * Decomposition A - electric mobility from a drift speed in a given field: `speed / electricFieldStrength =
 * electricMobility` (`μ = v / E`).
 *
 * @return the typed [KElectricMobilityUnitInstance] (`this.value / fieldStrength.value` m²/(V·s)).
 */
operator fun KSpeedUnitInstance.div(
    fieldStrength: KElectricFieldStrengthUnitInstance,
): KElectricMobilityUnitInstance = electricMobilityInstanceOf(value / fieldStrength.value)

/**
 * Solved for the drift speed - `electricMobility * electricFieldStrength = speed` (`v = μ · E`).
 *
 * @return the typed [KSpeedUnitInstance] (`this.value * fieldStrength.value` m/s).
 */
operator fun KElectricMobilityUnitInstance.times(
    fieldStrength: KElectricFieldStrengthUnitInstance,
): KSpeedUnitInstance = speedUnitInstanceOf(value * fieldStrength.value)

/**
 * Commutative form of [KElectricMobilityUnitInstance.times] - `electricFieldStrength · electricMobility =
 * speed`.
 *
 * @return the typed [KSpeedUnitInstance] (`mobility.value * this.value` m/s).
 */
operator fun KElectricFieldStrengthUnitInstance.times(
    mobility: KElectricMobilityUnitInstance,
): KSpeedUnitInstance = speedUnitInstanceOf(mobility.value * value)

/**
 * Solved for the field strength - `speed / electricMobility = electricFieldStrength` (`E = v / μ`).
 *
 * @return the typed [KElectricFieldStrengthUnitInstance] (`this.value / mobility.value` V/m).
 */
operator fun KSpeedUnitInstance.div(
    mobility: KElectricMobilityUnitInstance,
): KElectricFieldStrengthUnitInstance = electricFieldStrengthInstanceOf(value / mobility.value)
