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

import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Prefixed pressure templates scale the underlying unit by the prefix factor. */
class KPressurePrefixTest {

    @Test
    fun `prefixed pascals`() {
        assertEquals(100.0, (1 of hecto.pascals) into pascals, 1e-9)         // 1 hPa
        assertEquals(1000.0, (1 of kilo.pascals) into pascals, 1e-9)         // 1 kPa
        assertEquals(1_000_000.0, (1 of mega.pascals) into pascals, 1e-3)    // 1 MPa = N/mm²
    }

    @Test
    fun `prefixed bars`() {
        assertEquals(100.0, (1 of milli.bars) into pascals, 1e-9) // 1 mbar = 100 Pa
    }
}
