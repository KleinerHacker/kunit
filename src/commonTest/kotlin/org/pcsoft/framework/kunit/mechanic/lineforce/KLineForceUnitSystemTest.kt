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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KLineForceUnitInstance` surface: construction, equality, `toString`, operators, `toLineForce`. */
class KLineForceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(4000.0, (4000 of newtonsPerMeter) into newtonsPerMeter, 1e-6)
        assertEquals(4.0, (4000 of newtonsPerMeter) into newtonsPerMillimeter, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of dynesPerCentimeter, 1 of milli.newtonsPerMeter)
        assertEquals((1 of dynesPerCentimeter).hashCode(), (1 of milli.newtonsPerMeter).hashCode())
        assertFalse((1 of newtonsPerMeter) == (2 of newtonsPerMeter))
        assertFalse((1 of newtonsPerMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("4000.0 N/m", (4000 of newtonsPerMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of newtonsPerMeter
        val b = 4 of newtonsPerMeter
        assertEquals(14.0, (a + b) into newtonsPerMeter, 1e-9)
        assertEquals(6.0, (a - b) into newtonsPerMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toLineForce round-trip and failure`() {
        val raw = (1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)
        assertEquals(1.0, raw.toLineForce() into newtonsPerMeter, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toLineForce() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toLineForce()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * (1 of meters).toUnit() / ((1 of seconds).toUnit() pow 2))
                .toLineForce()
        }
    }
}
