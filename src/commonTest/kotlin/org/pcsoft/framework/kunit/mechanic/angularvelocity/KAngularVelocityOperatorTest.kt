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
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angle.turns
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group angular-velocity operators: `angle / time` and its inverses. */
class KAngularVelocityOperatorTest {

    /** `angle / time = angularvelocity`. */
    @Test
    fun `angle over time is angular velocity`() {
        val w = (1 of turns) / (1 of seconds)
        assertIs<KAngularVelocityUnitInstance>(w)
        assertEquals(60.0, w into revolutionsPerMinute, 1e-9)
    }

    /** `angularvelocity * time = angle` and the commutative `time * angularvelocity = angle`. */
    @Test
    fun `angular velocity times time is angle`() {
        val w = 1 of revolutionsPerSecond
        val a1 = w * (2 of seconds)
        val a2 = (2 of seconds) * w
        assertIs<KAngleUnitInstance>(a1)
        assertIs<KAngleUnitInstance>(a2)
        assertEquals(2.0, a1 into turns, 1e-12)
        assertEquals(2.0, a2 into turns, 1e-12)
    }

    /** `angle / angularvelocity = time`. */
    @Test
    fun `angle over angular velocity is time`() {
        val t: KTimeUnitInstance = (2 of turns) / (1 of revolutionsPerMinute)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(2.0, t into minutes, 1e-9)
    }

    /** The generic native form reduces onto the same value as the typed operator. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (2 of radians) / (4 of seconds)
        val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()
        assertEquals(typed, native)
    }
}
