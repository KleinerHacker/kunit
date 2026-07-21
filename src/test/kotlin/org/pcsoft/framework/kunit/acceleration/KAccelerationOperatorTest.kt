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

import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group acceleration operators: `speed / time = acceleration` and its inverses. */
class KAccelerationOperatorTest {

    /** `speed / time = acceleration`. */
    @Test
    fun `speed over time is acceleration`() {
        val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // 10 m/s over 5 s = 2 m/s²
        assertIs<KAccelerationUnitInstance>(a)
        assertEquals(2.0, a.value, 1e-9)
    }

    /** `acceleration * time = speed` and the commutative `time * acceleration = speed`. */
    @Test
    fun `acceleration times time is speed`() {
        val a = 2 of gals // 0.02 m/s²
        val v1 = a * (10 of seconds)
        val v2 = (10 of seconds) * a
        assertIs<KSpeedUnitInstance>(v1)
        assertIs<KSpeedUnitInstance>(v2)
        assertEquals(0.2, v1.value, 1e-12)
        assertEquals(0.2, v2.value, 1e-12)
    }

    /** `speed / acceleration = time`. */
    @Test
    fun `speed over acceleration is time`() {
        val v = (100 of meters) / (10 of seconds) // 10 m/s
        val a = ((100 of meters) / (10 of seconds)) / (10 of seconds) // 1 m/s²
        val t: KTimeUnitInstance = v / a
        assertIs<KTimeUnitInstance>(t)
        assertEquals(10.0, t into seconds, 1e-9)
    }
}
