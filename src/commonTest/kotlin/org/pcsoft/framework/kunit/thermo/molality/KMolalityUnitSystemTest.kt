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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KMolalityUnitInstance` surface: round-trip, equality, `toString`, operators, `toMolality`. */
class KMolalityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of molesPerKilogram) into molesPerKilogram, 1e-12)
        assertEquals(1000.0, (1 of molesPerKilogram) into millimolesPerKilogram, 1e-9)
        assertEquals(0.001, (1 of millimolesPerKilogram) into molesPerKilogram, 1e-15)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of molesPerKilogram, 1000 of millimolesPerKilogram)
        assertEquals((1 of molesPerKilogram).hashCode(), (1000 of millimolesPerKilogram).hashCode())
        assertFalse((1 of molesPerKilogram) == (2 of molesPerKilogram))
        assertFalse((1 of molesPerKilogram).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.25 mol/kg", (0.25 of molesPerKilogram).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of molesPerKilogram
        val b = 4 of molesPerKilogram
        assertEquals(14.0, (a + b) into molesPerKilogram, 1e-9)
        assertEquals(6.0, (a - b) into molesPerKilogram, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `mol·kg⁻¹` form converts back. The expression is assembled from unit templates, so the raw
     * mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toMolality round-trip and failure`() {
        val raw = 1 of moles.toUnit() / kilo.grams.toUnit()
        assertEquals(1.0, raw.toMolality() into molesPerKilogram, 1e-9)

        // The same expression per plain gram is 1000 times larger.
        val rawGram = 1 of moles.toUnit() / grams.toUnit()
        assertEquals(1000.0, rawGram.toMolality() into molesPerKilogram, 1e-6)

        assertFailsWith<IllegalStateException> { (1 of moles).toUnit().toMolality() }
        assertFailsWith<IllegalStateException> {
            (1 of moles.toUnit() / kilo.grams.toUnit() / (1 of meters).toUnit()).toMolality()
        }
    }
}
