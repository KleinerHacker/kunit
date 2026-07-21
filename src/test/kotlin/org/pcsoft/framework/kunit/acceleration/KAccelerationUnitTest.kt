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

package org.pcsoft.framework.kunit.acceleration

import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The genuinely named acceleration tokens carry the correct m/s² factor. */
class KAccelerationUnitTest {

    @Test
    fun `named tokens`() {
        assertEquals(KAccelerationUnit.GAL.baseValue, (1 of gals).value, 1e-12)
        assertEquals(0.01, (1 of gals).value, 1e-12)
        assertEquals(KAccelerationUnit.STANDARD_GRAVITY.baseValue, (1 of standardGravities).value, 1e-9)
        assertEquals(9.80665, (1 of standardGravities).value, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KAccelerationUnit.METERS_PER_SECOND_SQUARED, KAccelerationUnit.BASE)
        assertEquals(1.0, KAccelerationUnit.BASE.baseValue, 1e-12)
        assertEquals("m/s²", KAccelerationUnit.BASE.symbol)
    }
}
