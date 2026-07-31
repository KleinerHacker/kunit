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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KSolidAngleUnitInstance` surface: `of`/`into`, equality, `toString`, operators, `toSolidAngle`. */
class KSolidAngleUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of steradians) into steradians, 1e-12)
        assertEquals(1.0, (1 of spats) into spats, 1e-12)
        assertEquals(4.0 * Math.PI, (1 of spats) into steradians, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of spats, (4.0 * Math.PI) of steradians)
        assertEquals((1 of spats).hashCode(), ((4.0 * Math.PI) of steradians).hashCode())
        assertFalse((1 of steradians) == (2 of steradians))
        assertFalse((1 of steradians).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 sr", (2 of steradians).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 3 of steradians
        val b = 1 of steradians
        assertEquals(4.0, (a + b) into steradians, 1e-12)
        assertEquals(2.0, (a - b) into steradians, 1e-12)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
        assertEquals(3.0, (a / b).value, 1e-12)
    }

    @Test
    fun `toSolidAngle from both shapes and failure`() {
        assertEquals(2.0, (2 of steradians).toUnit().toSolidAngle() into steradians, 1e-12)
        val nativeForm = (2 of radians).toUnit() pow 2 // rad² = sr
        assertEquals(4.0, nativeForm.toSolidAngle() into steradians, 1e-12)
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toSolidAngle() }
        assertFailsWith<IllegalStateException> { (1 of radians).toUnit().toSolidAngle() }
        assertFailsWith<IllegalStateException> {
            ((1 of radians).toUnit() * (1 of seconds).toUnit()).toSolidAngle()
        }
    }

    @Test
    fun `prefixed template participates in arithmetic`() {
        assertEquals(1.5, ((1 of steradians) + (500 of milli.steradians)) into steradians, 1e-12)
    }
}
