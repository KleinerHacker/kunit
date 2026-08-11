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

package org.pcsoft.framework.kunit.thermo.concentration

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KConcentrationUnitInstance` surface: round-trip, equality, `toString`, operators, `toConcentration`. */
class KConcentrationUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1000.0, (1 of molesPerLiter) into molesPerCubicMeter, 1e-9)
        assertEquals(1.0, (1000 of molesPerCubicMeter) into molesPerLiter, 1e-12)
        assertEquals(1000.0, (1 of molesPerLiter) into millimolesPerLiter, 1e-9)
        assertEquals(5.5, (5.5 of millimolesPerLiter) into millimolesPerLiter, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of molesPerLiter, 1000 of molesPerCubicMeter)
        assertEquals((1 of molesPerLiter).hashCode(), (1000 of molesPerCubicMeter).hashCode())
        assertFalse((1 of molesPerLiter) == (2 of molesPerLiter))
        assertFalse((1 of molesPerLiter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 mol/m^3", (1 of molesPerLiter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of molesPerLiter
        val b = 4 of molesPerLiter
        assertEquals(14.0, (a + b) into molesPerLiter, 1e-9)
        assertEquals(6.0, (a - b) into molesPerLiter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical substance·distance⁻³ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toConcentration round-trip and failure`() {
        val raw = (1 of moles).toUnit() / ((1 of meters).toUnit() pow 3)
        assertEquals(1.0, raw.toConcentration() into molesPerCubicMeter, 1e-9)

        // An equivalent expression in decimeters (= liters) reduces onto the same normal form.
        val rawLiter = (1 of moles).toUnit() / ((1 of deci.meters).toUnit() pow 3)
        assertEquals(1.0, rawLiter.toConcentration() into molesPerLiter, 1e-9)

        val m = (1 of meters).toUnit()
        assertFailsWith<IllegalStateException> { (1 of moles).toUnit().toConcentration() }
        assertFailsWith<IllegalStateException> { ((1 of moles).toUnit() / m).toConcentration() }
        assertFailsWith<IllegalStateException> { ((1 of moles).toUnit() / (m pow 2)).toConcentration() }
    }
}
