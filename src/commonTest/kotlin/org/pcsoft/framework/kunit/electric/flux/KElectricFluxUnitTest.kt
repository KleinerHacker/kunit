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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named electric flux token carries the correct V·m factor. */
class KElectricFluxUnitTest {

    @Test
    fun `named tokens in volt meters`() {
        assertEquals(1.0, (1 of voltMeters) into voltMeters, 1e-12)
        assertEquals(1.0e-2, (1 of voltCentimeters) into voltMeters, 1e-15)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("V*m", KElectricFluxUnit.VOLT_METER.symbol)
        assertEquals(1.0, KElectricFluxUnit.VOLT_METER.baseValue, 1e-12)
        assertEquals("V*cm", KElectricFluxUnit.VOLT_CENTIMETER.symbol)
        assertEquals(1.0e-2, KElectricFluxUnit.VOLT_CENTIMETER.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KElectricFluxUnit.VOLT_METER, KElectricFluxUnit.BASE)
        assertEquals(1.0, KElectricFluxUnit.BASE.baseValue, 1e-12)
        assertEquals("V*m", KElectricFluxUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KElectricFluxUnit.entries.size)
        assertEquals(listOf("V*m", "V*cm"), KElectricFluxUnit.entries.map { it.symbol })
    }
}
