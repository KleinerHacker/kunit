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

package org.pcsoft.framework.kunit.mechanic.angle

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.*

/** `KAngleUnitInstance` surface: `of`/`into` round-trip, equality, `toString`, operators, `toAngle`. */
class KAngleUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(90.0, (90 of degrees) into degrees, 1e-9)
        assertEquals(Math.PI / 2.0, (90 of degrees) into radians, 1e-12)
        assertEquals(0.25, (90 of degrees) into turns, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(180 of degrees, 0.5 of turns)
        assertEquals((180 of degrees).hashCode(), (0.5 of turns).hashCode())
        assertFalse((1 of radians) == (2 of radians))
        assertFalse((1 of radians).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 rad", (2 of radians).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 90 of degrees
        val b = 30 of degrees
        assertEquals(120.0, (a + b) into degrees, 1e-9)
        assertEquals(60.0, (a - b) into degrees, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a / b)
        assertEquals(3.0, (a / b).value, 1e-9)
    }

    @Test
    fun `scalar scaling`() {
        assertEquals(180.0, ((90 of degrees) * 2) into degrees, 1e-9)
        assertEquals(45.0, ((90 of degrees) / 2) into degrees, 1e-9)
    }

    @Test
    fun `trigonometric helpers`() {
        assertEquals(1.0, (90 of degrees).sin(), 1e-12)
        assertEquals(1.0, (0 of degrees).cos(), 1e-12)
        assertEquals(1.0, (45 of degrees).tan(), 1e-12)
    }

    @Test
    fun `toAngle round-trip and failure`() {
        val raw = (2 of radians).toUnit()
        assertEquals(2.0, raw.toAngle() into radians, 1e-12)
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toAngle() }
        assertFailsWith<IllegalStateException> { ((1 of radians).toUnit() pow 2).toAngle() }
        assertFailsWith<IllegalStateException> {
            ((1 of radians).toUnit() * (1 of seconds).toUnit()).toAngle()
        }
    }

    @Test
    fun `prefixed template participates in arithmetic`() {
        assertEquals(1.5, ((1 of radians) + (500 of milli.radians)) into radians, 1e-12)
    }
}
