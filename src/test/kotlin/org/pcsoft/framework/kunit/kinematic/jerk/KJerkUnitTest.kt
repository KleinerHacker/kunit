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

package org.pcsoft.framework.kunit.kinematic.jerk

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named jerk token carries the correct m/s³ factor. */
class KJerkUnitTest {

    @Test
    fun `named tokens in meters per second cubed`() {
        assertEquals(1.0, (1 of metersPerSecondCubed) into metersPerSecondCubed, 1e-12)
        assertEquals(9.80665, (1 of standardGravitiesPerSecond) into metersPerSecondCubed, 1e-12)
        assertEquals(0.3048, (1 of feetPerSecondCubed) into metersPerSecondCubed, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m/s^3", KJerkUnit.METER_PER_SECOND_CUBED.symbol)
        assertEquals(1.0, KJerkUnit.METER_PER_SECOND_CUBED.baseValue, 1e-12)
        assertEquals("g/s", KJerkUnit.STANDARD_GRAVITY_PER_SECOND.symbol)
        assertEquals(9.80665, KJerkUnit.STANDARD_GRAVITY_PER_SECOND.baseValue, 1e-12)
        assertEquals("ft/s^3", KJerkUnit.FOOT_PER_SECOND_CUBED.symbol)
        assertEquals(0.3048, KJerkUnit.FOOT_PER_SECOND_CUBED.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KJerkUnit.METER_PER_SECOND_CUBED, KJerkUnit.BASE)
        assertEquals(1.0, KJerkUnit.BASE.baseValue, 1e-12)
        assertEquals("m/s^3", KJerkUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KJerkUnit.entries.size)
        assertEquals(listOf("m/s^3", "g/s", "ft/s^3"), KJerkUnit.entries.map { it.symbol })
    }
}
