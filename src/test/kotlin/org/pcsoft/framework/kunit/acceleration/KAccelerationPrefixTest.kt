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

import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Prefixed acceleration templates scale the underlying unit by the prefix factor. */
class KAccelerationPrefixTest {

    @Test
    fun `prefixed gals`() {
        assertEquals(0.00001, (1 of milli.gals).value, 1e-12) // 1 mGal = 0.01 * 1e-3 m/s²
        assertEquals(10.0, (1 of kilo.gals).value, 1e-9)      // 1 kGal = 0.01 * 1e3 m/s²
    }

    @Test
    fun `prefixed standard gravities`() {
        assertEquals(9.80665e-3, (1 of milli.standardGravities).value, 1e-12)
    }
}
