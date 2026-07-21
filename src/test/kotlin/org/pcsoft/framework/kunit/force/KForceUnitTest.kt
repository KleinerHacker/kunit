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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The genuinely named force tokens carry the correct newton factor. */
class KForceUnitTest {

    @Test
    fun `named tokens in newtons`() {
        assertEquals(1.0, (1 of newtons) into newtons, 1e-12)
        assertEquals(1.0e-5, (1 of dynes) into newtons, 1e-12)
        assertEquals(4.4482216152605, (1 of poundsForce) into newtons, 1e-9)
        assertEquals(9.80665e-3, (1 of ponds) into newtons, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KForceUnit.NEWTON, KForceUnit.BASE)
        assertEquals(1.0, KForceUnit.BASE.baseValue, 1e-12)
        assertEquals("N", KForceUnit.BASE.symbol)
    }
}
