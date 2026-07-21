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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KForceUnitInstance` surface: `of`/`into` round-trip, equality, `toString`, same-type operators, conversions. */
class KForceUnitSystemTest {

    /** `n of token` normalizes to the component base and round-trips through `into`. */
    @Test
    fun `construction and round-trip`() {
        assertEquals(10.0, (10 of newtons) into newtons, 1e-9)
        assertEquals(10.0 * N_IN_BASE, (10 of newtons).value, 1e-6) // raw component value g·m·s⁻²
        assertEquals(1.0, (1 of poundsForce) into poundsForce, 1e-9)
        assertEquals(9.80665, (1 of kilo.ponds) into newtons, 1e-9) // 1 kp = 1 kgf = 9.80665 N
    }

    /** Equality/hash by normalized component value (`1 kN == 1000 N`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.newtons, 1000 of newtons)
        assertEquals((1 of kilo.newtons).hashCode(), (1000 of newtons).hashCode())
        assertFalse((1 of newtons) == (2 of newtons))
        assertFalse((1 of newtons).equals(1.0))
    }

    /** `toString` renders the value in newtons. */
    @Test
    fun `toString base unit`() {
        assertEquals("10.0 N", (10 of newtons).toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val f1 = 10 of newtons
        val f2 = 4 of newtons
        assertEquals(6.0, (f1 - f2) into newtons, 1e-9)
        assertEquals(14.0, (f1 + f2) into newtons, 1e-9)
        assertTrue(f1 > f2)
        assertIs<KMixedUnitInstance>(f1 * f2)
        assertIs<KMixedUnitInstance>(f1 / f2)
    }

    /** A mass·length·time⁻² mixed unit converts back to a pure force; a wrong shape fails. */
    @Test
    fun `toForce round-trip and failure`() {
        val raw = (1000 of grams).toUnit() * (1 of meters).toUnit() / ((1 of seconds).toUnit() pow 2)
        assertEquals(1.0, raw.toForce() into newtons, 1e-9) // 1 kg·m/s² = 1 N
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toForce() }
        assertFailsWith<IllegalStateException> { ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toForce() }
        // three terms, but exactly one term carries a wrong exponent for its group
        val g = (1000 of grams).toUnit()
        val m = (1 of meters).toUnit()
        val s2 = (1 of seconds).toUnit() pow 2
        assertFailsWith<IllegalStateException> { ((g pow 2) * m / s2).toForce() }        // mass^2
        assertFailsWith<IllegalStateException> { (g * (m pow 2) / s2).toForce() }         // length^2
        assertFailsWith<IllegalStateException> { (g * m / ((1 of seconds).toUnit() pow 3)).toForce() } // time^-3
    }
}
