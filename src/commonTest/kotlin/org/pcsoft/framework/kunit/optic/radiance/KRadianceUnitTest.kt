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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named radiance token carries the correct W/(sr·m²) factor. */
class KRadianceUnitTest {

    @Test
    fun `named tokens in watts per steradian square meter`() {
        assertEquals(1.0, (1 of wattsPerSteradianSquareMeter) into wattsPerSteradianSquareMeter, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/(sr*m^2)", KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER.symbol)
        assertEquals(1.0, KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER, KRadianceUnit.BASE)
        assertEquals(1.0, KRadianceUnit.BASE.baseValue, 1e-12)
        assertEquals("W/(sr*m^2)", KRadianceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(1, KRadianceUnit.entries.size)
        assertEquals(listOf("W/(sr*m^2)"), KRadianceUnit.entries.map { it.symbol })
    }
}
