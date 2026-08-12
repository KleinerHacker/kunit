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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named luminous energy token carries the correct lm·s factor. */
class KLuminousEnergyUnitTest {

    @Test
    fun `named tokens in lumen seconds`() {
        assertEquals(1.0, (1 of lumenSeconds) into lumenSeconds, 1e-12)
        assertEquals(1.0, (1 of talbots) into lumenSeconds, 1e-12)
        assertEquals(3600.0, (1 of lumenHours) into lumenSeconds, 1e-9)
    }

    /** `talbots` is a second spelling of the base unit, not a unit of its own. */
    @Test
    fun `talbots is the base unit spelling`() {
        assertEquals(1 of lumenSeconds, 1 of talbots)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("lm*s", KLuminousEnergyUnit.LUMEN_SECOND.symbol)
        assertEquals(1.0, KLuminousEnergyUnit.LUMEN_SECOND.baseValue, 1e-12)
        assertEquals("lm*h", KLuminousEnergyUnit.LUMEN_HOUR.symbol)
        assertEquals(3600.0, KLuminousEnergyUnit.LUMEN_HOUR.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminousEnergyUnit.LUMEN_SECOND, KLuminousEnergyUnit.BASE)
        assertEquals(1.0, KLuminousEnergyUnit.BASE.baseValue, 1e-12)
        assertEquals("lm*s", KLuminousEnergyUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KLuminousEnergyUnit.entries.size)
        assertEquals(listOf("lm*s", "lm*h"), KLuminousEnergyUnit.entries.map { it.symbol })
    }
}
