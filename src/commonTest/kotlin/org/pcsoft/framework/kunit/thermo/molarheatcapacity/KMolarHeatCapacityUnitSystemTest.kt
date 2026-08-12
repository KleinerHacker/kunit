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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KMolarHeatCapacityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KMolarHeatCapacityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(29.1, (29.1 of joulesPerMoleKelvin) into joulesPerMoleKelvin, 1e-12)
        assertEquals(0.0291, (29.1 of joulesPerMoleKelvin) into kilo.joulesPerMoleKelvin, 1e-12)
        assertEquals(1.0, (4.184 of joulesPerMoleKelvin) into caloriesPerMoleKelvin, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joulesPerMoleKelvin, 1000 of joulesPerMoleKelvin)
        assertEquals((1 of kilo.joulesPerMoleKelvin).hashCode(), (1000 of joulesPerMoleKelvin).hashCode())
        assertFalse((1 of joulesPerMoleKelvin) == (2 of joulesPerMoleKelvin))
        assertFalse((1 of joulesPerMoleKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("29.1 J/(mol·K)", (29.1 of joulesPerMoleKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerMoleKelvin
        val b = 4 of joulesPerMoleKelvin
        assertEquals(6.0, (a - b) into joulesPerMoleKelvin, 1e-9)
        assertEquals(14.0, (a + b) into joulesPerMoleKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** The canonical five-term mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toMolarHeatCapacity round-trip and failure`() {
        val mol = (1 of moles).toUnit()
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val g = (1000 of grams).toUnit()
        val m2 = (1 of meters).toUnit() pow 2
        val s2 = (1 of seconds).toUnit() pow 2

        assertEquals(1.0, (g * m2 / s2 / mol / kelvin).toMolarHeatCapacity() into joulesPerMoleKelvin, 1e-9)

        assertFailsWith<IllegalStateException> { g.toMolarHeatCapacity() }
        assertFailsWith<IllegalStateException> { ((g pow 2) * m2 / s2 / mol / kelvin).toMolarHeatCapacity() }
        assertFailsWith<IllegalStateException> {
            (g * ((1 of meters).toUnit() pow 3) / s2 / mol / kelvin).toMolarHeatCapacity()
        }
        assertFailsWith<IllegalStateException> {
            (g * m2 / ((1 of seconds).toUnit() pow 3) / mol / kelvin).toMolarHeatCapacity()
        }
        assertFailsWith<IllegalStateException> { (g * m2 / s2 / (mol pow 2) / kelvin).toMolarHeatCapacity() }
        assertFailsWith<IllegalStateException> { (g * m2 / s2 / mol / (kelvin pow 2)).toMolarHeatCapacity() }
    }
}
