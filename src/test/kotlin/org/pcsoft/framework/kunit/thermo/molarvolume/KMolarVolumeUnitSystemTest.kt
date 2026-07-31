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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KMolarVolumeUnitInstance` surface: round-trip, equality, `toString`, operators, `toMolarVolume`. */
class KMolarVolumeUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(0.0224, (22.4 of litersPerMole) into cubicMetersPerMole, 1e-12)
        assertEquals(22.4, (0.0224 of cubicMetersPerMole) into litersPerMole, 1e-9)
        assertEquals(1000.0, (1 of litersPerMole) into cubicCentimetersPerMole, 1e-6)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of litersPerMole, 1000 of cubicCentimetersPerMole)
        assertEquals((1 of litersPerMole).hashCode(), (1000 of cubicCentimetersPerMole).hashCode())
        assertFalse((1 of litersPerMole) == (2 of litersPerMole))
        assertFalse((1 of litersPerMole).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.001 m^3/mol", (1 of litersPerMole).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of litersPerMole
        val b = 4 of litersPerMole
        assertEquals(14.0, (a + b) into litersPerMole, 1e-9)
        assertEquals(6.0, (a - b) into litersPerMole, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance³·substance⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toMolarVolume round-trip and failure`() {
        val raw = ((1 of meters).toUnit() pow 3) / (1 of moles).toUnit()
        assertEquals(1.0, raw.toMolarVolume() into cubicMetersPerMole, 1e-9)
        val rawCm = ((1 of centi.meters).toUnit() pow 3) / (1 of moles).toUnit()
        assertEquals(1.0, rawCm.toMolarVolume() into cubicCentimetersPerMole, 1e-9)

        val mol = (1 of moles).toUnit()
        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toMolarVolume() }
        assertFailsWith<IllegalStateException> { (((1 of meters).toUnit() pow 2) / mol).toMolarVolume() }
        assertFailsWith<IllegalStateException> { (((1 of meters).toUnit() pow 3) / (mol pow 2)).toMolarVolume() }
    }
}
