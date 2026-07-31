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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named angular-velocity tokens and enum entries carry the correct rad/s factor. */
class KAngularVelocityUnitTest {

    @Test
    fun `named tokens in radians per second`() {
        assertEquals(2.0 * Math.PI / 60.0, (1 of revolutionsPerMinute).value, 1e-12)
        assertEquals(2.0 * Math.PI, (1 of revolutionsPerSecond).value, 1e-12)
        assertEquals(60.0, (1 of revolutionsPerSecond) into revolutionsPerMinute, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("rad/s", KAngularVelocityUnit.RADIANS_PER_SECOND.symbol)
        assertEquals("°/s", KAngularVelocityUnit.DEGREES_PER_SECOND.symbol)
        assertEquals("rpm", KAngularVelocityUnit.REVOLUTIONS_PER_MINUTE.symbol)
        assertEquals("rps", KAngularVelocityUnit.REVOLUTIONS_PER_SECOND.symbol)
        assertEquals(1.0, KAngularVelocityUnit.RADIANS_PER_SECOND.baseValue, 1e-12)
        assertEquals(Math.PI / 180.0, KAngularVelocityUnit.DEGREES_PER_SECOND.baseValue, 1e-12)
        assertEquals(2.0 * Math.PI / 60.0, KAngularVelocityUnit.REVOLUTIONS_PER_MINUTE.baseValue, 1e-12)
        assertEquals(2.0 * Math.PI, KAngularVelocityUnit.REVOLUTIONS_PER_SECOND.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KAngularVelocityUnit.RADIANS_PER_SECOND, KAngularVelocityUnit.BASE)
        assertEquals(1.0, KAngularVelocityUnit.BASE.baseValue, 1e-12)
        assertEquals("rad/s", KAngularVelocityUnit.BASE.symbol)
    }
}
