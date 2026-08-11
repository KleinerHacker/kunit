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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import kotlin.test.*

/** `KRadianceUnitInstance` surface: round-trip, equality, `toString`, operators, `toRadiance`. */
class KRadianceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of wattsPerSteradianSquareMeter) into wattsPerSteradianSquareMeter, 1e-9)
        assertEquals(0.005, (5 of wattsPerSteradianSquareMeter) into kilo.wattsPerSteradianSquareMeter, 1e-12)
        assertEquals(5000.0, (5 of wattsPerSteradianSquareMeter) into milli.wattsPerSteradianSquareMeter, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of wattsPerSteradianSquareMeter, 1000 of milli.wattsPerSteradianSquareMeter)
        assertEquals(
            (1 of wattsPerSteradianSquareMeter).hashCode(),
            (1000 of milli.wattsPerSteradianSquareMeter).hashCode(),
        )
        assertFalse((1 of wattsPerSteradianSquareMeter) == (2 of wattsPerSteradianSquareMeter))
        assertFalse((1 of wattsPerSteradianSquareMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("5.0 W/(sr*m^2)", (5 of wattsPerSteradianSquareMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of wattsPerSteradianSquareMeter
        val b = 4 of wattsPerSteradianSquareMeter
        assertEquals(14.0, (a + b) into wattsPerSteradianSquareMeter, 1e-9)
        assertEquals(6.0, (a - b) into wattsPerSteradianSquareMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** The native `kg·s⁻³·sr⁻¹` form converts back; wrong shapes fail. */
    @Test
    fun `toRadiance round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()
        assertEquals(1.0, raw.toRadiance() into wattsPerSteradianSquareMeter, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() / (seconds pow 3) / steradians.toUnit()
        assertEquals(1.0e-3, rawGram.toRadiance() into wattsPerSteradianSquareMeter, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of steradians).toUnit().toRadiance() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()).toRadiance()
        }
    }
}
