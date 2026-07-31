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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KMomentumUnitInstance` surface: construction, equality, `toString`, operators, `toMomentum`. */
class KMomentumUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(6.0, (6 of kilogramMetersPerSecond) into kilogramMetersPerSecond, 1e-9)
        assertEquals(6.0, (6 of kilogramMetersPerSecond) into newtonSeconds, 1e-9)
        assertEquals(600000.0, (6 of kilogramMetersPerSecond) into gramCentimetersPerSecond, 1e-3)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of newtonSeconds, 1 of kilogramMetersPerSecond)
        assertEquals((1 of newtonSeconds).hashCode(), (1 of kilogramMetersPerSecond).hashCode())
        assertEquals(1 of kilo.newtonSeconds, 1000 of newtonSeconds)
        assertFalse((1 of newtonSeconds) == (2 of newtonSeconds))
        assertFalse((1 of newtonSeconds).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("6.0 kg*m/s", (6 of kilogramMetersPerSecond).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of kilogramMetersPerSecond
        val b = 4 of kilogramMetersPerSecond
        assertEquals(14.0, (a + b) into kilogramMetersPerSecond, 1e-9)
        assertEquals(6.0, (a - b) into kilogramMetersPerSecond, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toMomentum round-trip and failure`() {
        val raw = (2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()
        assertEquals(6.0, raw.toMomentum() into kilogramMetersPerSecond, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toMomentum() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toMomentum()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * (1 of meters).toUnit() * (1 of seconds).toUnit()).toMomentum()
        }
    }
}
