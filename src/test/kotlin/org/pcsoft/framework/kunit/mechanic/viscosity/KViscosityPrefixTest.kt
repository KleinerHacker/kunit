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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed dynamic-viscosity template scales its unit by the prefix factor. */
class KViscosityPrefixTest {

    @Test
    fun `prefixed pascal seconds`() {
        assertEquals(0.001, (1 of milli.pascalSeconds) into pascalSeconds, 1e-12)
        assertEquals(1000.0, (1 of kilo.pascalSeconds) into pascalSeconds, 1e-6)
    }

    @Test
    fun `prefixed poises`() {
        assertEquals(0.01, (1 of centi.poises) into poises, 1e-12)
        assertEquals(0.001, (1 of milli.poises) into poises, 1e-12)
    }

    @Test
    fun `prefixed pound force seconds per square foot`() {
        assertEquals(
            0.001,
            (1 of milli.poundForceSecondsPerSquareFoot) into poundForceSecondsPerSquareFoot,
            1e-12,
        )
    }

    @Test
    fun `prefixed reyns`() {
        assertEquals(1.0e-6, (1 of micro.reyns) into reyns, 1e-15)
    }
}
