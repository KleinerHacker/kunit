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

package org.pcsoft.framework.kunit.electric.elastance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KElastanceUnitInstance` surface: round-trip, equality, `toString`, operators, `toElastance`. */
class KElastanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1000.0, (1000 of reciprocalFarads) into reciprocalFarads, 1e-9)
        assertEquals(1.0, (1000 of reciprocalFarads) into kilo.reciprocalFarads, 1e-12)
        assertEquals(1000.0, (1000 of darafs) into reciprocalFarads, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of reciprocalFarads, 1000 of milli.reciprocalFarads)
        assertEquals(
            (1 of reciprocalFarads).hashCode(),
            (1000 of milli.reciprocalFarads).hashCode(),
        )
        assertFalse((1 of reciprocalFarads) == (2 of reciprocalFarads))
        assertFalse((1 of reciprocalFarads).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 1/F", (1000 of reciprocalFarads).toString())
    }

    /** Capacitors in series add their elastances. */
    @Test
    fun `same-type operators`() {
        val a = 1000 of reciprocalFarads
        val b = 500 of reciprocalFarads
        assertEquals(1500.0, (a + b) into reciprocalFarads, 1e-9)
        assertEquals(500.0, (a - b) into reciprocalFarads, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `kg·m²·s⁻⁴·A⁻²` form converts back. The expression is assembled from unit templates, so
     * the raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toElastance round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2)
        assertEquals(1.0, raw.toElastance() into reciprocalFarads, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2)
        assertEquals(1.0e-3, rawGram.toElastance() into reciprocalFarads, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of amperes).toUnit().toElastance() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / (amperes.toUnit() pow 2))
                .toElastance()
        }
    }
}
