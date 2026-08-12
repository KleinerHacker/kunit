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

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group molar energy operators and the equivalence of all decompositions. */
class KMolarEnergyOperatorTest {

    /** `energy / amountOfSubstance = molar energy`. */
    @Test
    fun `energy over amount of substance is molar energy`() {
        val dH = (572 of kilo.joules) / (2 of moles)
        assertIs<KMolarEnergyUnitInstance>(dH)
        assertEquals(286000.0, dH into joulesPerMole, 1e-6)
    }

    /** `molar energy * amountOfSubstance = energy` and its commutative counterpart. */
    @Test
    fun `molar energy times amount of substance is energy`() {
        val dH = 286 of kilo.joulesPerMole
        val n = 2 of moles
        val e1 = dH * n
        val e2 = n * dH
        assertIs<KEnergyUnitInstance>(e1)
        assertIs<KEnergyUnitInstance>(e2)
        assertEquals(572000.0, e1 into joules, 1e-6)
        assertEquals(572000.0, e2 into joules, 1e-6)
    }

    /** `energy / molar energy = amountOfSubstance`. */
    @Test
    fun `energy over molar energy is amount of substance`() {
        val n = (572 of kilo.joules) / (286 of kilo.joulesPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(2.0, n into moles, 1e-9)
    }

    /**
     * Both decompositions - the typed `energy / amountOfSubstance` operator and the native
     * `mass·distance²·time⁻²·substance⁻¹` expression through `toMolarEnergy()` - yield the same typed,
     * value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (1 of joules) / (1 of moles)
        val native = (
                (1000 of grams).toUnit() *
                        ((1 of meters).toUnit() pow 2) /
                        ((1 of seconds).toUnit() pow 2) /
                        (1 of moles).toUnit()
                ).toMolarEnergy()
        assertIs<KMolarEnergyUnitInstance>(typed)
        assertIs<KMolarEnergyUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into joulesPerMole, 1e-9)
    }
}
