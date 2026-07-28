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

package org.pcsoft.framework.kunit.common.power

import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.electricCurrentOf
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.N_IN_BASE
import org.pcsoft.framework.kunit.mechanic.force.forceUnitInstanceOf
import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.speedUnitInstanceOf
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.voltageInstanceOf

// Cross-unit operators of the power group - the electrical form (`P = U · I`) and the mechanical form
// (`P = F · v`). They live in the power package because power depends on voltage, current, force and speed
// (never the other way round), so placing them here avoids a dependency cycle. Every typed decomposition
// funnels into the group factory (`powerInstanceOf` / `voltageInstanceOf` / `electricCurrentOf` /
// `forceUnitInstanceOf` / `speedUnitInstanceOf`), keeping all decomposition paths value-equal.
//
// A force instance stores its component value in `g·m·s⁻²` (the mass group's base is the gram), so the
// mechanical operators bridge over [N_IN_BASE] to reach newtons before combining with a speed in m/s.
//
// This set is additively extensible: further power-adjacent decompositions (e.g. energy / time = power, see
// the energy package) can be added later without touching the existing operators.

// --- Electrical decomposition (P = U · I) --------------------------------------------------------

/**
 * Decomposition A - the electrical form of power: `voltage · current = power` (`P = U · I`).
 *
 * @return the typed [KPowerUnitInstance] (`this.value * current.value` watts).
 */
operator fun KVoltageUnitInstance.times(current: KElectricCurrentUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value * current.value)

/**
 * Commutative form of [KVoltageUnitInstance.times] - `current · voltage = power`.
 *
 * @return the typed [KPowerUnitInstance] (`voltage.value * this.value` watts).
 */
operator fun KElectricCurrentUnitInstance.times(voltage: KVoltageUnitInstance): KPowerUnitInstance =
    powerInstanceOf(voltage.value * value)

/**
 * Solved for voltage - `power / current = voltage` (`U = P / I`).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value / current.value` volts).
 */
operator fun KPowerUnitInstance.div(current: KElectricCurrentUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value / current.value)

/**
 * Solved for current - `power / voltage = current` (`I = P / U`).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value / voltage.value` amperes).
 */
operator fun KPowerUnitInstance.div(voltage: KVoltageUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value / voltage.value)

// --- Mechanical decomposition (P = F · v) --------------------------------------------------------

/**
 * Decomposition B - the mechanical form of power: `force · speed = power` (`P = F · v`).
 *
 * @return the typed [KPowerUnitInstance] (`newtons * speed.value` watts).
 */
operator fun KForceUnitInstance.times(speed: KSpeedUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value / N_IN_BASE * speed.value)

/**
 * Commutative form of [KForceUnitInstance.times] - `speed · force = power`.
 *
 * @return the typed [KPowerUnitInstance] (`force newtons * this.value` watts).
 */
operator fun KSpeedUnitInstance.times(force: KForceUnitInstance): KPowerUnitInstance =
    powerInstanceOf(force.value / N_IN_BASE * value)

/**
 * Solved for speed - `power / force = speed` (`v = P / F`).
 *
 * @return the typed [KSpeedUnitInstance] (`this.value / force newtons` meters per second).
 */
operator fun KPowerUnitInstance.div(force: KForceUnitInstance): KSpeedUnitInstance =
    speedUnitInstanceOf(value / (force.value / N_IN_BASE))

/**
 * Solved for force - `power / speed = force` (`F = P / v`).
 *
 * @return the typed [KForceUnitInstance] (`this.value / speed.value` newtons, stored as `g·m·s⁻²`).
 */
operator fun KPowerUnitInstance.div(speed: KSpeedUnitInstance): KForceUnitInstance =
    forceUnitInstanceOf(value / speed.value * N_IN_BASE)
