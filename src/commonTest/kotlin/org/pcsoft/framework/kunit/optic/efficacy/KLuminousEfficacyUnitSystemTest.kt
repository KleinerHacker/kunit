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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.*

/** `KLuminousEfficacyUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KLuminousEfficacyUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(120.0, (120 of lumensPerWatt) into lumensPerWatt, 1e-9)
        assertEquals(0.12, (120 of lumensPerWatt) into kilo.lumensPerWatt, 1e-9)
        assertEquals(120000.0, (120 of lumensPerWatt) into milli.lumensPerWatt, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of lumensPerWatt, 1000 of milli.lumensPerWatt)
        assertEquals((1 of lumensPerWatt).hashCode(), (1000 of milli.lumensPerWatt).hashCode())
        assertFalse((1 of lumensPerWatt) == (2 of lumensPerWatt))
        assertFalse((1 of lumensPerWatt).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("120.0 lm/W", (120 of lumensPerWatt).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of lumensPerWatt
        val b = 4 of lumensPerWatt
        assertEquals(14.0, (a + b) into lumensPerWatt, 1e-9)
        assertEquals(6.0, (a - b) into lumensPerWatt, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `cd·sr·kg⁻¹·m⁻²·s³` form converts back - the gram/kilogram bridge makes a *kilogram*
     * based expression the one that reads as 1 lm/W.
     */
    @Test
    fun `toLuminousEfficacy round-trip and failure`() {
        val raw = (1 of candelas).toUnit() * (1 of steradians).toUnit() /
                (1 of kilo.grams).toUnit() /
                ((1 of meters).toUnit() pow 2) *
                ((1 of seconds).toUnit() pow 3)
        assertEquals(1.0, raw.toLuminousEfficacy() into lumensPerWatt, 1e-9)

        // The same expression built on plain grams is 1000 times larger.
        val rawGram = (1 of candelas).toUnit() * (1 of steradians).toUnit() /
                (1 of grams).toUnit() /
                ((1 of meters).toUnit() pow 2) *
                ((1 of seconds).toUnit() pow 3)
        assertEquals(1000.0, rawGram.toLuminousEfficacy() into lumensPerWatt, 1e-6)

        val sr = (1 of steradians).toUnit()
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() * sr).toLuminousEfficacy() }
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * sr / (1 of kilo.grams).toUnit()).toLuminousEfficacy()
        }
    }
}
