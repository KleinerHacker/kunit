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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.*

/** `KIlluminanceUnitInstance` surface: round-trip, equality, `toString`, operators, `toIlluminance`. */
class KIlluminanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(500.0, (500 of lux) into lux, 1e-9)
        assertEquals(0.5, (500 of lux) into kilo.lux, 1e-9)
        assertEquals(10000.0, (1 of phots) into lux, 1e-6)
        assertEquals(1.0, (10000 of lux) into phots, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of phots, 10000 of lux)
        assertEquals((1 of phots).hashCode(), (10000 of lux).hashCode())
        assertFalse((1 of lux) == (2 of lux))
        assertFalse((1 of lux).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("500.0 lx", (500 of lux).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of lux
        val b = 4 of lux
        assertEquals(14.0, (a + b) into lux, 1e-9)
        assertEquals(6.0, (a - b) into lux, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical luminousIntensity·solidAngle·distance⁻² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toIlluminance round-trip and failure`() {
        val raw = (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
        assertEquals(1.0, raw.toIlluminance() into lux, 1e-9)

        // An equivalent expression written in centimeters reduces onto the same normal form (1 lm/cm² = 1 ph).
        val rawCm = (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of centi.meters).toUnit() pow 2)
        assertEquals(1.0, rawCm.toIlluminance() into phots, 1e-9)

        val sr = (1 of steradians).toUnit()
        val m = (1 of meters).toUnit()
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() * sr).toIlluminance() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() * sr / m).toIlluminance() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() / (m pow 2)).toIlluminance() }
    }
}
