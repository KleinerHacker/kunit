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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KViscosityUnitInstance` surface: construction, equality, `toString`, operators, `toViscosity`. */
class KViscosityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of pascalSeconds) into pascalSeconds, 1e-9)
        assertEquals(20.0, (2 of pascalSeconds) into poises, 1e-9)
        assertEquals(1.0, (1 of milli.pascalSeconds) into centi.poises, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of poises, 100 of milli.pascalSeconds)
        assertEquals((1 of poises).hashCode(), (100 of milli.pascalSeconds).hashCode())
        assertFalse((1 of pascalSeconds) == (2 of pascalSeconds))
        assertFalse((1 of pascalSeconds).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 Pa*s", (2 of pascalSeconds).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of pascalSeconds
        val b = 4 of pascalSeconds
        assertEquals(14.0, (a + b) into pascalSeconds, 1e-9)
        assertEquals(6.0, (a - b) into pascalSeconds, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toViscosity round-trip and failure`() {
        val raw = (1000 of grams).toUnit() / (1 of meters).toUnit() / (1 of seconds).toUnit()
        assertEquals(1.0, raw.toViscosity() into pascalSeconds, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toViscosity() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of meters).toUnit()).toViscosity()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of meters).toUnit() / (1 of seconds).toUnit() /
                    (1 of seconds).toUnit()).toViscosity()
        }
    }
}
