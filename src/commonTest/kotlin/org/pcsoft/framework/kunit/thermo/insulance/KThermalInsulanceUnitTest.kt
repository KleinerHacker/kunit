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

package org.pcsoft.framework.kunit.thermo.insulance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named thermal insulance token carries the correct m²·K/W factor. */
class KThermalInsulanceUnitTest {

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
        assertEquals("m²·K/W", KThermalInsulanceUnit.SQUARE_METER_KELVIN_PER_WATT.symbol)
        assertEquals(1.0, KThermalInsulanceUnit.SQUARE_METER_KELVIN_PER_WATT.baseValue, 1e-12)
        assertEquals("h·ft²·°F/Btu", KThermalInsulanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU.symbol)
        assertEquals(
            1.0 / 5.678263341113489,
            KThermalInsulanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU.baseValue,
            1e-12,
        )
        assertEquals("clo", KThermalInsulanceUnit.CLO.symbol)
        assertEquals(0.155, KThermalInsulanceUnit.CLO.baseValue, 1e-12)
        assertEquals("tog", KThermalInsulanceUnit.TOG.symbol)
        assertEquals(0.1, KThermalInsulanceUnit.TOG.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalInsulanceUnit.SQUARE_METER_KELVIN_PER_WATT, KThermalInsulanceUnit.BASE)
        assertEquals(1.0, KThermalInsulanceUnit.BASE.baseValue, 1e-12)
        assertEquals("m²·K/W", KThermalInsulanceUnit.BASE.symbol)
    }

    /** A clo is exactly 1.55 tog - the two textile scales relate by their base values. */
    @Test
    fun `clo and tog relate`() {
        assertEquals(1.55, (1 of clo) into tog, 1e-12)
    }
}
