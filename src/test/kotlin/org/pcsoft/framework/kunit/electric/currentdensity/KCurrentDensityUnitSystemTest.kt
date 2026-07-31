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

package org.pcsoft.framework.kunit.electric.currentdensity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import kotlin.test.*

/**
 * `KCurrentDensityUnitInstance` surface: construction via expressions, `into`, equality, `toString`,
 * same-type operators and the `toCurrentDensity()` normal-form hook.
 */
class KCurrentDensityUnitSystemTest {

    /** A current density built as `current / area` reads back through an expression `into` target. */
    @Test
    fun `construction and round-trip`() {
        val j = (10 of amperes) / ((1 of meters) * (2 of meters)) // 5 A/m²
        assertEquals(5.0, j into (amperes / (meters pow 2)), 1e-9)
        assertEquals(5.0e-6, j into (amperes / (milli.meters pow 2)), 1e-15)
        assertEquals(5000.0, j into (milli.amperes / (meters pow 2)), 1e-6)
    }

    /** `of` on a current density template scales it (covers scaledBy). */
    @Test
    fun `of scales current density`() {
        val j = (2 of amperes) / ((1 of meters) * (1 of meters)) // 2 A/m²
        assertEquals(6.0, (3 of j) into (amperes / (meters pow 2)), 1e-9)
    }

    /** Equality/hash by normalized value in A/m². */
    @Test
    fun `equals and hashCode`() {
        val a = (2 of amperes) / ((1 of meters) * (1 of meters))
        val b = (4 of amperes) / ((1 of meters) * (2 of meters))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertFalse(a == (1 of amperes) / ((1 of meters) * (1 of meters)))
        assertFalse(a.equals(1.0))
    }

    /** `toString` renders the value in A/m². */
    @Test
    fun `toString base unit`() {
        val j = (5 of amperes) / ((1 of meters) * (1 of meters))
        assertEquals("5.0 A/m²", j.toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val a = (3 of amperes) / ((1 of meters) * (1 of meters)) // 3 A/m²
        val b = (1 of amperes) / ((1 of meters) * (1 of meters)) // 1 A/m²
        assertEquals(4.0, (a + b) into (amperes / (meters pow 2)), 1e-9)
        assertEquals(2.0, (a - b) into (amperes / (meters pow 2)), 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A `current¹·distance⁻²` mixed unit converts back to a pure current density; wrong shapes fail. */
    @Test
    fun `toCurrentDensity round-trip and failure`() {
        val raw = (6 of amperes).toUnit() / ((1 of meters).toUnit() pow 2) // [A^1, m^-2], 6 A/m²
        assertEquals(6.0, raw.toCurrentDensity() into (amperes / (meters pow 2)), 1e-9)
        assertEquals((6 of amperes) / ((1 of meters) * (1 of meters)), raw.toCurrentDensity())
        // a plain current is not a current density
        assertFailsWith<IllegalStateException> { (1 of amperes).toUnit().toCurrentDensity() }
        // wrong distance exponent (-3 instead of -2)
        assertFailsWith<IllegalStateException> {
            ((6 of amperes).toUnit() / ((1 of meters).toUnit() pow 3)).toCurrentDensity()
        }
    }
}
