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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KInertiaUnitInstance` surface: construction, equality, `toString`, operators, `toInertia`. */
class KInertiaUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(18.0, (18 of kilogramMetersSquared) into kilogramMetersSquared, 1e-9)
        assertEquals(1.8e8, (18 of kilogramMetersSquared) into gramCentimetersSquared, 1.0)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1000 of kilogramMetersSquared, 1 of kilo.kilogramMetersSquared)
        assertEquals((1000 of kilogramMetersSquared).hashCode(), (1 of kilo.kilogramMetersSquared).hashCode())
        assertEquals(1.0, (1.0e7 of gramCentimetersSquared) into kilogramMetersSquared, 1e-9)
        assertFalse((1 of kilogramMetersSquared) == (2 of kilogramMetersSquared))
        assertFalse((1 of kilogramMetersSquared).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("18.0 kg*m^2", (18 of kilogramMetersSquared).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of kilogramMetersSquared
        val b = 4 of kilogramMetersSquared
        assertEquals(14.0, (a + b) into kilogramMetersSquared, 1e-9)
        assertEquals(6.0, (a - b) into kilogramMetersSquared, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toInertia from the native form and failure`() {
        val native = (2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)
        assertEquals(18.0, native.toInertia() into kilogramMetersSquared, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toInertia() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * (1 of meters).toUnit()).toInertia()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() * ((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toInertia()
        }
    }
}
