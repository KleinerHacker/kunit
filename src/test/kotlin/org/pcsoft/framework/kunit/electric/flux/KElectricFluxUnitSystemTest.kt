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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/** `KElectricFluxUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KElectricFluxUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of voltMeters) into voltMeters, 1e-12)
        assertEquals(500.0, (5 of voltMeters) into voltCentimeters, 1e-9)
        assertEquals(0.005, (5 of voltMeters) into kilo.voltMeters, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of voltMeters, 100 of voltCentimeters)
        assertEquals((1 of voltMeters).hashCode(), (100 of voltCentimeters).hashCode())
        assertFalse((1 of voltMeters) == (2 of voltMeters))
        assertFalse((1 of voltMeters).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("5.0 V*m", (5 of voltMeters).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of voltMeters
        val b = 4 of voltMeters
        assertEquals(14.0, (a + b) into voltMeters, 1e-9)
        assertEquals(6.0, (a - b) into voltMeters, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `kg·m³·s⁻³·A⁻¹` form converts back. The expression is assembled from unit templates, so
     * the raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toElectricFlux round-trip and failure`() {
        val raw = 1 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit()
        assertEquals(1.0, raw.toElectricFlux() into voltMeters, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit()
        assertEquals(1.0e-3, rawGram.toElectricFlux() into voltMeters, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of amperes).toUnit().toElectricFlux() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / amperes.toUnit())
                .toElectricFlux()
        }
    }
}
