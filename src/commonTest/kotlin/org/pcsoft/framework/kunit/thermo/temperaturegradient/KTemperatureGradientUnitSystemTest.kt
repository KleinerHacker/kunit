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

package org.pcsoft.framework.kunit.thermo.temperaturegradient

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KTemperatureGradientUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KTemperatureGradientUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(25.0, (25 of kelvinPerKilometer) into kelvinPerKilometer, 1e-12)
        assertEquals(0.025, (25 of kelvinPerKilometer) into kelvinPerMeter, 1e-15)
        assertEquals(25.0, (0.025 of kelvinPerMeter) into kelvinPerKilometer, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kelvinPerMeter, 1000 of kelvinPerKilometer)
        assertEquals((1 of kelvinPerMeter).hashCode(), (1000 of kelvinPerKilometer).hashCode())
        assertFalse((1 of kelvinPerMeter) == (2 of kelvinPerMeter))
        assertFalse((1 of kelvinPerMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.025 K/m", (25 of kelvinPerKilometer).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of kelvinPerMeter
        val b = 4 of kelvinPerMeter
        assertEquals(6.0, (a - b) into kelvinPerMeter, 1e-9)
        assertEquals(14.0, (a + b) into kelvinPerMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical temperature¹·distance⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toTemperatureGradient round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val m = (1 of meters).toUnit()
        assertEquals(1.0, (kelvin / m).toTemperatureGradient() into kelvinPerMeter, 1e-9)

        assertFailsWith<IllegalStateException> { kelvin.toTemperatureGradient() }
        assertFailsWith<IllegalStateException> { ((kelvin pow 2) / m).toTemperatureGradient() }
        assertFailsWith<IllegalStateException> { (kelvin / (m pow 2)).toTemperatureGradient() }
    }
}
