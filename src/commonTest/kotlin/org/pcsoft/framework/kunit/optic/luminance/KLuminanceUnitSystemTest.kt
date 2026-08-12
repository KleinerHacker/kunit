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

package org.pcsoft.framework.kunit.optic.luminance

import kotlin.math.PI
import kotlin.test.*
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas

/** `KLuminanceUnitInstance` surface: round-trip, equality, `toString`, operators, `toLuminance`. */
class KLuminanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(250.0, (250 of candelasPerSquareMeter) into candelasPerSquareMeter, 1e-9)
        assertEquals(0.25, (250 of candelasPerSquareMeter) into kilo.candelasPerSquareMeter, 1e-9)
        assertEquals(1.0, (10000 of candelasPerSquareMeter) into stilbs, 1e-9)
        assertEquals(1.0, ((1.0 / PI) of candelasPerSquareMeter) into apostilbs, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of stilbs, 10000 of candelasPerSquareMeter)
        assertEquals((1 of stilbs).hashCode(), (10000 of candelasPerSquareMeter).hashCode())
        assertFalse((1 of nits) == (2 of nits))
        assertFalse((1 of nits).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("250.0 cd/m^2", (250 of candelasPerSquareMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of nits
        val b = 4 of nits
        assertEquals(14.0, (a + b) into nits, 1e-9)
        assertEquals(6.0, (a - b) into nits, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical luminousIntensity·distance⁻² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toLuminance round-trip and failure`() {
        val raw = (1 of candelas).toUnit() / ((1 of meters).toUnit() pow 2)
        assertEquals(1.0, raw.toLuminance() into candelasPerSquareMeter, 1e-9)

        // An equivalent expression in centimeters reduces onto the same normal form (1 cd/cm² = 1 sb).
        val rawCm = (1 of candelas).toUnit() / ((1 of centi.meters).toUnit() pow 2)
        assertEquals(1.0, rawCm.toLuminance() into stilbs, 1e-9)

        val m = (1 of meters).toUnit()
        assertFailsWith<IllegalStateException> { (1 of candelas).toUnit().toLuminance() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() / m).toLuminance() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() / (m pow 3)).toLuminance() }
    }
}
