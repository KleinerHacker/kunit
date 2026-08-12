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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KSpecificChargeUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KSpecificChargeUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of coulombsPerKilogram) into coulombsPerKilogram, 1e-12)
        assertEquals(1000.0, (1 of coulombsPerKilogram) into milli.coulombsPerKilogram, 1e-9)
        assertEquals(1.0, (2.58e-4 of coulombsPerKilogram) into roentgens, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of roentgens, 2.58e-4 of coulombsPerKilogram)
        assertEquals((1 of roentgens).hashCode(), (2.58e-4 of coulombsPerKilogram).hashCode())
        assertFalse((1 of coulombsPerKilogram) == (2 of coulombsPerKilogram))
        assertFalse((1 of coulombsPerKilogram).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.0 C/kg", (1 of coulombsPerKilogram).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of coulombsPerKilogram
        val b = 4 of coulombsPerKilogram
        assertEquals(14.0, (a + b) into coulombsPerKilogram, 1e-9)
        assertEquals(6.0, (a - b) into coulombsPerKilogram, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `A·s·kg⁻¹` form converts back. The expression is assembled from unit templates, so the raw
     * mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toSpecificCharge round-trip and failure`() {
        val raw = 1 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()
        assertEquals(1.0, raw.toSpecificCharge() into coulombsPerKilogram, 1e-9)

        // The same expression per plain gram is 1000 times larger.
        val rawGram = 1 of amperes.toUnit() * (seconds pow 1) / grams.toUnit()
        assertEquals(1000.0, rawGram.toSpecificCharge() into coulombsPerKilogram, 1e-6)

        assertFailsWith<IllegalStateException> { (1 of amperes).toUnit().toSpecificCharge() }
        assertFailsWith<IllegalStateException> {
            (1 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit() / (1 of meters).toUnit())
                .toSpecificCharge()
        }
    }
}
