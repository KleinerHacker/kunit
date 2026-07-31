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

package org.pcsoft.framework.kunit.common.energy

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/**
 * The energy operators: same-type arithmetic/comparison, the escape of `energy*energy`/`energy/energy` to a
 * mixed unit, all energy decompositions (`power·time`, `power/frequency`, mechanical work `force·length`,
 * electrical `charge·voltage` and the canonical native `mass·length²·time⁻²` form via `toEnergy`), the
 * inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KEnergyOperatorTest {

    /** Same-type energy operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `energy same-type operators`() {
        val w1 = 100 of joules
        val w2 = 40 of joules
        assertEquals(140.0, (w1 + w2).value, 1e-9)
        assertEquals(60.0, (w1 - w2).value, 1e-9)
        assertTrue(w1 > w2)
        assertIs<KMixedUnitInstance>(w1 * w2)
        assertIs<KMixedUnitInstance>(w1 / w2)
    }

    /** All energy decompositions agree: `P·t`, `t·P`, `P/f`, `F·s`, `s·F`, `Q·U`, `U·Q` and the native form. */
    @Test
    fun `all energy decompositions agree`() {
        // Decomposition A: power over time
        val fromPower = (100 of watts) * (5 of seconds)
        assertEquals(500 of joules, fromPower)
        assertEquals(500 of joules, (5 of seconds) * (100 of watts))

        // Decomposition A': the inverse-time form (W/Hz = W·s = J)
        val fromFrequency = (100 of watts) / (0.2 of hertz)
        assertEquals(500 of joules, fromFrequency)

        // Decomposition B: mechanical work (100 N over 5 m)
        val fromWork = (100 of newtons) * (5 of meters)
        assertEquals(500 of joules, fromWork)
        assertEquals(500 of joules, (5 of meters) * (100 of newtons))

        // Decomposition C: electrical energy (10 C at 50 V)
        val fromCharge = (10 of coulombs) * (50 of volts)
        assertEquals(500 of joules, fromCharge)
        assertEquals(500 of joules, (50 of volts) * (10 of coulombs))

        // Decomposition D: native canonical expression
        val native = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
        assertEquals(500 of joules, native.toEnergy())
        assertEquals(500.0, native.toEnergy().value, 1e-9)

        // All funnel into energyInstanceOf and are value-equal
        assertEquals(fromPower, fromFrequency)
        assertEquals(fromPower, fromWork)
        assertEquals(fromPower, fromCharge)
        assertEquals(fromPower, native.toEnergy())
        assertEquals(fromPower.value, native.toEnergy().value, 1e-9)
    }

    /** Inverse operators: `W/t = P`, `W/P = t`, `W/Q = U`. */
    @Test
    fun `inverse energy operators`() {
        val w = 500 of joules
        assertIs<KPowerUnitInstance>(w / (5 of seconds))
        assertEquals(100 of watts, w / (5 of seconds))
        assertIs<KTimeUnitInstance>(w / (100 of watts))
        assertEquals(5.0, w / (100 of watts) into seconds, 1e-9)
        assertIs<KVoltageUnitInstance>(w / (10 of coulombs))
        assertEquals(50 of volts, w / (10 of coulombs))
    }

    /** Real-world example: a 2 kW heater running 3 hours consumes 6 kWh = 21600 kJ. */
    @Test
    fun `heater consumption real world`() {
        val w = (2 of kilo.watts) * (3 of hours)
        assertEquals(21600.0, w into kilo.joules, 1e-6)
        assertEquals(6.0, (w / ((1 of kilo.watts) * (1 of hours))).value, 1e-9) // 6 kWh
    }

    /** A non-canonical shape cannot be read back as an energy. */
    @Test
    fun `invalid energy decomposition fails`() {
        // wrong time exponent (-3 instead of -2) - that is a power
        val wrongTime = (kilo.grams * (meters pow 2)) / (seconds pow 3)
        assertFailsWith<IllegalStateException> { wrongTime.toEnergy() }
        // wrong distance exponent (+1 instead of +2) - that is a force
        val wrongDistance = (kilo.grams * (meters pow 1)) / (seconds pow 2)
        assertFailsWith<IllegalStateException> { wrongDistance.toEnergy() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toEnergy() }
    }
}
