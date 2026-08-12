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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KVolumetricHeatCapacityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KVolumetricHeatCapacityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of joulesPerCubicMeterKelvin) into joulesPerCubicMeterKelvin, 1e-12)
        assertEquals(
            4.184,
            (1 of caloriesPerCubicCentimeterKelvin) into mega.joulesPerCubicMeterKelvin,
            1e-9,
        )
        assertEquals(
            1.0,
            (4.184e6 of joulesPerCubicMeterKelvin) into caloriesPerCubicCentimeterKelvin,
            1e-12,
        )
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of caloriesPerCubicCentimeterKelvin, 4.184e6 of joulesPerCubicMeterKelvin)
        assertEquals(
            (1 of caloriesPerCubicCentimeterKelvin).hashCode(),
            (4.184e6 of joulesPerCubicMeterKelvin).hashCode(),
        )
        assertFalse((1 of joulesPerCubicMeterKelvin) == (2 of joulesPerCubicMeterKelvin))
        assertFalse((1 of joulesPerCubicMeterKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("4184000.0 J/(m^3*K)", (4.184e6 of joulesPerCubicMeterKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerCubicMeterKelvin
        val b = 4 of joulesPerCubicMeterKelvin
        assertEquals(14.0, (a + b) into joulesPerCubicMeterKelvin, 1e-9)
        assertEquals(6.0, (a - b) into joulesPerCubicMeterKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /**
     * The native `kg·m⁻¹·s⁻²·K⁻¹` form converts back. The expression is assembled from unit templates, so
     * the raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toVolumetricHeatCapacity round-trip and failure`() {
        val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
        val raw = 1 of kilo.grams.toUnit() / (meters pow 1) / (seconds pow 2) / kelvinTerm
        assertEquals(1.0, raw.toVolumetricHeatCapacity() into joulesPerCubicMeterKelvin, 1e-9)

        // The same expression on plain grams is 1000 times smaller.
        val rawGram = 1 of grams.toUnit() / (meters pow 1) / (seconds pow 2) / kelvinTerm
        assertEquals(1.0e-3, rawGram.toVolumetricHeatCapacity() into joulesPerCubicMeterKelvin, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toVolumetricHeatCapacity() }
        assertFailsWith<IllegalStateException> {
            (1 of kilo.grams.toUnit() / (meters pow 1) / (seconds pow 2)).toVolumetricHeatCapacity()
        }
    }
}
