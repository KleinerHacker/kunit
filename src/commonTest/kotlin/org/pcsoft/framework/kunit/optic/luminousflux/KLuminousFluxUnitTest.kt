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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named luminous flux token carries the correct lumen factor. */
class KLuminousFluxUnitTest {

    @Test
    fun `named tokens in lumens`() {
        assertEquals(1.0, (1 of lumens) into lumens, 1e-12)
        assertEquals(1.0, (1 of candelaSteradians) into lumens, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("lm", KLuminousFluxUnit.LUMEN.symbol)
        assertEquals(1.0, KLuminousFluxUnit.LUMEN.baseValue, 1e-12)
        assertEquals("cd·sr", KLuminousFluxUnit.CANDELA_STERADIAN.symbol)
        assertEquals(1.0, KLuminousFluxUnit.CANDELA_STERADIAN.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminousFluxUnit.LUMEN, KLuminousFluxUnit.BASE)
        assertEquals(1.0, KLuminousFluxUnit.BASE.baseValue, 1e-12)
        assertEquals("lm", KLuminousFluxUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KLuminousFluxUnit.entries.size)
        assertEquals(listOf("lm", "cd·sr"), KLuminousFluxUnit.entries.map { it.symbol })
    }
}
