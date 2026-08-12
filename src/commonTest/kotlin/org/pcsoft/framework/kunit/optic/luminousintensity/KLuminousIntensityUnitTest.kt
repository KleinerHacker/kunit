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

package org.pcsoft.framework.kunit.optic.luminousintensity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named luminous intensity token carries the correct candela factor. */
class KLuminousIntensityUnitTest {

    @Test
    fun `named tokens in candelas`() {
        assertEquals(1.0, (1 of candelas) into candelas, 1e-12)
        assertEquals(0.903, (1 of hefnerCandles) into candelas, 1e-12)
        assertEquals(0.981, (1 of candlepower) into candelas, 1e-12)
        assertEquals(9.74, (1 of carcels) into candelas, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("cd", KLuminousIntensityUnit.CANDELA.symbol)
        assertEquals(1.0, KLuminousIntensityUnit.CANDELA.baseValue, 1e-12)
        assertEquals("HK", KLuminousIntensityUnit.HEFNER_CANDLE.symbol)
        assertEquals(0.903, KLuminousIntensityUnit.HEFNER_CANDLE.baseValue, 1e-12)
        assertEquals("cp", KLuminousIntensityUnit.CANDLEPOWER.symbol)
        assertEquals(0.981, KLuminousIntensityUnit.CANDLEPOWER.baseValue, 1e-12)
        assertEquals("carcel", KLuminousIntensityUnit.CARCEL.symbol)
        assertEquals(9.74, KLuminousIntensityUnit.CARCEL.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminousIntensityUnit.CANDELA, KLuminousIntensityUnit.BASE)
        assertEquals(1.0, KLuminousIntensityUnit.BASE.baseValue, 1e-12)
        assertEquals("cd", KLuminousIntensityUnit.BASE.symbol)
    }

    /** Every enum entry is reachable through `entries` - guards against a silently dropped unit. */
    @Test
    fun `enum entries are complete`() {
        assertEquals(4, KLuminousIntensityUnit.entries.size)
        assertEquals(
            listOf("cd", "HK", "cp", "carcel"),
            KLuminousIntensityUnit.entries.map { it.symbol },
        )
    }
}
