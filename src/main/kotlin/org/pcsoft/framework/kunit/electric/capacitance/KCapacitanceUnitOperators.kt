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

package org.pcsoft.framework.kunit.electric.capacitance

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.voltageInstanceOf

// Cross-unit operators between charge, voltage and capacitance (`C = Q / U`). They live in the capacitance
// package because capacitance depends on charge and voltage (never the other way round), so placing them
// here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`capacitanceInstanceOf` / `chargeInstanceOf` / `voltageInstanceOf`), keeping all decomposition paths
// value-equal.
//
// This set is additively extensible: further capacitance-adjacent decompositions (e.g. energy / voltage² =
// capacitance) can be added here later without touching the existing operators.

/**
 * Decomposition A - the definition of capacitance: `charge / voltage = capacitance`.
 *
 * @return the typed [KCapacitanceUnitInstance] (`this.value / voltage.value` farads).
 */
operator fun KChargeUnitInstance.div(voltage: KVoltageUnitInstance): KCapacitanceUnitInstance =
    capacitanceInstanceOf(value / voltage.value)

/**
 * Inverse of the capacitance definition - `capacitance · voltage = charge` (`Q = C · U`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value * voltage.value` coulombs).
 */
operator fun KCapacitanceUnitInstance.times(voltage: KVoltageUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * voltage.value)

/**
 * Commutative form of [times] - `voltage · capacitance = charge`.
 *
 * @return the typed [KChargeUnitInstance] (`this.value * capacitance.value` coulombs).
 */
operator fun KVoltageUnitInstance.times(capacitance: KCapacitanceUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * capacitance.value)

/**
 * Solved for voltage - `charge / capacitance = voltage` (`U = Q / C`).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value / capacitance.value` volts).
 */
operator fun KChargeUnitInstance.div(capacitance: KCapacitanceUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value / capacitance.value)
