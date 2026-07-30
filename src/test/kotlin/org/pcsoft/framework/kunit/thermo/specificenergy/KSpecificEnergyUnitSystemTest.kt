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

package org.pcsoft.framework.kunit.thermo.specificenergy

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KSpecificEnergyUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KSpecificEnergyUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(334000.0, (334 of kilo.joulesPerKilogram) into joulesPerKilogram, 1e-6)
        assertEquals(334.0, (334000 of joulesPerKilogram) into kilo.joulesPerKilogram, 1e-9)
        assertEquals(1.0, (4184 of joulesPerKilogram) into caloriesPerGram, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joulesPerKilogram, 1000 of joulesPerKilogram)
        assertEquals((1 of kilo.joulesPerKilogram).hashCode(), (1000 of joulesPerKilogram).hashCode())
        assertFalse((1 of joulesPerKilogram) == (2 of joulesPerKilogram))
        assertFalse((1 of joulesPerKilogram).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("334000.0 J/kg", (334 of kilo.joulesPerKilogram).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerKilogram
        val b = 4 of joulesPerKilogram
        assertEquals(6.0, (a - b) into joulesPerKilogram, 1e-9)
        assertEquals(14.0, (a + b) into joulesPerKilogram, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance²·time⁻² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toSpecificEnergy round-trip and failure`() {
        val raw = ((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)
        assertEquals(1.0, raw.toSpecificEnergy() into joulesPerKilogram, 1e-9) // 1 m²/s² = 1 J/kg

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toSpecificEnergy() }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 3) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()
        }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 3)).toSpecificEnergy()
        }
    }
}
