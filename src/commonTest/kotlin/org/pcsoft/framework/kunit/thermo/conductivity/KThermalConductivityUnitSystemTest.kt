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

package org.pcsoft.framework.kunit.thermo.conductivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KThermalConductivityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KThermalConductivityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(401.0, (401 of wattsPerMeterKelvin) into wattsPerMeterKelvin, 1e-9)
        assertEquals(0.401, (401 of wattsPerMeterKelvin) into kilo.wattsPerMeterKelvin, 1e-9)
        assertEquals(40.0, (0.04 of wattsPerMeterKelvin) into milli.wattsPerMeterKelvin, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.wattsPerMeterKelvin, 1000 of wattsPerMeterKelvin)
        assertEquals((1 of kilo.wattsPerMeterKelvin).hashCode(), (1000 of wattsPerMeterKelvin).hashCode())
        assertFalse((1 of wattsPerMeterKelvin) == (2 of wattsPerMeterKelvin))
        assertFalse((1 of wattsPerMeterKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("401.0 W/(m·K)", (401 of wattsPerMeterKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of wattsPerMeterKelvin
        val b = 4 of wattsPerMeterKelvin
        assertEquals(6.0, (a - b) into wattsPerMeterKelvin, 1e-9)
        assertEquals(14.0, (a + b) into wattsPerMeterKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·distance·time⁻³·temperature⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toThermalConductivity round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val g = (1000 of grams).toUnit()
        val m = (1 of meters).toUnit()
        val s3 = (1 of seconds).toUnit() pow 3
        assertEquals(1.0, (g * m / s3 / kelvin).toThermalConductivity() into wattsPerMeterKelvin, 1e-9)

        assertFailsWith<IllegalStateException> { g.toThermalConductivity() }
        assertFailsWith<IllegalStateException> { ((g pow 2) * m / s3 / kelvin).toThermalConductivity() }
        assertFailsWith<IllegalStateException> { (g * (m pow 2) / s3 / kelvin).toThermalConductivity() }
        assertFailsWith<IllegalStateException> {
            (g * m / ((1 of seconds).toUnit() pow 2) / kelvin).toThermalConductivity()
        }
        assertFailsWith<IllegalStateException> { (g * m / s3 / (kelvin pow 2)).toThermalConductivity() }
    }
}
