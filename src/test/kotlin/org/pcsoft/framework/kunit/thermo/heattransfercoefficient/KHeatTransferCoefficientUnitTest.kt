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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named heat transfer coefficient token carries the correct W/(m²·K) factor. */
class KHeatTransferCoefficientUnitTest {

    @Test
    fun `named tokens in watts per square meter kelvin`() {
        assertEquals(1.0, (1 of wattsPerSquareMeterKelvin) into wattsPerSquareMeterKelvin, 1e-12)
        assertEquals(
            5.678263341113489,
            (1 of btusPerHourSquareFootFahrenheit) into wattsPerSquareMeterKelvin,
            1e-9,
        )
        assertEquals(
            41840.0,
            (1 of caloriesPerSecondSquareCentimeterKelvin) into wattsPerSquareMeterKelvin,
            1e-6,
        )
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/(m²·K)", KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN.symbol)
        assertEquals(1.0, KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN.baseValue, 1e-12)
        assertEquals(
            "Btu/(h·ft²·°F)",
            KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT.symbol,
        )
        assertEquals(
            5.678263341113489,
            KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT.baseValue,
            1e-12,
        )
        assertEquals(
            "cal/(s·cm²·K)",
            KHeatTransferCoefficientUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN.symbol,
        )
        assertEquals(
            41840.0,
            KHeatTransferCoefficientUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN.baseValue,
            1e-9,
        )
    }

    @Test
    fun `base unit marker`() {
        assertEquals(
            KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN,
            KHeatTransferCoefficientUnit.BASE,
        )
        assertEquals(1.0, KHeatTransferCoefficientUnit.BASE.baseValue, 1e-12)
        assertEquals("W/(m²·K)", KHeatTransferCoefficientUnit.BASE.symbol)
    }
}
