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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named heat flux density token carries the correct W/m² factor. */
class KHeatFluxDensityUnitTest {

    @Test
    fun `named tokens in watts per square meter`() {
        assertEquals(1.0, (1 of wattsPerSquareMeter) into wattsPerSquareMeter, 1e-12)
        assertEquals(3.154590745063563, (1 of btusPerHourSquareFoot) into wattsPerSquareMeter, 1e-9)
        assertEquals(41840.0, (1 of caloriesPerSecondSquareCentimeter) into wattsPerSquareMeter, 1e-6)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/m²", KHeatFluxDensityUnit.WATT_PER_SQUARE_METER.symbol)
        assertEquals(1.0, KHeatFluxDensityUnit.WATT_PER_SQUARE_METER.baseValue, 1e-12)
        assertEquals("Btu/(h·ft²)", KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT.symbol)
        assertEquals(3.154590745063563, KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT.baseValue, 1e-12)
        assertEquals("cal/(s·cm²)", KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER.symbol)
        assertEquals(41840.0, KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KHeatFluxDensityUnit.WATT_PER_SQUARE_METER, KHeatFluxDensityUnit.BASE)
        assertEquals(1.0, KHeatFluxDensityUnit.BASE.baseValue, 1e-12)
        assertEquals("W/m²", KHeatFluxDensityUnit.BASE.symbol)
    }

    @Test
    fun `solar constant`() {
        assertEquals(1361.0, SOLAR_CONSTANT, 1e-9)
        assertEquals(1361.0, (SOLAR_CONSTANT of wattsPerSquareMeter) into wattsPerSquareMeter, 1e-9)
    }
}
