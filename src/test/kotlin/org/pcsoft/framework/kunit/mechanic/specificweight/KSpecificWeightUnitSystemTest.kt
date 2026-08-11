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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KSpecificWeightUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KSpecificWeightUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(9807.0, (9807 of newtonsPerCubicMeter) into newtonsPerCubicMeter, 1e-9)
        assertEquals(9.807, (9807 of newtonsPerCubicMeter) into kilonewtonsPerCubicMeter, 1e-12)
        assertEquals(1000.0, (1 of kilonewtonsPerCubicMeter) into newtonsPerCubicMeter, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilonewtonsPerCubicMeter, 1000 of newtonsPerCubicMeter)
        assertEquals(
            (1 of kilonewtonsPerCubicMeter).hashCode(),
            (1000 of newtonsPerCubicMeter).hashCode(),
        )
        assertFalse((1 of newtonsPerCubicMeter) == (2 of newtonsPerCubicMeter))
        assertFalse((1 of newtonsPerCubicMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("9807.0 N/m^3", (9807 of newtonsPerCubicMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of newtonsPerCubicMeter
        val b = 4 of newtonsPerCubicMeter
        assertEquals(14.0, (a + b) into newtonsPerCubicMeter, 1e-9)
        assertEquals(6.0, (a - b) into newtonsPerCubicMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·distance⁻²·time⁻² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toSpecificWeight round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() / (meters pow 2) / (seconds pow 2)
        assertEquals(1.0, raw.toSpecificWeight() into newtonsPerCubicMeter, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() / (meters pow 2) / (seconds pow 2)
        assertEquals(1.0e-3, rawGram.toSpecificWeight() into newtonsPerCubicMeter, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toSpecificWeight() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() / (meters pow 3) / (seconds pow 2)).toSpecificWeight()
        }
    }
}
