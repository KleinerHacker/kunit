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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed strain template scales its unit by the prefix factor. */
class KStrainPrefixTest {

    @Test
    fun `prefixed ratio`() {
        assertEquals(0.001, (1 of milli.ratio) into ratio, 1e-15)
        assertEquals(1.0, (1 of micro.ratio) into microstrain, 1e-9)
    }

    @Test
    fun `prefixed percent`() {
        assertEquals(1.0e-5, (1 of milli.percent) into ratio, 1e-18)
    }

    @Test
    fun `prefixed per mille`() {
        assertEquals(1.0e-6, (1 of milli.perMille) into ratio, 1e-18)
    }

    @Test
    fun `prefixed microstrain`() {
        assertEquals(1.0e-3, (1 of kilo.microstrain) into ratio, 1e-15)
    }
}
