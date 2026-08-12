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

package org.pcsoft.framework.kunit.thermo.specificenergy

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named specific energy token carries the correct J/kg factor. */
class KSpecificEnergyUnitTest {

    @Test
    fun `named tokens in joules per kilogram`() {
        assertEquals(1.0, (1 of joulesPerKilogram) into joulesPerKilogram, 1e-12)
        assertEquals(4184.0, (1 of caloriesPerGram) into joulesPerKilogram, 1e-9)
        assertEquals(3600.0, (1 of wattHoursPerKilogram) into joulesPerKilogram, 1e-9)
        assertEquals(2326.0, (1 of btusPerPound) into joulesPerKilogram, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/kg", KSpecificEnergyUnit.JOULE_PER_KILOGRAM.symbol)
        assertEquals(1.0, KSpecificEnergyUnit.JOULE_PER_KILOGRAM.baseValue, 1e-12)
        assertEquals("cal/g", KSpecificEnergyUnit.CALORIE_PER_GRAM.symbol)
        assertEquals(4184.0, KSpecificEnergyUnit.CALORIE_PER_GRAM.baseValue, 1e-9)
        assertEquals("Wh/kg", KSpecificEnergyUnit.WATT_HOUR_PER_KILOGRAM.symbol)
        assertEquals(3600.0, KSpecificEnergyUnit.WATT_HOUR_PER_KILOGRAM.baseValue, 1e-9)
        assertEquals("Btu/lb", KSpecificEnergyUnit.BTU_PER_POUND.symbol)
        assertEquals(2326.0, KSpecificEnergyUnit.BTU_PER_POUND.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSpecificEnergyUnit.JOULE_PER_KILOGRAM, KSpecificEnergyUnit.BASE)
        assertEquals(1.0, KSpecificEnergyUnit.BASE.baseValue, 1e-12)
        assertEquals("J/kg", KSpecificEnergyUnit.BASE.symbol)
    }
}
