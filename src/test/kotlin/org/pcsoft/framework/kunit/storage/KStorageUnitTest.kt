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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The concrete storage units (`KStorageUnitBareValues` + `KStorageUnitExtensions`): byte/bit conversion. */
class KStorageUnitTest {

    /** 1 byte == 8 bits, both directions. */
    @Test
    fun `byte bit conversion`() {
        assertEquals(8.0, (1 of bytes) into bits, 1e-9)
        assertEquals(0.125, (1 of bits) into bytes, 1e-12)
    }
}
