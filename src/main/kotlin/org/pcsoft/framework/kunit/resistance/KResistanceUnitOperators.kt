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

package org.pcsoft.framework.kunit.resistance

import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.electricCurrentOf
import org.pcsoft.framework.kunit.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.voltage.voltageInstanceOf

// Cross-unit operators between voltage, resistance and electric current (Ohm's law, `U = R · I`). They
// live in the resistance package because resistance depends on voltage (and both on electric current), so
// placing them here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`resistanceInstanceOf` / `voltageInstanceOf` / `electricCurrentOf`), keeping all decomposition paths
// value-equal.
//
// This set is additively extensible: further Ohm's-law-adjacent decompositions (e.g. power / current² =
// resistance, power / current = voltage) can be added here later without touching the existing operators.

/**
 * Decomposition A - Ohm's law solved for resistance: `voltage / current = resistance`.
 *
 * @return the typed [KResistanceUnitInstance] (`this.value / current.value` ohms).
 */
operator fun KVoltageUnitInstance.div(current: KElectricCurrentUnitInstance): KResistanceUnitInstance =
    resistanceInstanceOf(value / current.value)

/**
 * Inverse of Ohm's law - `resistance · current = voltage`.
 *
 * @return the typed [KVoltageUnitInstance] (`this.value * current.value` volts).
 */
operator fun KResistanceUnitInstance.times(current: KElectricCurrentUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * current.value)

/**
 * Commutative form of [times] - `current · resistance = voltage`.
 *
 * @return the typed [KVoltageUnitInstance] (`this.value * resistance.value` volts).
 */
operator fun KElectricCurrentUnitInstance.times(resistance: KResistanceUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * resistance.value)

/**
 * Inverse of Ohm's law solved for current - `voltage / resistance = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value / resistance.value` amperes).
 */
operator fun KVoltageUnitInstance.div(resistance: KResistanceUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value / resistance.value)
