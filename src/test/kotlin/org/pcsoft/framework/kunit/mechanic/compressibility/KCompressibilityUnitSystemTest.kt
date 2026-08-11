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

package org.pcsoft.framework.kunit.mechanic.compressibility

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KCompressibilityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KCompressibilityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(4.5e-10, (4.5e-10 of reciprocalPascals) into reciprocalPascals, 1e-22)
        assertEquals(4.5e-5, (4.5e-10 of reciprocalPascals) into reciprocalBars, 1e-17)
        assertEquals(1.0e-5, (1 of reciprocalBars) into reciprocalPascals, 1e-18)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of reciprocalBars, 1.0e-5 of reciprocalPascals)
        assertEquals((1 of reciprocalBars).hashCode(), (1.0e-5 of reciprocalPascals).hashCode())
        assertFalse((1 of reciprocalPascals) == (2 of reciprocalPascals))
        assertFalse((1 of reciprocalPascals).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.0 1/Pa", (1 of reciprocalPascals).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of reciprocalPascals
        val b = 4 of reciprocalPascals
        assertEquals(14.0, (a + b) into reciprocalPascals, 1e-9)
        assertEquals(6.0, (a - b) into reciprocalPascals, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass⁻¹·distance·time² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toCompressibility round-trip and failure`() {
        val raw = 1 of (meters pow 1) * (seconds pow 2) / kilo.grams.toUnit()
        assertEquals(1.0, raw.toCompressibility() into reciprocalPascals, 1e-12)

        // The same expression on plain grams is 1000 times larger.
        val rawGram = 1 of (meters pow 1) * (seconds pow 2) / grams.toUnit()
        assertEquals(1000.0, rawGram.toCompressibility() into reciprocalPascals, 1e-9)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toCompressibility() }
        assertFailsWith<IllegalStateException> {
            (1 of (meters pow 2) * (seconds pow 2) / kilo.grams.toUnit()).toCompressibility()
        }
    }
}
