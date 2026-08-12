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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named luminous efficacy token carries the correct lm/W factor. */
class KLuminousEfficacyUnitTest {

    @Test
    fun `named tokens in lumens per watt`() {
        assertEquals(1.0, (1 of lumensPerWatt) into lumensPerWatt, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("lm/W", KLuminousEfficacyUnit.LUMEN_PER_WATT.symbol)
        assertEquals(1.0, KLuminousEfficacyUnit.LUMEN_PER_WATT.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminousEfficacyUnit.LUMEN_PER_WATT, KLuminousEfficacyUnit.BASE)
        assertEquals(1.0, KLuminousEfficacyUnit.BASE.baseValue, 1e-12)
        assertEquals("lm/W", KLuminousEfficacyUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(1, KLuminousEfficacyUnit.entries.size)
        assertEquals(listOf("lm/W"), KLuminousEfficacyUnit.entries.map { it.symbol })
    }

    /** The physical ceiling at 555 nm following from the SI definition of the candela. */
    @Test
    fun `maximum luminous efficacy constant`() {
        assertEquals(683.0, MAX_LUMINOUS_EFFICACY, 1e-12)
        assertEquals(683.0, (MAX_LUMINOUS_EFFICACY of lumensPerWatt) into lumensPerWatt, 1e-9)
    }
}
