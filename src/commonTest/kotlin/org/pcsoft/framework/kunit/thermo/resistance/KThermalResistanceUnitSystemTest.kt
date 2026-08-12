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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KThermalResistanceUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KThermalResistanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.5, (2.5 of kelvinsPerWatt) into kelvinsPerWatt, 1e-12)
        assertEquals(2500.0, (2.5 of kelvinsPerWatt) into milli.kelvinsPerWatt, 1e-9)
        assertEquals(1.0, (1.8956342406 of kelvinsPerWatt) into hourFahrenheitPerBtu, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kelvinsPerWatt, 1000 of milli.kelvinsPerWatt)
        assertEquals((1 of kelvinsPerWatt).hashCode(), (1000 of milli.kelvinsPerWatt).hashCode())
        assertFalse((1 of kelvinsPerWatt) == (2 of kelvinsPerWatt))
        assertFalse((1 of kelvinsPerWatt).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.5 K/W", (2.5 of kelvinsPerWatt).toString())
    }

    /** A thermal chain in series: junction-to-case + case-to-sink + sink-to-air. */
    @Test
    fun `same-type operators`() {
        val junctionToCase = 0.5 of kelvinsPerWatt
        val caseToSink = 0.2 of kelvinsPerWatt
        val sinkToAir = 1.8 of kelvinsPerWatt
        assertEquals(2.5, (junctionToCase + caseToSink + sinkToAir) into kelvinsPerWatt, 1e-9)
        assertEquals(1.6, (sinkToAir - caseToSink) into kelvinsPerWatt, 1e-9)
        assertTrue(sinkToAir > junctionToCase)
        assertIs<KMixedUnitInstance>(junctionToCase * caseToSink)
        assertIs<KMixedUnitInstance>(junctionToCase / caseToSink)
    }

    /**
     * The native `kg⁻¹·m⁻²·s³·K` form converts back. The expression is assembled from unit templates, so the
     * raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toThermalResistance round-trip and failure`() {
        val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
        val raw = 1 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2)
        assertEquals(1.0, raw.toThermalResistance() into kelvinsPerWatt, 1e-9)

        // The same expression on plain grams is 1000 times larger.
        val rawGram = 1 of (seconds pow 3) * kelvinTerm / grams.toUnit() / (meters pow 2)
        assertEquals(1000.0, rawGram.toThermalResistance() into kelvinsPerWatt, 1e-6)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toThermalResistance() }
        assertFailsWith<IllegalStateException> {
            (1 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit()).toThermalResistance()
        }
    }
}
