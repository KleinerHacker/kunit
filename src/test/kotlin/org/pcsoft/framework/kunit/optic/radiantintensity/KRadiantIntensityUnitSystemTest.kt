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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.spats
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import kotlin.test.*

/** `KRadiantIntensityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KRadiantIntensityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of wattsPerSteradian) into wattsPerSteradian, 1e-9)
        assertEquals(0.005, (5 of wattsPerSteradian) into kilo.wattsPerSteradian, 1e-12)
        assertEquals(5000.0, (5 of wattsPerSteradian) into milli.wattsPerSteradian, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of wattsPerSteradian, 1000 of milli.wattsPerSteradian)
        assertEquals((1 of wattsPerSteradian).hashCode(), (1000 of milli.wattsPerSteradian).hashCode())
        assertFalse((1 of wattsPerSteradian) == (2 of wattsPerSteradian))
        assertFalse((1 of wattsPerSteradian).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("5.0 W/sr", (5 of wattsPerSteradian).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of wattsPerSteradian
        val b = 4 of wattsPerSteradian
        assertEquals(14.0, (a + b) into wattsPerSteradian, 1e-9)
        assertEquals(6.0, (a - b) into wattsPerSteradian, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `kg·m²·s⁻³·sr⁻¹` form converts back. The expression is assembled from unit templates, so
     * the raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toRadiantIntensity round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
        assertEquals(1.0, raw.toRadiantIntensity() into wattsPerSteradian, 1e-9)

        // Spread over a whole spat (4π sr) instead of one steradian, the same flux is 4π times weaker.
        val rawSpat = 1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / spats.toUnit()
        assertEquals(1.0 / (4.0 * Math.PI), rawSpat.toRadiantIntensity() into wattsPerSteradian, 1e-9)

        assertFailsWith<IllegalStateException> { (1 of steradians).toUnit().toRadiantIntensity() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3)).toRadiantIntensity()
        }
    }
}
