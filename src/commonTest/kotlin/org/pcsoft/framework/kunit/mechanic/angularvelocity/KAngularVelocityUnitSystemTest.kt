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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

import kotlin.math.PI
import kotlin.test.*
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.degrees
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.of

/** `KAngularVelocityUnitInstance` surface: construction, equality, `toString`, operators, `toAngularVelocity`. */
class KAngularVelocityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(3000.0, (3000 of revolutionsPerMinute) into revolutionsPerMinute, 1e-9)
        assertEquals(50.0, (3000 of revolutionsPerMinute) into revolutionsPerSecond, 1e-9)
        assertEquals(2.0, (2 of radians / seconds) into (radians / seconds), 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(60 of revolutionsPerMinute, 1 of revolutionsPerSecond)
        assertEquals((60 of revolutionsPerMinute).hashCode(), (1 of revolutionsPerSecond).hashCode())
        assertFalse((1 of revolutionsPerSecond) == (2 of revolutionsPerSecond))
        assertFalse((1 of revolutionsPerSecond).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 rad/s", (2 of radians / seconds).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of radians / seconds
        val b = 4 of radians / seconds
        assertEquals(14.0, (a + b) into (radians / seconds), 1e-12)
        assertEquals(6.0, (a - b) into (radians / seconds), 1e-12)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toAngularVelocity round-trip and failure`() {
        val raw = (180 of degrees).toUnit() / (1 of seconds).toUnit()
        assertEquals(PI, raw.toAngularVelocity() into (radians / seconds), 1e-12)
        assertFailsWith<IllegalStateException> { (1 of radians).toUnit().toAngularVelocity() }
        assertFailsWith<IllegalStateException> {
            ((1 of meters).toUnit() / (1 of seconds).toUnit()).toAngularVelocity()
        }
        assertFailsWith<IllegalStateException> {
            ((1 of radians).toUnit() * (1 of seconds).toUnit()).toAngularVelocity()
        }
    }
}
