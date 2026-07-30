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

/** Every named thermal resistance token carries the correct m²·K/W factor. */
class KThermalResistanceUnitTest {

    @Test
    fun `named tokens in square meter kelvin per watt`() {
        assertEquals(1.0, (1 of squareMeterKelvinPerWatt) into squareMeterKelvinPerWatt, 1e-12)
        assertEquals(
            1.0 / 5.678263341113489,
            (1 of hourSquareFootFahrenheitPerBtu) into squareMeterKelvinPerWatt,
            1e-12,
        )
        assertEquals(0.155, (1 of clo) into squareMeterKelvinPerWatt, 1e-12)
        assertEquals(0.1, (1 of tog) into squareMeterKelvinPerWatt, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m²·K/W", KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT.symbol)
        assertEquals(1.0, KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT.baseValue, 1e-12)
        assertEquals("h·ft²·°F/Btu", KThermalResistanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU.symbol)
        assertEquals(
            1.0 / 5.678263341113489,
            KThermalResistanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU.baseValue,
            1e-12,
        )
        assertEquals("clo", KThermalResistanceUnit.CLO.symbol)
        assertEquals(0.155, KThermalResistanceUnit.CLO.baseValue, 1e-12)
        assertEquals("tog", KThermalResistanceUnit.TOG.symbol)
        assertEquals(0.1, KThermalResistanceUnit.TOG.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT, KThermalResistanceUnit.BASE)
        assertEquals(1.0, KThermalResistanceUnit.BASE.baseValue, 1e-12)
        assertEquals("m²·K/W", KThermalResistanceUnit.BASE.symbol)
    }

    /** A clo is exactly 1.55 tog - the two textile scales relate by their base values. */
    @Test
    fun `clo and tog relate`() {
        assertEquals(1.55, (1 of clo) into tog, 1e-12)
    }
}
