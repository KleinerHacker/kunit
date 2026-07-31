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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group molar mass operators and the equivalence of all decompositions. */
class KMolarMassOperatorTest {

    /** `mass / amountOfSubstance = molar mass`. */
    @Test
    fun `mass over amount of substance is molar mass`() {
        val m = (36.03 of grams) / (2 of moles)
        assertIs<KMolarMassUnitInstance>(m)
        assertEquals(18.015, m into gramsPerMole, 1e-9)
    }

    /** `molar mass * amountOfSubstance = mass` and its commutative counterpart. */
    @Test
    fun `molar mass times amount of substance is mass`() {
        val m = 18.015 of gramsPerMole
        val n = 2 of moles
        val m1 = m * n
        val m2 = n * m
        assertIs<KMassUnitInstance>(m1)
        assertIs<KMassUnitInstance>(m2)
        assertEquals(36.03, m1 into grams, 1e-9)
        assertEquals(36.03, m2 into grams, 1e-9)
    }

    /** `mass / molar mass = amountOfSubstance`. */
    @Test
    fun `mass over molar mass is amount of substance`() {
        val n = (36.03 of grams) / (18.015 of gramsPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(2.0, n into moles, 1e-9)
    }

    /**
     * Both decompositions - the typed `mass / amountOfSubstance` operator and the native `mass¹·substance⁻¹`
     * expression through `toMolarMass()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (18.015 of grams) / (1 of moles)
        val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()
        assertIs<KMolarMassUnitInstance>(typed)
        assertIs<KMolarMassUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(18.015, typed into gramsPerMole, 1e-9)
    }
}
