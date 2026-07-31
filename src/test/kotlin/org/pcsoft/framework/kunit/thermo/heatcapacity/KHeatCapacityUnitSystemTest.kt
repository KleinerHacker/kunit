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

package org.pcsoft.framework.kunit.thermo.heatcapacity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KHeatCapacityUnitInstance` surface: round-trip, equality, `toString`, operators, `toHeatCapacity`. */
class KHeatCapacityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(4184.0, (4184 of joulesPerKelvin) into joulesPerKelvin, 1e-9)
        assertEquals(4.184, (4184 of joulesPerKelvin) into kilo.joulesPerKelvin, 1e-9)
        assertEquals(1000.0, (4184 of joulesPerKelvin) into caloriesPerKelvin, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joulesPerKelvin, 1000 of joulesPerKelvin)
        assertEquals((1 of kilo.joulesPerKelvin).hashCode(), (1000 of joulesPerKelvin).hashCode())
        assertFalse((1 of joulesPerKelvin) == (2 of joulesPerKelvin))
        assertFalse((1 of joulesPerKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("4184.0 J/K", (4184 of joulesPerKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerKelvin
        val b = 4 of joulesPerKelvin
        assertEquals(6.0, (a - b) into joulesPerKelvin, 1e-9)
        assertEquals(14.0, (a + b) into joulesPerKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·distance²·time⁻²·temperature⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toHeatCapacity round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val raw = (1000 of grams).toUnit() *
                ((1 of meters).toUnit() pow 2) /
                ((1 of seconds).toUnit() pow 2) /
                kelvin
        assertEquals(1.0, raw.toHeatCapacity() into joulesPerKelvin, 1e-9) // 1 kg·m²/(s²·K) = 1 J/K

        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toHeatCapacity() }
        // four terms, but one carries a wrong exponent for its group
        val g = (1000 of grams).toUnit()
        val m2 = (1 of meters).toUnit() pow 2
        val s2 = (1 of seconds).toUnit() pow 2
        assertFailsWith<IllegalStateException> { ((g pow 2) * m2 / s2 / kelvin).toHeatCapacity() }
        assertFailsWith<IllegalStateException> { (g * ((1 of meters).toUnit() pow 3) / s2 / kelvin).toHeatCapacity() }
        assertFailsWith<IllegalStateException> { (g * m2 / ((1 of seconds).toUnit() pow 3) / kelvin).toHeatCapacity() }
        assertFailsWith<IllegalStateException> { (g * m2 / s2 / (kelvin pow 2)).toHeatCapacity() }
    }
}
