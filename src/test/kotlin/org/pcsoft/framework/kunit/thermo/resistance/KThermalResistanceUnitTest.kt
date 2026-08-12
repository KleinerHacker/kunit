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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named absolute thermal resistance token carries the correct K/W factor. */
class KThermalResistanceUnitTest {

    @Test
    fun `named tokens in kelvins per watt`() {
        assertEquals(1.0, (1 of kelvinsPerWatt) into kelvinsPerWatt, 1e-12)
        assertEquals(1.0, (1 of degreesCelsiusPerWatt) into kelvinsPerWatt, 1e-12)
        assertEquals(1.8956342406, (1 of hourFahrenheitPerBtu) into kelvinsPerWatt, 1e-9)
    }

    /** A temperature *difference* of 1 °C is 1 K, so both spellings coincide. */
    @Test
    fun `celsius per watt equals kelvin per watt`() {
        assertEquals(1 of kelvinsPerWatt, 1 of degreesCelsiusPerWatt)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("K/W", KThermalResistanceUnit.KELVIN_PER_WATT.symbol)
        assertEquals(1.0, KThermalResistanceUnit.KELVIN_PER_WATT.baseValue, 1e-12)
        assertEquals("°C/W", KThermalResistanceUnit.DEGREE_CELSIUS_PER_WATT.symbol)
        assertEquals(1.0, KThermalResistanceUnit.DEGREE_CELSIUS_PER_WATT.baseValue, 1e-12)
        assertEquals("h*°F/Btu", KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU.symbol)
        assertEquals(1.8956342406, KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalResistanceUnit.KELVIN_PER_WATT, KThermalResistanceUnit.BASE)
        assertEquals(1.0, KThermalResistanceUnit.BASE.baseValue, 1e-12)
        assertEquals("K/W", KThermalResistanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KThermalResistanceUnit.entries.size)
        assertEquals(
            listOf("K/W", "°C/W", "h*°F/Btu"),
            KThermalResistanceUnit.entries.map { it.symbol },
        )
    }
}
