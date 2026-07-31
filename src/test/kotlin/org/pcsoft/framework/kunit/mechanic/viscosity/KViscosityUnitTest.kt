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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named dynamic-viscosity tokens carry the correct Pa·s factor. */
class KViscosityUnitTest {

    @Test
    fun `named tokens in pascal seconds`() {
        assertEquals(1.0, (1 of pascalSeconds) into pascalSeconds, 1e-9)
        assertEquals(0.1, (1 of poises) into pascalSeconds, 1e-12)
        assertEquals(
            4.4482216152605 / (0.3048 * 0.3048),
            (1 of poundForceSecondsPerSquareFoot) into pascalSeconds,
            1e-9,
        )
        assertEquals(
            4.4482216152605 / (0.0254 * 0.0254),
            (1 of reyns) into pascalSeconds,
            1e-6,
        )
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("Pa*s", KViscosityUnit.PASCAL_SECOND.symbol)
        assertEquals("P", KViscosityUnit.POISE.symbol)
        assertEquals("lbf*s/ft^2", KViscosityUnit.POUND_FORCE_SECOND_PER_SQUARE_FOOT.symbol)
        assertEquals("reyn", KViscosityUnit.REYN.symbol)
        assertEquals(1.0, KViscosityUnit.PASCAL_SECOND.baseValue, 1e-12)
        assertEquals(0.1, KViscosityUnit.POISE.baseValue, 1e-12)
        assertEquals(
            47.880258980336,
            KViscosityUnit.POUND_FORCE_SECOND_PER_SQUARE_FOOT.baseValue,
            1e-9,
        )
        assertEquals(6894.757293168, KViscosityUnit.REYN.baseValue, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KViscosityUnit.PASCAL_SECOND, KViscosityUnit.BASE)
        assertEquals(1.0, KViscosityUnit.BASE.baseValue, 1e-12)
        assertEquals("Pa*s", KViscosityUnit.BASE.symbol)
    }
}
