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

package org.pcsoft.framework.kunit.power

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.voltage.volts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The power operators: same-type arithmetic/comparison, the escape of `power*power`/`power/power` to a mixed
 * unit, all power decompositions (electrical `voltage·current`, mechanical `force·speed` and the canonical
 * native `mass·length²·time⁻³` form via `toPower`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KPowerOperatorTest {

    /** Same-type power operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `power same-type operators`() {
        val p1 = 100 of watts
        val p2 = 40 of watts
        assertEquals(140.0, (p1 + p2).value, 1e-9)
        assertEquals(60.0, (p1 - p2).value, 1e-9)
        assertTrue(p1 > p2)
        assertIs<KMixedUnitInstance>(p1 * p2)
        assertIs<KMixedUnitInstance>(p1 / p2)
    }

    /** All power decompositions agree: `U·I`, `I·U`, `F·v`, `v·F` and the native `kg·m²·s⁻³` expression. */
    @Test
    fun `all power decompositions agree`() {
        // Decomposition A: the electrical form
        val electrical = (10 of volts) * (50 of amperes)
        assertEquals(500 of watts, electrical)
        assertEquals(500 of watts, (50 of amperes) * (10 of volts))

        // Decomposition B: the mechanical form (100 N at 5 m/s)
        val mechanical = (100 of newtons) * ((5 of meters) / (1 of seconds))
        assertEquals(500 of watts, mechanical)
        assertEquals(500 of watts, ((5 of meters) / (1 of seconds)) * (100 of newtons))

        // Decomposition C: native canonical expression
        val native = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
        assertEquals(500 of watts, native.toPower())
        assertEquals(500.0, native.toPower().value, 1e-9)

        // All funnel into powerInstanceOf and are value-equal
        assertEquals(electrical, mechanical)
        assertEquals(electrical, native.toPower())
        assertEquals(electrical.value, native.toPower().value, 1e-9)
    }

    /** Inverse electrical operators: `P/I = U`, `P/U = I`. */
    @Test
    fun `inverse electrical power operators`() {
        val p = 500 of watts
        assertIs<KVoltageUnitInstance>(p / (50 of amperes))
        assertEquals(10 of volts, p / (50 of amperes))
        assertIs<KElectricCurrentUnitInstance>(p / (10 of volts))
        assertEquals(50 of amperes, p / (10 of volts))
    }

    /** Inverse mechanical operators: `P/F = v`, `P/v = F`. */
    @Test
    fun `inverse mechanical power operators`() {
        val p = 500 of watts
        val v = p / (100 of newtons)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(5.0, v into (meters / seconds), 1e-9)
        val f = p / ((5 of meters) / (1 of seconds))
        assertIs<KForceUnitInstance>(f)
        assertEquals(100.0, f into newtons, 1e-9)
    }

    /** Real-world example: a 230 V mains socket at 10 A delivers 2.3 kW. */
    @Test
    fun `mains socket power real world`() {
        val p = (230 of volts) * (10 of amperes)
        assertEquals(2.3, p into kilo.watts, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a power. */
    @Test
    fun `invalid power decomposition fails`() {
        // wrong time exponent (-2 instead of -3) - that is an energy
        val wrongTime = (kilo.grams * (meters pow 2)) / (seconds pow 2)
        assertFailsWith<IllegalStateException> { wrongTime.toPower() }
        // wrong distance exponent (+1 instead of +2)
        val wrongDistance = (kilo.grams * (meters pow 1)) / (seconds pow 3)
        assertFailsWith<IllegalStateException> { wrongDistance.toPower() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toPower() }
    }
}
