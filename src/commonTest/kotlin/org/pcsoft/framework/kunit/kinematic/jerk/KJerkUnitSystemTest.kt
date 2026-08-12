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

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.*

/** `KJerkUnitInstance` surface: round-trip, equality, `toString`, operators, `toJerk`. */
class KJerkUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(0.6, (0.6 of metersPerSecondCubed) into metersPerSecondCubed, 1e-12)
        assertEquals(600.0, (0.6 of metersPerSecondCubed) into milli.metersPerSecondCubed, 1e-9)
        assertEquals(1.0, (9.80665 of metersPerSecondCubed) into standardGravitiesPerSecond, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of metersPerSecondCubed, 1000 of milli.metersPerSecondCubed)
        assertEquals(
            (1 of metersPerSecondCubed).hashCode(),
            (1000 of milli.metersPerSecondCubed).hashCode(),
        )
        assertFalse((1 of metersPerSecondCubed) == (2 of metersPerSecondCubed))
        assertFalse((1 of metersPerSecondCubed).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.6 m/s^3", (0.6 of metersPerSecondCubed).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of metersPerSecondCubed
        val b = 4 of metersPerSecondCubed
        assertEquals(14.0, (a + b) into metersPerSecondCubed, 1e-9)
        assertEquals(6.0, (a - b) into metersPerSecondCubed, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance·time⁻³ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toJerk round-trip and failure`() {
        val raw = (1 of meters).toUnit() / ((1 of seconds).toUnit() pow 3)
        assertEquals(1.0, raw.toJerk() into metersPerSecondCubed, 1e-9)

        // An equivalent expression written per minute reduces onto the same normal form.
        val rawMinute = (1 of meters).toUnit() / ((1 of minutes).toUnit() pow 3)
        assertEquals(1.0 / (60.0 * 60.0 * 60.0), rawMinute.toJerk() into metersPerSecondCubed, 1e-12)

        val s = (1 of seconds).toUnit()
        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toJerk() }
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() / (s pow 2)).toJerk() }
    }
}
