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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.KMolarMassUnitInstance
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group molality operators and the typed/native decomposition equivalence. */
class KMolalityOperatorTest {

    /** `amountOfSubstance / mass = molality`. */
    @Test
    fun `amount of substance over mass is molality`() {
        val b = (0.5 of moles) / (2 of kilo.grams)
        assertIs<KMolalityUnitInstance>(b)
        assertEquals(0.25, b into molesPerKilogram, 1e-9)
    }

    /** `molality * mass = amountOfSubstance` and its commutative counterpart. */
    @Test
    fun `molality times mass is amount of substance`() {
        val b = 0.25 of molesPerKilogram
        val m = 2 of kilo.grams
        val n1 = b * m
        val n2 = m * b
        assertIs<KAmountOfSubstanceUnitInstance>(n1)
        assertIs<KAmountOfSubstanceUnitInstance>(n2)
        assertEquals(0.5, n1 into moles, 1e-9)
        assertEquals(0.5, n2 into moles, 1e-9)
    }

    /** `amountOfSubstance / molality = mass`. */
    @Test
    fun `amount of substance over molality is mass`() {
        val m = (0.5 of moles) / (0.25 of molesPerKilogram)
        assertIs<KMassUnitInstance>(m)
        assertEquals(2.0, m into kilo.grams, 1e-9)
    }

    /** `1 / molarMass = molality` - one kilogram of water holds ≈ 55.5 mol. */
    @Test
    fun `reciprocal of molar mass is molality`() {
        val b = 1 / (18.015 of gramsPerMole)
        assertIs<KMolalityUnitInstance>(b)
        assertEquals(55.5093, b into molesPerKilogram, 1e-4)
    }

    /** `1 / molality = molarMass` - the inverse direction. */
    @Test
    fun `reciprocal of molality is molar mass`() {
        val m = 1 / (55.5093 of molesPerKilogram)
        assertIs<KMolarMassUnitInstance>(m)
        assertEquals(18.015, m into gramsPerMole, 1e-4)
    }

    /** The typed operator and the native `mol·kg⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (0.5 of moles) / (2 of kilo.grams)
        val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()
        assertIs<KMolalityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into molesPerKilogram, native into molesPerKilogram, 1e-9)
    }
}
