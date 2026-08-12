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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KThermalConductanceUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KThermalConductanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(0.4, (0.4 of wattsPerKelvin) into wattsPerKelvin, 1e-12)
        assertEquals(400.0, (0.4 of wattsPerKelvin) into milli.wattsPerKelvin, 1e-9)
        assertEquals(1.0, (0.5275279263 of wattsPerKelvin) into btusPerHourFahrenheit, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of wattsPerKelvin, 1000 of milli.wattsPerKelvin)
        assertEquals((1 of wattsPerKelvin).hashCode(), (1000 of milli.wattsPerKelvin).hashCode())
        assertFalse((1 of wattsPerKelvin) == (2 of wattsPerKelvin))
        assertFalse((1 of wattsPerKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.4 W/K", (0.4 of wattsPerKelvin).toString())
    }

    /** Heat paths in parallel add their conductances. */
    @Test
    fun `same-type operators`() {
        val a = 0.4 of wattsPerKelvin
        val b = 0.1 of wattsPerKelvin
        assertEquals(0.5, (a + b) into wattsPerKelvin, 1e-9)
        assertEquals(0.3, (a - b) into wattsPerKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `kg·m²·s⁻³·K⁻¹` form converts back. The expression is assembled from unit templates, so the
     * raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toThermalConductance round-trip and failure`() {
        val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
        val raw = 1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm
        assertEquals(1.0, raw.toThermalConductance() into wattsPerKelvin, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm
        assertEquals(1.0e-3, rawGram.toThermalConductance() into wattsPerKelvin, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toThermalConductance() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3)).toThermalConductance()
        }
    }
}
