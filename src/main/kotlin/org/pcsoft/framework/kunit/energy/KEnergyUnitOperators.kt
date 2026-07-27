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

package org.pcsoft.framework.kunit.energy

import org.pcsoft.framework.kunit.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.N_IN_BASE
import org.pcsoft.framework.kunit.frequency.KFrequencyUnitInstance
import org.pcsoft.framework.kunit.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.power.powerInstanceOf
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.voltage.voltageInstanceOf

// Cross-unit operators of the energy group - the power-over-time form (`W = P · t`), the mechanical work
// form (`W = F · s`) and the electrical form (`W = Q · U`). They live in the energy package because energy
// depends on power, time, force, length, charge and voltage (never the other way round), so placing them
// here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`energyInstanceOf` / `powerInstanceOf` / `timeUnitInstanceOf` / `voltageInstanceOf`), keeping all
// decomposition paths value-equal.
//
// A force instance stores its component value in `g·m·s⁻²` (the mass group's base is the gram), so the
// mechanical operators bridge over [N_IN_BASE] to reach newtons before combining with a length in meters.
//
// This set is additively extensible: further energy-adjacent decompositions can be added later without
// touching the existing operators.

// --- Decomposition A: power over time (W = P · t) ------------------------------------------------

/**
 * Decomposition A - energy as power over time: `power · time = energy` (`W = P · t`).
 *
 * @return the typed [KEnergyUnitInstance] (`this.value * time.value` joules).
 */
operator fun KPowerUnitInstance.times(time: KTimeUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * time.value)

/**
 * Commutative form of [KPowerUnitInstance.times] - `time · power = energy`.
 *
 * @return the typed [KEnergyUnitInstance] (`power.value * this.value` joules).
 */
operator fun KTimeUnitInstance.times(power: KPowerUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(power.value * value)

/**
 * Inverse-time form of decomposition A: `power / frequency = energy` (dividing by a frequency mirrors a
 * multiplication by a time, `1/Hz = s`).
 *
 * @return the typed [KEnergyUnitInstance] (`this.value / frequency.value` joules).
 */
operator fun KPowerUnitInstance.div(frequency: KFrequencyUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value / frequency.value)

/**
 * Solved for power - `energy / time = power` (`P = W / t`).
 *
 * @return the typed [KPowerUnitInstance] (`this.value / time.value` watts).
 */
operator fun KEnergyUnitInstance.div(time: KTimeUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value / time.value)

/**
 * Solved for time - `energy / power = time` (`t = W / P`).
 *
 * @return the typed [KTimeUnitInstance] (`this.value / power.value` seconds).
 */
operator fun KEnergyUnitInstance.div(power: KPowerUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / power.value)

// --- Decomposition B: mechanical work (W = F · s) ------------------------------------------------

/**
 * Decomposition B - mechanical work: `force · length = energy` (`W = F · s`).
 *
 * @return the typed [KEnergyUnitInstance] (`newtons * length.value` joules).
 */
operator fun KForceUnitInstance.times(length: KLengthUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value / N_IN_BASE * length.value)

/**
 * Commutative form of [KForceUnitInstance.times] - `length · force = energy`.
 *
 * @return the typed [KEnergyUnitInstance] (`force newtons * this.value` joules).
 */
operator fun KLengthUnitInstance.times(force: KForceUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(force.value / N_IN_BASE * value)

// --- Decomposition C: electrical energy (W = Q · U) ----------------------------------------------

/**
 * Decomposition C - electrical energy: `charge · voltage = energy` (`W = Q · U`).
 *
 * @return the typed [KEnergyUnitInstance] (`this.value * voltage.value` joules).
 */
operator fun KChargeUnitInstance.times(voltage: KVoltageUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * voltage.value)

/**
 * Commutative form of [KChargeUnitInstance.times] - `voltage · charge = energy`.
 *
 * @return the typed [KEnergyUnitInstance] (`charge.value * this.value` joules).
 */
operator fun KVoltageUnitInstance.times(charge: KChargeUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(charge.value * value)

/**
 * Solved for voltage - `energy / charge = voltage` (`U = W / Q`).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value / charge.value` volts).
 */
operator fun KEnergyUnitInstance.div(charge: KChargeUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value / charge.value)
