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

package org.pcsoft.framework.kunit.storage

import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** All storage operators: `+`/`-` (normalized to bytes) and comparison by the byte value. */
class KStorageOperatorTest {

    /** Add/subtract normalize to bytes; comparison uses the byte value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(2.0, ((1 of bytes) + (8 of bits)).value, 1e-9)
        assertEquals(0.0, ((1 of bytes) - (8 of bits)).value, 1e-9)
        assertTrue((1 of kibi.bytes) > (1 of kilo.bytes))
    }
}
