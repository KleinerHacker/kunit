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

package org.pcsoft.framework.kunit.electricfieldstrength

import org.pcsoft.framework.kunit.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.lengthOf
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.N_IN_BASE
import org.pcsoft.framework.kunit.force.forceUnitInstanceOf
import org.pcsoft.framework.kunit.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.voltage.voltageInstanceOf

// Cross-unit operators around the electric field strength: the voltage decomposition (`E = U / l`) and the
// force decomposition (`E = F / Q`). They live in the electricFieldStrength package because the group
// depends on voltage, distance, force and charge (never the other way round), so placing them here avoids a
// dependency cycle. Every typed decomposition funnels into a group factory
// (`electricFieldStrengthInstanceOf` / `voltageInstanceOf` / `lengthOf` / `forceUnitInstanceOf` /
// `chargeInstanceOf`), keeping all decomposition paths value-equal.
//
// Force values are stored in the component form `g·m·s⁻²`, so every conversion to or from newtons applies
// [N_IN_BASE].
//
// This set is additively extensible: further field-adjacent decompositions (e.g. the permittivity pair with
// the electric flux density, see the permittivity package) can be added later without touching the existing
// operators.

// --- Decomposition A: voltage over a length ------------------------------------------------------

/**
 * Decomposition A - field strength from a voltage over a distance: `voltage / length = electricFieldStrength`
 * (`E = U / l`).
 *
 * @return the typed [KElectricFieldStrengthUnitInstance] (`this.value / length.value` V/m).
 */
operator fun KVoltageUnitInstance.div(length: KLengthUnitInstance): KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(value / length.value)

/**
 * Solved for the voltage - `electricFieldStrength * length = voltage` (`U = E · l`).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value * length.value` volts).
 */
operator fun KElectricFieldStrengthUnitInstance.times(length: KLengthUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * length.value)

/**
 * Commutative form of [KElectricFieldStrengthUnitInstance.times] - `length · electricFieldStrength = voltage`.
 *
 * @return the typed [KVoltageUnitInstance] (`fieldStrength.value * this.value` volts).
 */
operator fun KLengthUnitInstance.times(fieldStrength: KElectricFieldStrengthUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(fieldStrength.value * value)

/**
 * Solved for the length - `voltage / electricFieldStrength = length` (`l = U / E`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / fieldStrength.value` meters).
 */
operator fun KVoltageUnitInstance.div(fieldStrength: KElectricFieldStrengthUnitInstance): KLengthUnitInstance =
    lengthOf(value / fieldStrength.value)

// --- Decomposition B: force per charge -----------------------------------------------------------

/**
 * Decomposition B - field strength as the force acting on a unit charge: `force / charge =
 * electricFieldStrength` (`E = F / Q`). The force value is converted from its component form `g·m·s⁻²` to
 * newtons via [N_IN_BASE].
 *
 * @return the typed [KElectricFieldStrengthUnitInstance] (`this.value / N_IN_BASE / charge.value` V/m).
 */
operator fun KForceUnitInstance.div(charge: KChargeUnitInstance): KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(value / N_IN_BASE / charge.value)

/**
 * Solved for the force - `electricFieldStrength * charge = force` (`F = E · Q`). The result is stored in the
 * force component form `g·m·s⁻²` via [N_IN_BASE].
 *
 * @return the typed [KForceUnitInstance] (`this.value * charge.value` newtons).
 */
operator fun KElectricFieldStrengthUnitInstance.times(charge: KChargeUnitInstance): KForceUnitInstance =
    forceUnitInstanceOf(value * charge.value * N_IN_BASE)

/**
 * Commutative form of [KElectricFieldStrengthUnitInstance.times] - `charge · electricFieldStrength = force`.
 *
 * @return the typed [KForceUnitInstance] (`fieldStrength.value * this.value` newtons).
 */
operator fun KChargeUnitInstance.times(fieldStrength: KElectricFieldStrengthUnitInstance): KForceUnitInstance =
    forceUnitInstanceOf(fieldStrength.value * value * N_IN_BASE)

/**
 * Solved for the charge - `force / electricFieldStrength = charge` (`Q = F / E`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value / N_IN_BASE / fieldStrength.value` coulombs).
 */
operator fun KForceUnitInstance.div(fieldStrength: KElectricFieldStrengthUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value / N_IN_BASE / fieldStrength.value)
