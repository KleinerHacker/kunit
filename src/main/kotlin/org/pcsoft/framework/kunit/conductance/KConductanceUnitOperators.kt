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

package org.pcsoft.framework.kunit.conductance

import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.electricCurrentOf
import org.pcsoft.framework.kunit.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.resistance.resistanceInstanceOf
import org.pcsoft.framework.kunit.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.voltage.voltageInstanceOf

// Cross-unit operators between conductance, resistance, voltage and electric current. They live in the
// conductance package because conductance depends on resistance, voltage and electric current, so placing
// them here avoids a dependency cycle and leaves the existing resistance operators untouched. Every typed
// decomposition funnels into the group factory (`conductanceInstanceOf` / `resistanceInstanceOf` /
// `voltageInstanceOf` / `electricCurrentOf`), keeping all decomposition paths value-equal.
//
// This set is additively extensible: further conductance decompositions can be added here later without
// touching the existing operators.

/**
 * Decomposition A - Ohm's law solved for conductance: `current / voltage = conductance`.
 *
 * @return the typed [KConductanceUnitInstance] (`this.value / voltage.value` siemens).
 */
operator fun KElectricCurrentUnitInstance.div(voltage: KVoltageUnitInstance): KConductanceUnitInstance =
    conductanceInstanceOf(value / voltage.value)

/**
 * Decomposition B - reciprocal of a resistance: `1 / resistance = conductance` (`G = 1 / R`). More
 * specific than the generic `Number.div(KUnitMeasurable)`, so the typed conductance wins overload
 * resolution.
 *
 * @return the typed [KConductanceUnitInstance] (`this / resistance.value` siemens).
 */
operator fun Number.div(resistance: KResistanceUnitInstance): KConductanceUnitInstance =
    conductanceInstanceOf(this.toDouble() / resistance.value)

/**
 * Reciprocal of a conductance: `1 / conductance = resistance` (`R = 1 / G`) - the inverse counterpart of
 * the reciprocal-resistance operator.
 *
 * @return the typed [KResistanceUnitInstance] (`this / conductance.value` ohms).
 */
operator fun Number.div(conductance: KConductanceUnitInstance): KResistanceUnitInstance =
    resistanceInstanceOf(this.toDouble() / conductance.value)

/**
 * Inverse of Ohm's law - `conductance · voltage = current` (`I = G · U`).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * voltage.value` amperes).
 */
operator fun KConductanceUnitInstance.times(voltage: KVoltageUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * voltage.value)

/**
 * Commutative form of [KConductanceUnitInstance.times] - `voltage · conductance = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * conductance.value` amperes).
 */
operator fun KVoltageUnitInstance.times(conductance: KConductanceUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * conductance.value)

/**
 * Ohm's law solved for voltage from a conductance - `current / conductance = voltage` (`U = I / G`).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value / conductance.value` volts).
 */
operator fun KElectricCurrentUnitInstance.div(conductance: KConductanceUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value / conductance.value)
