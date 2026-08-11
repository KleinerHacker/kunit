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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KAcousticImpedanceUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KAcousticImpedanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(413.0, (413 of rayls) into pascalSecondsPerMeter, 1e-9)
        assertEquals(41.3, (413 of rayls) into cgsRayls, 1e-9)
        assertEquals(1.48, (1.48e6 of rayls) into mega.rayls, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of cgsRayls, 10 of rayls)
        assertEquals((1 of cgsRayls).hashCode(), (10 of rayls).hashCode())
        assertFalse((1 of rayls) == (2 of rayls))
        assertFalse((1 of rayls).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("413.0 Pa*s/m", (413 of rayls).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of rayls
        val b = 4 of rayls
        assertEquals(14.0, (a + b) into rayls, 1e-9)
        assertEquals(6.0, (a - b) into rayls, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·distance⁻²·time⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toAcousticImpedance round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() / (meters pow 2) / (seconds pow 1)
        assertEquals(1.0, raw.toAcousticImpedance() into pascalSecondsPerMeter, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() / (meters pow 2) / (seconds pow 1)
        assertEquals(1.0e-3, rawGram.toAcousticImpedance() into pascalSecondsPerMeter, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toAcousticImpedance() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() / (meters pow 2) / (seconds pow 2)).toAcousticImpedance()
        }
    }
}
