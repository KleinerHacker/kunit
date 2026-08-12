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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named specific heat capacity token carries the correct J/(kg·K) factor. */
class KSpecificHeatCapacityUnitTest {

    @Test
    fun `named tokens in joules per kilogram kelvin`() {
        assertEquals(1.0, (1 of joulesPerKilogramKelvin) into joulesPerKilogramKelvin, 1e-12)
        assertEquals(4184.0, (1 of caloriesPerGramKelvin) into joulesPerKilogramKelvin, 1e-9)
        assertEquals(4186.8, (1 of btusPerPoundFahrenheit) into joulesPerKilogramKelvin, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/(kg·K)", KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN.symbol)
        assertEquals(1.0, KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN.baseValue, 1e-12)
        assertEquals("cal/(g·K)", KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN.symbol)
        assertEquals(4184.0, KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN.baseValue, 1e-9)
        assertEquals("Btu/(lb·°F)", KSpecificHeatCapacityUnit.BTU_PER_POUND_FAHRENHEIT.symbol)
        assertEquals(4186.8, KSpecificHeatCapacityUnit.BTU_PER_POUND_FAHRENHEIT.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN, KSpecificHeatCapacityUnit.BASE)
        assertEquals(1.0, KSpecificHeatCapacityUnit.BASE.baseValue, 1e-12)
        assertEquals("J/(kg·K)", KSpecificHeatCapacityUnit.BASE.symbol)
    }
}
