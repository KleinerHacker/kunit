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

package org.pcsoft.framework.kunit.electric.magneticflux

import org.pcsoft.framework.kunit.kinematic.frequency.KFrequencyUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.voltageInstanceOf

// Cross-unit operators between voltage, time, frequency and magnetic flux (Faraday's law of induction,
// `Φ = U · t`). They live in the magneticflux package because flux depends on voltage and time (never the
// other way round), so placing them here avoids a dependency cycle. Every typed decomposition funnels into
// the group factory (`magneticFluxInstanceOf` / `voltageInstanceOf` / `timeUnitInstanceOf`), keeping all
// decomposition paths value-equal.
//
// This set is additively extensible: further flux-adjacent decompositions (e.g. inductance · current = flux,
// see the inductance package) can be added later without touching the existing operators.

/**
 * Decomposition A - the induction law: `voltage · time = flux` (`Φ = U · t`, since `V·s = Wb`).
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value * time.value` webers).
 */
operator fun KVoltageUnitInstance.times(time: KTimeUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value * time.value)

/**
 * Commutative form of [KVoltageUnitInstance.times] - `time · voltage = flux`.
 *
 * @return the typed [KMagneticFluxUnitInstance] (`voltage.value * this.value` webers).
 */
operator fun KTimeUnitInstance.times(voltage: KVoltageUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(voltage.value * value)

/**
 * Decomposition B - the inverse-time form: `voltage / frequency = flux` (dividing by a frequency mirrors a
 * multiplication by a time, `1/Hz = s`).
 *
 * @return the typed [KMagneticFluxUnitInstance] (`this.value / frequency.value` webers).
 */
operator fun KVoltageUnitInstance.div(frequency: KFrequencyUnitInstance): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(value / frequency.value)

/**
 * Solved for voltage - `flux / time = voltage` (the induced voltage of a flux change).
 *
 * @return the typed [KVoltageUnitInstance] (`this.value / time.value` volts).
 */
operator fun KMagneticFluxUnitInstance.div(time: KTimeUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value / time.value)

/**
 * Inverse-time counterpart of [KMagneticFluxUnitInstance.div] against a time - `flux · frequency = voltage`.
 *
 * @return the typed [KVoltageUnitInstance] (`this.value * frequency.value` volts).
 */
operator fun KMagneticFluxUnitInstance.times(frequency: KFrequencyUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * frequency.value)

/**
 * Solved for time - `flux / voltage = time`.
 *
 * @return the typed [KTimeUnitInstance] (`this.value / voltage.value` seconds).
 */
operator fun KMagneticFluxUnitInstance.div(voltage: KVoltageUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / voltage.value)
