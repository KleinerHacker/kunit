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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group concentration operators and the typed/native decomposition equivalence. */
class KConcentrationOperatorTest {

    /** `amountOfSubstance / volume = concentration`. */
    @Test
    fun `amount of substance over volume is concentration`() {
        val c = (0.5 of moles) / (2 of liters)
        assertIs<KConcentrationUnitInstance>(c)
        assertEquals(0.25, c into molesPerLiter, 1e-9)
    }

    /** `concentration * volume = amountOfSubstance` and its commutative counterpart. */
    @Test
    fun `concentration times volume is amount of substance`() {
        val c = 0.25 of molesPerLiter
        val v = 2 of liters
        val n1 = c * v
        val n2 = v * c
        assertIs<KAmountOfSubstanceUnitInstance>(n1)
        assertIs<KAmountOfSubstanceUnitInstance>(n2)
        assertEquals(0.5, n1 into moles, 1e-9)
        assertEquals(0.5, n2 into moles, 1e-9)
    }

    /** `amountOfSubstance / concentration = volume`. */
    @Test
    fun `amount of substance over concentration is volume`() {
        val v = (0.5 of moles) / (0.25 of molesPerLiter)
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(2.0, v into liters, 1e-9)
    }

    /** The typed operator and the native `mol·m⁻³` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (0.5 of moles) / (2 of liters)
        val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()
        assertIs<KConcentrationUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into molesPerLiter, native into molesPerLiter, 1e-12)
    }

    /** A clinical blood glucose reading: 5.5 mmol/l in 5 l of blood. */
    @Test
    fun `blood glucose amount`() {
        val n = (5.5 of millimolesPerLiter) * (5 of liters)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(0.0275, n into moles, 1e-9)
    }
}
