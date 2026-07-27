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

package org.pcsoft.framework.kunit.charge

import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.electricCurrentOf
import org.pcsoft.framework.kunit.frequency.KFrequencyUnitInstance
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.timeUnitInstanceOf

// Cross-unit operators between electric current, time, frequency and charge (`Q = I · t`). They live in
// the charge package because charge depends on electric current and time (never the other way round), so
// placing them here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`chargeInstanceOf` / `electricCurrentOf` / `timeUnitInstanceOf`), keeping all decomposition paths
// value-equal.
//
// This set is additively extensible: further charge-adjacent decompositions (e.g. capacitance · voltage =
// charge) can be added here later without touching the existing operators.

/**
 * Decomposition A - the definition of charge: `current · time = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`this.value * time.value` coulombs).
 */
operator fun KElectricCurrentUnitInstance.times(time: KTimeUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * time.value)

/**
 * Commutative form of [KElectricCurrentUnitInstance.times] - `time · current = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`this.value * current.value` coulombs).
 */
operator fun KTimeUnitInstance.times(current: KElectricCurrentUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(current.value * value)

/**
 * Decomposition B - the inverse-time form: `current / frequency = charge` (dividing by a frequency mirrors
 * a multiplication by a time, `1/Hz = s`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value / frequency.value` coulombs).
 */
operator fun KElectricCurrentUnitInstance.div(frequency: KFrequencyUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value / frequency.value)

/**
 * Solved for current - `charge / time = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value / time.value` amperes).
 */
operator fun KChargeUnitInstance.div(time: KTimeUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value / time.value)

/**
 * Solved for time - `charge / current = time`.
 *
 * @return the typed [KTimeUnitInstance] (`this.value / current.value` seconds).
 */
operator fun KChargeUnitInstance.div(current: KElectricCurrentUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / current.value)

/**
 * Inverse-time counterpart of [KChargeUnitInstance.div] against a time - `charge · frequency = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * frequency.value` amperes).
 */
operator fun KChargeUnitInstance.times(frequency: KFrequencyUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * frequency.value)
