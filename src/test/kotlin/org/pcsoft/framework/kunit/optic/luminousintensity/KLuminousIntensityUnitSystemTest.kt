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

package org.pcsoft.framework.kunit.optic.luminousintensity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import kotlin.test.*

/** `KLuminousIntensityUnitInstance` surface: round-trip, equality, `toString`, operators, `toLuminousIntensity`. */
class KLuminousIntensityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1200.0, (1200 of candelas) into candelas, 1e-9)
        assertEquals(1.2, (1200 of candelas) into kilo.candelas, 1e-9)
        assertEquals(1000.0, (1 of candelas) into milli.candelas, 1e-9)
        assertEquals(0.903, (1 of hefnerCandles) into candelas, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of candelas, 1000 of milli.candelas)
        assertEquals((1 of candelas).hashCode(), (1000 of milli.candelas).hashCode())
        assertFalse((1 of candelas) == (2 of candelas))
        assertFalse((1 of candelas).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1200.0 cd", (1200 of candelas).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of candelas
        val b = 4 of candelas
        assertEquals(14.0, (a + b) into candelas, 1e-9)
        assertEquals(6.0, (a - b) into candelas, 1e-9)
        assertTrue(a > b)
        assertEquals(0, (10 of candelas).compareTo(10 of candelas))
    }

    /** A single luminous-intensity term converts back; any other shape fails. */
    @Test
    fun `toLuminousIntensity round-trip and failure`() {
        val raw = (1200 of candelas).toUnit()
        assertEquals(1200.0, raw.toLuminousIntensity() into candelas, 1e-9)

        val rawHefner = (1 of hefnerCandles).toUnit()
        assertEquals(0.903, rawHefner.toLuminousIntensity() into candelas, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toLuminousIntensity() }
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * (1 of meters).toUnit()).toLuminousIntensity()
        }
    }
}
