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

package org.pcsoft.framework.kunit.thermo.amountofsubstance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** `KAmountOfSubstanceUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KAmountOfSubstanceUnitSystemTest {

    /** `n of token` round-trips through `into` and normalizes to moles. */
    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of moles) into moles, 1e-12)
        assertEquals(2000.0, (2 of moles) into milli.moles, 1e-9)
        assertEquals(2.0, (2000 of milli.moles) into moles, 1e-12)
    }

    /** Equality/hash by normalized value (`1 mol == 1000 mmol`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of moles, 1000 of milli.moles)
        assertEquals((1 of moles).hashCode(), (1000 of milli.moles).hashCode())
        assertFalse((1 of moles) == (2 of moles))
        assertFalse((1 of moles).equals(1.0))
    }

    /** `toString` renders the value in moles. */
    @Test
    fun `toString base unit`() {
        assertEquals("2.0 mol", (2 of moles).toString())
    }

    /** Same-type `+`/`-`/comparison. */
    @Test
    fun `same-type operators`() {
        val a = 10 of moles
        val b = 4 of moles
        assertEquals(6.0, (a - b) into moles, 1e-12)
        assertEquals(14.0, (a + b) into moles, 1e-12)
        assertTrue(a > b)
        assertEquals(1.5, ((1 of moles) + (500 of milli.moles)) into moles, 1e-12)
    }

    /** A single mole term converts back to a pure amount; a wrong shape fails. */
    @Test
    fun `toAmountOfSubstance round-trip and failure`() {
        val raw = (2 of kilo.moles).toUnit()
        assertEquals(2000.0, raw.toAmountOfSubstance() into moles, 1e-9)
        // A mole² term is still a single amount-of-substance term and stays convertible (like mass/time).
        assertEquals(4.0, ((2 of moles).toUnit() * (2 of moles).toUnit()).toAmountOfSubstance() into moles, 1e-12)
        // Two terms of different groups are not a pure amount of substance.
        assertFailsWith<IllegalStateException> {
            ((2 of moles).toUnit() * (2 of grams).toUnit()).toAmountOfSubstance()
        }
    }
}
