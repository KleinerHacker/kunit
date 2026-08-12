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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named thermal expansion token carries the correct 1/K factor. */
class KThermalExpansionUnitTest {

    @Test
    fun `named tokens in per kelvin`() {
        assertEquals(1.0, (1 of perKelvin) into perKelvin, 1e-12)
        assertEquals(1.8, (1 of perFahrenheit) into perKelvin, 1e-12)
        assertEquals(1.0e-6, (1 of ppmPerKelvin) into perKelvin, 1e-18)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("1/K", KThermalExpansionUnit.PER_KELVIN.symbol)
        assertEquals(1.0, KThermalExpansionUnit.PER_KELVIN.baseValue, 1e-12)
        assertEquals("1/°F", KThermalExpansionUnit.PER_FAHRENHEIT.symbol)
        assertEquals(1.8, KThermalExpansionUnit.PER_FAHRENHEIT.baseValue, 1e-12)
        assertEquals("ppm/K", KThermalExpansionUnit.PPM_PER_KELVIN.symbol)
        assertEquals(1.0e-6, KThermalExpansionUnit.PPM_PER_KELVIN.baseValue, 1e-18)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalExpansionUnit.PER_KELVIN, KThermalExpansionUnit.BASE)
        assertEquals(1.0, KThermalExpansionUnit.BASE.baseValue, 1e-12)
        assertEquals("1/K", KThermalExpansionUnit.BASE.symbol)
    }

    /** Steel's tabulated 12 ppm/K is 1.2e-5 1/K. */
    @Test
    fun `ppm per kelvin matches material tables`() {
        assertEquals(1.2e-5, (12 of ppmPerKelvin) into perKelvin, 1e-18)
    }
}
