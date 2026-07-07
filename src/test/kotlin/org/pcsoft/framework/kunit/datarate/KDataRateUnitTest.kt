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

package org.pcsoft.framework.kunit.datarate

import kotlin.test.Test
import kotlin.test.assertEquals

class KDataRateUnitTest {

    /** Byte-per-second is the group's base unit (factor 1.0, symbol "B/s") and equals [KDataRateUnit.BASE]. */
    @Test
    fun `bytes per second is the base unit`() {
        assertEquals(KDataRateUnit.BYTES_PER_SECOND, KDataRateUnit.BASE)
        assertEquals(1.0, KDataRateUnit.BYTES_PER_SECOND.baseValue)
        assertEquals("B/s", KDataRateUnit.BYTES_PER_SECOND.symbol)
    }

    /** bit/s carries the symbol "bit/s" and the conversion factor 0.125 B/s (an eighth of a byte per second). */
    @Test
    fun `bits per second symbol and baseValue`() {
        assertEquals("bit/s", KDataRateUnit.BITS_PER_SECOND.symbol)
        assertEquals(0.125, KDataRateUnit.BITS_PER_SECOND.baseValue)
    }

    /** The group defines exactly two units — guards against an accidental addition/removal. */
    @Test
    fun `all enum values are covered`() {
        assertEquals(2, KDataRateUnit.entries.size)
    }
}
