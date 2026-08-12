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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named thermal conductivity token carries the correct W/(m·K) factor. */
class KThermalConductivityUnitTest {

    @Test
    fun `named tokens in watts per meter kelvin`() {
        assertEquals(1.0, (1 of wattsPerMeterKelvin) into wattsPerMeterKelvin, 1e-12)
        assertEquals(1.7307346501925406, (1 of btusPerHourFootFahrenheit) into wattsPerMeterKelvin, 1e-9)
        assertEquals(418.4, (1 of caloriesPerSecondCentimeterKelvin) into wattsPerMeterKelvin, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/(m·K)", KThermalConductivityUnit.WATT_PER_METER_KELVIN.symbol)
        assertEquals(1.0, KThermalConductivityUnit.WATT_PER_METER_KELVIN.baseValue, 1e-12)
        assertEquals("Btu/(h·ft·°F)", KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT.symbol)
        assertEquals(1.7307346501925406, KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT.baseValue, 1e-12)
        assertEquals("cal/(s·cm·K)", KThermalConductivityUnit.CALORIE_PER_SECOND_CENTIMETER_KELVIN.symbol)
        assertEquals(418.4, KThermalConductivityUnit.CALORIE_PER_SECOND_CENTIMETER_KELVIN.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalConductivityUnit.WATT_PER_METER_KELVIN, KThermalConductivityUnit.BASE)
        assertEquals(1.0, KThermalConductivityUnit.BASE.baseValue, 1e-12)
        assertEquals("W/(m·K)", KThermalConductivityUnit.BASE.symbol)
    }
}
