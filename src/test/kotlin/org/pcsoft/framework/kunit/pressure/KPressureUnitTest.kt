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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The genuinely named pressure tokens carry the correct pascal factor. */
class KPressureUnitTest {

    @Test
    fun `named tokens in pascals`() {
        assertEquals(1.0, (1 of pascals) into pascals, 1e-9)
        assertEquals(100000.0, (1 of bars) into pascals, 1e-6)
        assertEquals(101325.0, (1 of atmospheres) into pascals, 1e-6)
        assertEquals(6894.757, (1 of psis) into pascals, 1e-6)
        assertEquals(133.322, (1 of torrs) into pascals, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KPressureUnit.PASCAL, KPressureUnit.BASE)
        assertEquals(1.0, KPressureUnit.BASE.baseValue, 1e-12)
        assertEquals("Pa", KPressureUnit.BASE.symbol)
    }
}
