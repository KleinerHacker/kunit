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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KAngularAccelerationUnitInstance` surface: construction, equality, `toString`, operators, `toAngularAcceleration`. */
class KAngularAccelerationUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of radiansPerSecondSquared) into radiansPerSecondSquared, 1e-12)
        assertEquals(1.0, (60 of revolutionsPerMinutePerSecond) into revolutionsPerSecondSquared, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(60 of revolutionsPerMinutePerSecond, 1 of revolutionsPerSecondSquared)
        assertEquals(
            (60 of revolutionsPerMinutePerSecond).hashCode(),
            (1 of revolutionsPerSecondSquared).hashCode(),
        )
        assertFalse((1 of radiansPerSecondSquared) == (2 of radiansPerSecondSquared))
        assertFalse((1 of radiansPerSecondSquared).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 rad/s^2", (2 of radiansPerSecondSquared).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of radiansPerSecondSquared
        val b = 4 of radiansPerSecondSquared
        assertEquals(14.0, (a + b) into radiansPerSecondSquared, 1e-12)
        assertEquals(6.0, (a - b) into radiansPerSecondSquared, 1e-12)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toAngularAcceleration from the native form and failure`() {
        val native = (2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)
        assertEquals(2.0, native.toAngularAcceleration() into radiansPerSecondSquared, 1e-12)
        assertFailsWith<IllegalStateException> { (1 of radians).toUnit().toAngularAcceleration() }
        assertFailsWith<IllegalStateException> {
            ((1 of radians).toUnit() / (1 of seconds).toUnit()).toAngularAcceleration()
        }
        assertFailsWith<IllegalStateException> {
            ((1 of meters).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()
        }
    }
}
