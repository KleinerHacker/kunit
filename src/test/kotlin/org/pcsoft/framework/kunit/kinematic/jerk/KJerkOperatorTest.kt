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
import org.pcsoft.framework.kunit.kinematic.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group jerk operators and the typed/native decomposition equivalence. */
class KJerkOperatorTest {

    /** An acceleration of 1.2 m/s² (1 Gal = 0.01 m/s²). */
    private val acceleration: KAccelerationUnitInstance = 120 of gals

    /** `acceleration / time = jerk`. */
    @Test
    fun `acceleration over time is jerk`() {
        val j = acceleration / (2 of seconds)
        assertIs<KJerkUnitInstance>(j)
        assertEquals(0.6, j into metersPerSecondCubed, 1e-9)
    }

    /** `jerk * time = acceleration` and its commutative counterpart. */
    @Test
    fun `jerk times time is acceleration`() {
        val j = 0.6 of metersPerSecondCubed
        val t = 2 of seconds
        val a1 = j * t
        val a2 = t * j
        assertIs<KAccelerationUnitInstance>(a1)
        assertIs<KAccelerationUnitInstance>(a2)
        assertEquals(1.2, a1.value, 1e-9)
        assertEquals(1.2, a2.value, 1e-9)
    }

    /** `acceleration / jerk = time`. */
    @Test
    fun `acceleration over jerk is time`() {
        val t = acceleration / (0.6 of metersPerSecondCubed)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(2.0, t into seconds, 1e-9)
    }

    /** A lift ramping to 1 m/s² within a 0.5 m/s³ comfort limit needs at least 2 s. */
    @Test
    fun `lift comfort ramp`() {
        val target = 100 of gals                       // 1 m/s²
        val t = target / (0.5 of metersPerSecondCubed)
        assertEquals(2.0, t into seconds, 1e-9)
    }

    /** The typed operator and the native `m·s⁻³` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = acceleration / (2 of seconds)
        val native = (acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()
        assertIs<KJerkUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into metersPerSecondCubed, native into metersPerSecondCubed, 1e-12)
    }
}
