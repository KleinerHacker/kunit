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
import kotlin.test.assertIs
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularvelocity.KAngularVelocityUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.of

/** Typed cross-group angular-acceleration operators and the equality of both decompositions. */
class KAngularAccelerationOperatorTest {

    /** `angularvelocity / time = angularacceleration`. */
    @Test
    fun `angular velocity over time is angular acceleration`() {
        val alpha = (1 of revolutionsPerSecond) / (2 of seconds)
        assertIs<KAngularAccelerationUnitInstance>(alpha)
        assertEquals(PI, alpha into radiansPerSecondSquared, 1e-12)
    }

    /** `angularacceleration * time = angularvelocity`, and the commutative form. */
    @Test
    fun `angular acceleration times time is angular velocity`() {
        val alpha = 2 of radiansPerSecondSquared
        val w1 = alpha * (3 of seconds)
        val w2 = (3 of seconds) * alpha
        assertIs<KAngularVelocityUnitInstance>(w1)
        assertIs<KAngularVelocityUnitInstance>(w2)
        assertEquals(6.0, w1.value, 1e-12)
        assertEquals(6.0, w2.value, 1e-12)
    }

    /** `angularvelocity / angularacceleration = time`. */
    @Test
    fun `angular velocity over angular acceleration is time`() {
        val t: KTimeUnitInstance = (6 of radians / seconds) / (2 of radiansPerSecondSquared)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(3.0, t into seconds, 1e-9)
    }

    /** The typed `angularvelocity / time` and the native `angle / time²` decomposition agree. */
    @Test
    fun `both decompositions agree`() {
        val typed = (6 of radians / seconds) / (3 of seconds)
        val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()
        assertEquals(typed, native)
        assertEquals(typed into radiansPerSecondSquared, native into radiansPerSecondSquared, 1e-12)
    }
}
