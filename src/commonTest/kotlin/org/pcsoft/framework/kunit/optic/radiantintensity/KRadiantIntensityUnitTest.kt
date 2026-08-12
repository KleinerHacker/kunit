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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named radiant intensity token carries the correct W/sr factor. */
class KRadiantIntensityUnitTest {

    @Test
    fun `named tokens in watts per steradian`() {
        assertEquals(1.0, (1 of wattsPerSteradian) into wattsPerSteradian, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("W/sr", KRadiantIntensityUnit.WATT_PER_STERADIAN.symbol)
        assertEquals(1.0, KRadiantIntensityUnit.WATT_PER_STERADIAN.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KRadiantIntensityUnit.WATT_PER_STERADIAN, KRadiantIntensityUnit.BASE)
        assertEquals(1.0, KRadiantIntensityUnit.BASE.baseValue, 1e-12)
        assertEquals("W/sr", KRadiantIntensityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(1, KRadiantIntensityUnit.entries.size)
        assertEquals(listOf("W/sr"), KRadiantIntensityUnit.entries.map { it.symbol })
    }
}
