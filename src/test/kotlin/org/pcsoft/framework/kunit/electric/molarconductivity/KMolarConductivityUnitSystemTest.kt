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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KMolarConductivityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KMolarConductivityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of siemensSquareMetersPerMole) into siemensSquareMetersPerMole, 1e-12)
        assertEquals(
            10_000.0,
            (1 of siemensSquareMetersPerMole) into siemensSquareCentimetersPerMole,
            1e-6,
        )
        assertEquals(
            1.0,
            (10_000 of siemensSquareCentimetersPerMole) into siemensSquareMetersPerMole,
            1e-9,
        )
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of siemensSquareMetersPerMole, 10_000 of siemensSquareCentimetersPerMole)
        assertEquals(
            (1 of siemensSquareMetersPerMole).hashCode(),
            (10_000 of siemensSquareCentimetersPerMole).hashCode(),
        )
        assertFalse((1 of siemensSquareMetersPerMole) == (2 of siemensSquareMetersPerMole))
        assertFalse((1 of siemensSquareMetersPerMole).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.0126 S*m^2/mol", (0.0126 of siemensSquareMetersPerMole).toString())
    }

    /** Kohlrausch's law of independent ion migration is exactly the same-type `+`. */
    @Test
    fun `same-type operators`() {
        val cation = 5.011 of milli.siemensSquareMetersPerMole
        val anion = 7.635 of milli.siemensSquareMetersPerMole
        assertEquals(12.646, (cation + anion) into milli.siemensSquareMetersPerMole, 1e-9)
        assertEquals(2.624, (anion - cation) into milli.siemensSquareMetersPerMole, 1e-9)
        assertTrue(anion > cation)
        assertIs<KMixedUnitInstance>(cation * anion)
        assertIs<KMixedUnitInstance>(cation / anion)
    }

    /**
     * The native `kg⁻¹·s³·A²·mol⁻¹` form converts back. The expression is assembled from unit templates, so
     * the raw mixed value is the gram-based product the bridge expects.
     */
    @Test
    fun `toMolarConductivity round-trip and failure`() {
        val raw = 1 of (seconds pow 3) * (amperes.toUnit() pow 2) /
                kilo.grams.toUnit() / moles.toUnit()
        assertEquals(1.0, raw.toMolarConductivity() into siemensSquareMetersPerMole, 1e-9)

        // The same expression on plain grams is 1000 times larger.
        val rawGram = 1 of (seconds pow 3) * (amperes.toUnit() pow 2) /
                grams.toUnit() / moles.toUnit()
        assertEquals(1000.0, rawGram.toMolarConductivity() into siemensSquareMetersPerMole, 1e-6)

        assertFailsWith<IllegalStateException> { (1 of moles).toUnit().toMolarConductivity() }
        assertFailsWith<IllegalStateException> {
            (1 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit() /
                    (1 of meters).toUnit()).toMolarConductivity()
        }
    }
}
