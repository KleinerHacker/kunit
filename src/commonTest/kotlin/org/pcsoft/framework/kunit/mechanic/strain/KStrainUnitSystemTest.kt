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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KStrainUnitInstance` surface: construction, equality, `toString`, operators, `toStrain`. */
class KStrainUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of percent) into percent, 1e-12)
        assertEquals(0.02, (2 of percent) into ratio, 1e-15)
        assertEquals(20.0, (2 of percent) into perMille, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of percent, 10 of perMille)
        assertEquals((1 of percent).hashCode(), (10 of perMille).hashCode())
        assertEquals(1 of microstrain, 1 of milli.perMille)
        assertFalse((1 of percent) == (2 of percent))
        assertFalse((1 of percent).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.002 1", (2 of perMille).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 3 of perMille
        val b = 1 of perMille
        assertEquals(4.0, (a + b) into perMille, 1e-12)
        assertEquals(2.0, (a - b) into perMille, 1e-12)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** The native form `length / length` is dimensionless and converts through `toStrain`. */
    @Test
    fun `toStrain from the native form and failure`() {
        val e = ((2 of milli.meters) / (1 of meters)).toStrain()
        assertEquals(2.0, e into perMille, 1e-12)
        assertEquals(0.002, e into ratio, 1e-15)
        assertEquals(1.0, (1 of percent).toUnit().toStrain() into percent, 1e-12)
        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toStrain() }
        assertFailsWith<IllegalStateException> {
            ((1 of percent).toUnit() * (1 of seconds).toUnit()).toStrain()
        }
    }
}
