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

package org.pcsoft.framework.kunit.thermo.molarmass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KMolarMassUnitInstance` surface: round-trip, equality, `toString`, operators, `toMolarMass`. */
class KMolarMassUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1000.0, (1 of kilogramsPerMole) into gramsPerMole, 1e-9)
        assertEquals(1.0, (1000 of gramsPerMole) into kilogramsPerMole, 1e-12)
        assertEquals(18.015, (18.015 of gramsPerMole) into gramsPerMole, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilogramsPerMole, 1000 of gramsPerMole)
        assertEquals((1 of kilogramsPerMole).hashCode(), (1000 of gramsPerMole).hashCode())
        assertFalse((1 of gramsPerMole) == (2 of gramsPerMole))
        assertFalse((1 of gramsPerMole).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 g/mol", (1 of kilogramsPerMole).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of gramsPerMole
        val b = 4 of gramsPerMole
        assertEquals(14.0, (a + b) into gramsPerMole, 1e-9)
        assertEquals(6.0, (a - b) into gramsPerMole, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass¹·substance⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toMolarMass round-trip and failure`() {
        val raw = (18.015 of grams).toUnit() / (1 of moles).toUnit()
        assertEquals(18.015, raw.toMolarMass() into gramsPerMole, 1e-9)
        assertEquals(
            18.015,
            ((18.015 of kilo.grams).toUnit() / (1000 of moles).toUnit()).toMolarMass() into gramsPerMole,
            1e-9
        )

        assertFailsWith<IllegalStateException> { (1 of grams).toUnit().toMolarMass() }
        val g = (1 of grams).toUnit()
        val mol = (1 of moles).toUnit()
        assertFailsWith<IllegalStateException> { ((g pow 2) / mol).toMolarMass() }
        assertFailsWith<IllegalStateException> { (g / (mol pow 2)).toMolarMass() }
    }
}
