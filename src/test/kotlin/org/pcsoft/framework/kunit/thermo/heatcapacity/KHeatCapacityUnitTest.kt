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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named heat capacity token carries the correct J/K factor. */
class KHeatCapacityUnitTest {

    @Test
    fun `named tokens in joules per kelvin`() {
        assertEquals(1.0, (1 of joulesPerKelvin) into joulesPerKelvin, 1e-12)
        assertEquals(4.184, (1 of caloriesPerKelvin) into joulesPerKelvin, 1e-9)
        assertEquals(1055.05585262 / (5.0 / 9.0), (1 of btusPerFahrenheit) into joulesPerKelvin, 1e-6)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/K", KHeatCapacityUnit.JOULE_PER_KELVIN.symbol)
        assertEquals(1.0, KHeatCapacityUnit.JOULE_PER_KELVIN.baseValue, 1e-12)
        assertEquals("cal/K", KHeatCapacityUnit.CALORIE_PER_KELVIN.symbol)
        assertEquals(4.184, KHeatCapacityUnit.CALORIE_PER_KELVIN.baseValue, 1e-12)
        assertEquals("Btu/°F", KHeatCapacityUnit.BTU_PER_FAHRENHEIT.symbol)
        assertEquals(1055.05585262 / (5.0 / 9.0), KHeatCapacityUnit.BTU_PER_FAHRENHEIT.baseValue, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KHeatCapacityUnit.JOULE_PER_KELVIN, KHeatCapacityUnit.BASE)
        assertEquals(1.0, KHeatCapacityUnit.BASE.baseValue, 1e-12)
        assertEquals("J/K", KHeatCapacityUnit.BASE.symbol)
    }
}
