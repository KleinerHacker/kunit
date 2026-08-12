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

package org.pcsoft.framework.kunit.thermo.molarenergy

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KMolarEnergyUnitInstance` surface: round-trip, equality, `toString`, operators, `toMolarEnergy`. */
class KMolarEnergyUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(286000.0, (286 of kilo.joulesPerMole) into joulesPerMole, 1e-6)
        assertEquals(286.0, (286000 of joulesPerMole) into kilo.joulesPerMole, 1e-9)
        assertEquals(1000.0, (4184 of joulesPerMole) into caloriesPerMole, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joulesPerMole, 1000 of joulesPerMole)
        assertEquals((1 of kilo.joulesPerMole).hashCode(), (1000 of joulesPerMole).hashCode())
        assertFalse((1 of joulesPerMole) == (2 of joulesPerMole))
        assertFalse((1 of joulesPerMole).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("286000.0 J/mol", (286 of kilo.joulesPerMole).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerMole
        val b = 4 of joulesPerMole
        assertEquals(6.0, (a - b) into joulesPerMole, 1e-9)
        assertEquals(14.0, (a + b) into joulesPerMole, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·distance²·time⁻²·substance⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toMolarEnergy round-trip and failure`() {
        val mol = (1 of moles).toUnit()
        val raw = (1000 of grams).toUnit() *
                ((1 of meters).toUnit() pow 2) /
                ((1 of seconds).toUnit() pow 2) /
                mol
        assertEquals(1.0, raw.toMolarEnergy() into joulesPerMole, 1e-9)

        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toMolarEnergy() }
        val g = (1000 of grams).toUnit()
        val m2 = (1 of meters).toUnit() pow 2
        val s2 = (1 of seconds).toUnit() pow 2
        assertFailsWith<IllegalStateException> { ((g pow 2) * m2 / s2 / mol).toMolarEnergy() }
        assertFailsWith<IllegalStateException> { (g * ((1 of meters).toUnit() pow 3) / s2 / mol).toMolarEnergy() }
        assertFailsWith<IllegalStateException> { (g * m2 / ((1 of seconds).toUnit() pow 3) / mol).toMolarEnergy() }
        assertFailsWith<IllegalStateException> { (g * m2 / s2 / (mol pow 2)).toMolarEnergy() }
    }
}
