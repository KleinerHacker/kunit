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

package org.pcsoft.framework.kunit.mechanic.angularacceleration

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/** The named angular-acceleration tokens and enum entries carry the correct rad/s² factor. */
class KAngularAccelerationUnitTest {

    @Test
    fun `named tokens in radians per second squared`() {
        assertEquals(1.0, (1 of radiansPerSecondSquared).value, 1e-12)
        assertEquals(PI / 180.0, (1 of degreesPerSecondSquared).value, 1e-12)
        assertEquals(2.0 * PI, (1 of revolutionsPerSecondSquared).value, 1e-12)
        assertEquals(2.0 * PI / 60.0, (1 of revolutionsPerMinutePerSecond).value, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("rad/s^2", KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED.symbol)
        assertEquals("°/s^2", KAngularAccelerationUnit.DEGREES_PER_SECOND_SQUARED.symbol)
        assertEquals("rps^2", KAngularAccelerationUnit.REVOLUTIONS_PER_SECOND_SQUARED.symbol)
        assertEquals("rpm/s", KAngularAccelerationUnit.REVOLUTIONS_PER_MINUTE_PER_SECOND.symbol)
        assertEquals(1.0, KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED.baseValue, 1e-12)
        assertEquals(PI / 180.0, KAngularAccelerationUnit.DEGREES_PER_SECOND_SQUARED.baseValue, 1e-12)
        assertEquals(2.0 * PI, KAngularAccelerationUnit.REVOLUTIONS_PER_SECOND_SQUARED.baseValue, 1e-12)
        assertEquals(
            2.0 * PI / 60.0,
            KAngularAccelerationUnit.REVOLUTIONS_PER_MINUTE_PER_SECOND.baseValue,
            1e-12,
        )
    }

    @Test
    fun `conversions between tokens`() {
        assertEquals(60.0, (1 of revolutionsPerSecondSquared) into revolutionsPerMinutePerSecond, 1e-9)
        assertEquals(180.0 / PI, (1 of radiansPerSecondSquared) into degreesPerSecondSquared, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(
            KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED,
            KAngularAccelerationUnit.BASE,
        )
        assertEquals(1.0, KAngularAccelerationUnit.BASE.baseValue, 1e-12)
        assertEquals("rad/s^2", KAngularAccelerationUnit.BASE.symbol)
    }
}
