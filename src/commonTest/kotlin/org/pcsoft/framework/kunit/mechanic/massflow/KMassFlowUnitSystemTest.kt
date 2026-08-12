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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KMassFlowUnitInstance` surface: construction, equality, `toString`, operators, `toMassFlow`. */
class KMassFlowUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of kilogramsPerSecond) into kilogramsPerSecond, 1e-9)
        assertEquals(7.2, (2 of kilogramsPerSecond) into tonnesPerHour, 1e-9)
        assertEquals(7200.0, (2 of kilogramsPerSecond) into kilogramsPerHour, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilogramsPerSecond, 1000 of gramsPerSecond)
        assertEquals((1 of kilogramsPerSecond).hashCode(), (1000 of gramsPerSecond).hashCode())
        assertEquals(1000 of kilogramsPerSecond, 1 of kilo.kilogramsPerSecond)
        assertFalse((1 of kilogramsPerSecond) == (2 of kilogramsPerSecond))
        assertFalse((1 of kilogramsPerSecond).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 kg/s", (2 of kilogramsPerSecond).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of kilogramsPerSecond
        val b = 4 of kilogramsPerSecond
        assertEquals(14.0, (a + b) into kilogramsPerSecond, 1e-9)
        assertEquals(6.0, (a - b) into kilogramsPerSecond, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toMassFlow round-trip and failure`() {
        val raw = (2000 of grams).toUnit() / (1 of seconds).toUnit()
        assertEquals(2.0, raw.toMassFlow() into kilogramsPerSecond, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toMassFlow() }
        assertFailsWith<IllegalStateException> {
            ((1 of meters).toUnit() / (1 of seconds).toUnit()).toMassFlow()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * (1 of meters).toUnit() / (1 of seconds).toUnit()).toMassFlow()
        }
    }
}
