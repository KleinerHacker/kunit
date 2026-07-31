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

package org.pcsoft.framework.kunit.mechanic.angle

import org.pcsoft.framework.kunit.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed angle template scales its unit by the prefix factor. */
class KAnglePrefixTest {

    @Test
    fun `prefixed radians`() {
        assertEquals(0.001, (1 of milli.radians) into radians, 1e-15)
        assertEquals(1.0e-6, (1 of micro.radians) into radians, 1e-18)
    }

    @Test
    fun `prefixed degrees`() {
        assertEquals(0.001, (1 of milli.degrees) into degrees, 1e-12)
    }

    @Test
    fun `prefixed arcminutes`() {
        assertEquals(0.001, (1 of milli.arcminutes) into arcminutes, 1e-12)
    }

    @Test
    fun `prefixed arcseconds`() {
        assertEquals(1.0e-6, (1 of micro.arcseconds) into arcseconds, 1e-12)
    }

    @Test
    fun `prefixed gradians`() {
        assertEquals(0.001, (1 of milli.gradians) into gradians, 1e-12)
    }

    @Test
    fun `prefixed turns`() {
        assertEquals(1000.0, (1 of kilo.turns) into turns, 1e-9)
    }
}
