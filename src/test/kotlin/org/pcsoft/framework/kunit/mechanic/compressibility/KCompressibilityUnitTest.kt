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

package org.pcsoft.framework.kunit.mechanic.compressibility

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named compressibility token carries the correct 1/Pa factor. */
class KCompressibilityUnitTest {

    @Test
    fun `named tokens in reciprocal pascals`() {
        assertEquals(1.0, (1 of reciprocalPascals) into reciprocalPascals, 1e-12)
        assertEquals(1.0e-5, (1 of reciprocalBars) into reciprocalPascals, 1e-18)
        assertEquals(1.0 / 101325.0, (1 of reciprocalAtmospheres) into reciprocalPascals, 1e-18)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("1/Pa", KCompressibilityUnit.RECIPROCAL_PASCAL.symbol)
        assertEquals(1.0, KCompressibilityUnit.RECIPROCAL_PASCAL.baseValue, 1e-12)
        assertEquals("1/bar", KCompressibilityUnit.RECIPROCAL_BAR.symbol)
        assertEquals(1.0e-5, KCompressibilityUnit.RECIPROCAL_BAR.baseValue, 1e-18)
        assertEquals("1/atm", KCompressibilityUnit.RECIPROCAL_ATMOSPHERE.symbol)
        assertEquals(1.0 / 101325.0, KCompressibilityUnit.RECIPROCAL_ATMOSPHERE.baseValue, 1e-18)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KCompressibilityUnit.RECIPROCAL_PASCAL, KCompressibilityUnit.BASE)
        assertEquals(1.0, KCompressibilityUnit.BASE.baseValue, 1e-12)
        assertEquals("1/Pa", KCompressibilityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KCompressibilityUnit.entries.size)
        assertEquals(
            listOf("1/Pa", "1/bar", "1/atm"),
            KCompressibilityUnit.entries.map { it.symbol },
        )
    }
}
