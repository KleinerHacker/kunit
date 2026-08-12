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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named thermal conductance token carries the correct W/K factor. */
class KThermalConductanceUnitTest {

    @Test
    fun `named tokens in watts per kelvin`() {
        assertEquals(1.0, (1 of wattsPerKelvin) into wattsPerKelvin, 1e-12)
        assertEquals(0.5275279263, (1 of btusPerHourFahrenheit) into wattsPerKelvin, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/K", KThermalConductanceUnit.WATT_PER_KELVIN.symbol)
        assertEquals(1.0, KThermalConductanceUnit.WATT_PER_KELVIN.baseValue, 1e-12)
        assertEquals("Btu/(h*°F)", KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT.symbol)
        assertEquals(0.5275279263, KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalConductanceUnit.WATT_PER_KELVIN, KThermalConductanceUnit.BASE)
        assertEquals(1.0, KThermalConductanceUnit.BASE.baseValue, 1e-12)
        assertEquals("W/K", KThermalConductanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KThermalConductanceUnit.entries.size)
        assertEquals(
            listOf("W/K", "Btu/(h*°F)"),
            KThermalConductanceUnit.entries.map { it.symbol },
        )
    }
}
