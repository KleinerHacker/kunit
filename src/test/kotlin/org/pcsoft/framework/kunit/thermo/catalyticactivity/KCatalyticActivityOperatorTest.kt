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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group catalytic activity operators and the typed/native decomposition equivalence. */
class KCatalyticActivityOperatorTest {

    /** `amountOfSubstance / time = catalytic activity`. */
    @Test
    fun `amount of substance over time is catalytic activity`() {
        val a = (0.5 of milli.moles) / (10 of seconds)
        assertIs<KCatalyticActivityUnitInstance>(a)
        assertEquals(50.0, a into micro.katals, 1e-9)
    }

    /** `catalytic activity * time = amountOfSubstance` and its commutative counterpart. */
    @Test
    fun `catalytic activity times time is amount of substance`() {
        val a = 50 of micro.katals
        val t = 10 of seconds
        val n1 = a * t
        val n2 = t * a
        assertIs<KAmountOfSubstanceUnitInstance>(n1)
        assertIs<KAmountOfSubstanceUnitInstance>(n2)
        assertEquals(0.5, n1 into milli.moles, 1e-9)
        assertEquals(0.5, n2 into milli.moles, 1e-9)
    }

    /** `amountOfSubstance / catalytic activity = time`. */
    @Test
    fun `amount of substance over catalytic activity is time`() {
        val t = (0.5 of milli.moles) / (50 of micro.katals)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(10.0, t into seconds, 1e-9)
    }

    /** The enzyme unit is exactly one micromole per minute. */
    @Test
    fun `enzyme unit definition`() {
        val a = (1 of micro.moles) / (1 of minutes)
        assertIs<KCatalyticActivityUnitInstance>(a)
        assertEquals(1.0, a into enzymeUnits, 1e-9)
    }

    /** The typed operator and the native `mol·s⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (2 of moles) / (4 of seconds)
        val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()
        assertIs<KCatalyticActivityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into katals, native into katals, 1e-12)
    }
}
