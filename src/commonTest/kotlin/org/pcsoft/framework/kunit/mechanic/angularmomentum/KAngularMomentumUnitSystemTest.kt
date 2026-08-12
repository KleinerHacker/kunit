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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KAngularMomentumUnitInstance` surface: construction, equality, `toString`, operators, `toAngularMomentum`. */
class KAngularMomentumUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(6.0, (6 of kilogramMetersSquaredPerSecond) into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(6.0, (6 of kilogramMetersSquaredPerSecond) into jouleSeconds, 1e-9)
        assertEquals(6.0, (6 of kilogramMetersSquaredPerSecond) into newtonMeterSeconds, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of jouleSeconds, 1 of newtonMeterSeconds)
        assertEquals((1 of jouleSeconds).hashCode(), (1 of newtonMeterSeconds).hashCode())
        assertEquals(1000 of jouleSeconds, 1 of kilo.jouleSeconds)
        assertFalse((1 of jouleSeconds) == (2 of jouleSeconds))
        assertFalse((1 of jouleSeconds).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("6.0 kg*m^2/s", (6 of kilogramMetersSquaredPerSecond).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of jouleSeconds
        val b = 4 of jouleSeconds
        assertEquals(14.0, (a + b) into jouleSeconds, 1e-9)
        assertEquals(6.0, (a - b) into jouleSeconds, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toAngularMomentum from the native form and failure`() {
        val native = (2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()
        assertEquals(18.0, native.toAngularMomentum() into kilogramMetersSquaredPerSecond, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toAngularMomentum() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * ((1 of meters).toUnit() pow 2)).toAngularMomentum()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * (1 of meters).toUnit() / (1 of seconds).toUnit()).toAngularMomentum()
        }
    }
}
