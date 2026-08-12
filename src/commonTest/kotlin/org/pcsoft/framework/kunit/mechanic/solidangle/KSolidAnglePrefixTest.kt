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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed solid-angle template scales its unit by the prefix factor. */
class KSolidAnglePrefixTest {

    @Test
    fun `prefixed steradians`() {
        assertEquals(0.001, (1 of milli.steradians) into steradians, 1e-15)
        assertEquals(1.0e-6, (1 of micro.steradians) into steradians, 1e-18)
    }

    @Test
    fun `prefixed square degrees`() {
        assertEquals(0.001, (1 of milli.squareDegrees) into squareDegrees, 1e-12)
    }

    @Test
    fun `prefixed spats`() {
        assertEquals(0.001, (1 of milli.spats) into spats, 1e-12)
    }
}
