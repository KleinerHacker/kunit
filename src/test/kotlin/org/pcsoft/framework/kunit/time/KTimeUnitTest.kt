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

package org.pcsoft.framework.kunit.time

import kotlin.test.Test
import kotlin.test.assertEquals

class KTimeUnitTest {

    @Test
    fun `second is the base unit`() {
        assertEquals(KTimeUnit.SECOND, KTimeUnit.BASE)
        assertEquals(1.0, KTimeUnit.SECOND.baseValue)
        assertEquals("s", KTimeUnit.SECOND.symbol)
    }

    @Test
    fun `minute symbol and baseValue`() {
        assertEquals("min", KTimeUnit.MINUTE.symbol)
        assertEquals(60.0, KTimeUnit.MINUTE.baseValue)
    }

    @Test
    fun `hour symbol and baseValue`() {
        assertEquals("h", KTimeUnit.HOUR.symbol)
        assertEquals(3600.0, KTimeUnit.HOUR.baseValue)
    }

    @Test
    fun `day symbol and baseValue`() {
        assertEquals("d", KTimeUnit.DAY.symbol)
        assertEquals(86_400.0, KTimeUnit.DAY.baseValue)
    }

    @Test
    fun `all enum values are covered`() {
        assertEquals(4, KTimeUnit.entries.size)
    }
}
