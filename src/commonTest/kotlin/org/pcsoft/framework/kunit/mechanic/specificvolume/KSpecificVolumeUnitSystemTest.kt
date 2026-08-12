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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KSpecificVolumeUnitInstance` surface: construction, equality, `toString`, operators, `toSpecificVolume`. */
class KSpecificVolumeUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of cubicMetersPerKilogram) into cubicMetersPerKilogram, 1e-9)
        assertEquals(2000.0, (2 of cubicMetersPerKilogram) into litersPerKilogram, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of litersPerKilogram, 1 of cubicCentimetersPerGram)
        assertEquals((1 of litersPerKilogram).hashCode(), (1 of cubicCentimetersPerGram).hashCode())
        assertEquals(1000 of litersPerKilogram, 1 of kilo.litersPerKilogram)
        assertFalse((1 of litersPerKilogram) == (2 of litersPerKilogram))
        assertFalse((1 of litersPerKilogram).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.0 m^3/kg", (2 of cubicMetersPerKilogram).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of cubicMetersPerKilogram
        val b = 4 of cubicMetersPerKilogram
        assertEquals(14.0, (a + b) into cubicMetersPerKilogram, 1e-9)
        assertEquals(6.0, (a - b) into cubicMetersPerKilogram, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toSpecificVolume round-trip and failure`() {
        val raw = ((1 of meters).toUnit() pow 3) / (500 of grams).toUnit()
        assertEquals(2.0, raw.toSpecificVolume() into cubicMetersPerKilogram, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toSpecificVolume() }
        assertFailsWith<IllegalStateException> {
            ((1 of meters).toUnit() / (1000 of grams).toUnit()).toSpecificVolume()
        }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 3) / (1000 of grams).toUnit() / (1 of seconds).toUnit())
                .toSpecificVolume()
        }
    }
}
