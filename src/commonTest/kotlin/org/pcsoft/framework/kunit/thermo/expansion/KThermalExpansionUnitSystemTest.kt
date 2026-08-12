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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.*

/** `KThermalExpansionUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KThermalExpansionUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.2e-5, (12 of ppmPerKelvin) into perKelvin, 1e-18)
        assertEquals(12.0, (1.2e-5 of perKelvin) into ppmPerKelvin, 1e-9)
        assertEquals(12.0, (1.2e-5 of perKelvin) into micro.perKelvin, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of perKelvin, 1_000_000 of ppmPerKelvin)
        assertEquals((1 of perKelvin).hashCode(), (1_000_000 of ppmPerKelvin).hashCode())
        assertFalse((1 of perKelvin) == (2 of perKelvin))
        assertFalse((1 of perKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.2E-5 1/K", (12 of ppmPerKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 12 of ppmPerKelvin
        val b = 5 of ppmPerKelvin
        assertEquals(7.0, (a - b) into ppmPerKelvin, 1e-9)
        assertEquals(17.0, (a + b) into ppmPerKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A single temperature⁻¹ term converts back; wrong shapes fail. */
    @Test
    fun `toThermalExpansion round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        assertEquals(1.0, (kelvin pow -1).toThermalExpansion() into perKelvin, 1e-12)

        assertFailsWith<IllegalStateException> { kelvin.toThermalExpansion() }
        assertFailsWith<IllegalStateException> { (kelvin pow -2).toThermalExpansion() }
    }
}
