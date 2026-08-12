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

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/**
 * The capacitance operators: same-type arithmetic/comparison, the escape of `capacitance*capacitance`/
 * `capacitance/capacitance` to a mixed unit, both capacitance decompositions (the definition
 * `charge/voltage` and the canonical native `mass⁻¹·length⁻²·time⁴·current²` form via `toCapacitance`),
 * the inverse operators, and the invalid-shape guard.
 */
class KCapacitanceOperatorTest {

    /** Same-type capacitance operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `capacitance same-type operators`() {
        val c1 = 100 of farads
        val c2 = 40 of farads
        assertEquals(140.0, (c1 + c2).value, 1e-9)
        assertEquals(60.0, (c1 - c2).value, 1e-9)
        assertTrue(c1 > c2)
        assertIs<KMixedUnitInstance>(c1 * c2)
        assertIs<KMixedUnitInstance>(c1 / c2)
    }

    /** Both capacitance decompositions agree: `Q/U` and the native `kg⁻¹·m⁻²·s⁴·A²` expression. */
    @Test
    fun `both capacitance decompositions agree`() {
        // Decomposition A: definition of capacitance
        val fromDefinition = (10 of coulombs) / (5 of volts)
        assertEquals(2 of farads, fromDefinition)

        // Decomposition B: native canonical expression
        val native = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
        assertEquals(2 of farads, native.toCapacitance())
        assertEquals(2.0, native.toCapacitance().value, 1e-9)

        // Both funnel into capacitanceInstanceOf and are value-equal
        assertEquals(fromDefinition, native.toCapacitance())
        assertEquals(fromDefinition.value, native.toCapacitance().value, 1e-9)
    }

    /** Inverse operators: `C·U = Q`, `U·C = Q`, `Q/C = U`. */
    @Test
    fun `inverse capacitance operators`() {
        val c = 2 of farads
        val u = 5 of volts
        assertEquals(10 of coulombs, c * u)
        assertEquals(10 of coulombs, u * c)
        assertIs<KChargeUnitInstance>(c * u)
        assertIs<KVoltageUnitInstance>((10 of coulombs) / c)
        assertEquals(5 of volts, (10 of coulombs) / c)
    }

    /** Real-world example: a 470 µF capacitor charged to 12 V stores 5.64 mC. */
    @Test
    fun `capacitor charge real world`() {
        val q = (470 of micro.farads) * (12 of volts)
        assertEquals(5.64, q into milli.coulombs, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a capacitance. */
    @Test
    fun `invalid capacitance decomposition fails`() {
        // wrong current exponent (+1 instead of +2)
        val wrongCurrent = ((amperes pow 1) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toCapacitance() }
        // wrong time exponent (+3 instead of +4)
        val wrongTime = ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toCapacitance() }
        // wrong distance exponent (-1 instead of -2)
        val wrongDistance = ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 1))
        assertFailsWith<IllegalStateException> { wrongDistance.toCapacitance() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toCapacitance() }
    }
}
