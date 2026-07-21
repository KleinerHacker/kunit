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

package org.pcsoft.framework.kunit.areadensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KAreaDensityUnitInstance` surface: construction via expressions, `into`, equality, `toString`, operators. */
class KAreaDensityUnitSystemTest {

    private val area = (5 of meters) * (1 of meters) // 5 m²

    /** An area density built as `mass / area` reads back through an expression `into` target. */
    @Test
    fun `construction and round-trip`() {
        val q = (25 of kilo.grams) / area // 5 kg/m²
        assertEquals(5.0, q into (kilo.grams / (meters pow 2)), 1e-9)
        assertEquals(5.0, (q into (grams / (milli.meters pow 2))) * 1000.0, 1e-6) // 5 g/mm² → per m² scaling
    }

    /** `of` on an area-density template scales it (covers scaledBy). */
    @Test
    fun `of scales area density`() {
        val q = (5 of kilo.grams) / area // 1 kg/m²
        assertEquals(3.0, (3 of q) into (kilo.grams / (meters pow 2)), 1e-9)
    }

    /** Equality/hash by normalized component value. */
    @Test
    fun `equals and hashCode`() {
        val a = (10 of kilo.grams) / area // 2 kg/m²
        val b = (20 of kilo.grams) / ((10 of meters) * (1 of meters)) // 2 kg/m²
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertFalse(a == (5 of kilo.grams) / area)
        assertFalse(a.equals(1.0))
    }

    /** `toString` renders the value in kg/m². */
    @Test
    fun `toString base unit`() {
        val q = (5 of kilo.grams) / area // 1 kg/m²
        assertEquals("1.0 kg/m²", q.toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val q1 = (15 of kilo.grams) / area // 3 kg/m²
        val q2 = (5 of kilo.grams) / area  // 1 kg/m²
        assertEquals(2.0, (q1 - q2) into (kilo.grams / (meters pow 2)), 1e-9)
        assertEquals(4.0, (q1 + q2) into (kilo.grams / (meters pow 2)), 1e-9)
        assertTrue(q1 > q2)
        assertIs<KMixedUnitInstance>(q1 * q2)
        assertIs<KMixedUnitInstance>(q1 / q2)
    }

    /** A mass·length⁻² mixed unit converts back to a pure area density; a wrong shape fails. */
    @Test
    fun `toAreaDensity round-trip and failure`() {
        val raw = (1000 of grams).toUnit() / ((1 of meters).toUnit() pow 2) // [GRAM^1, METER^-2], 1000 g/m²
        assertEquals(1.0, raw.toAreaDensity() into (kilo.grams / (meters pow 2)), 1e-9) // 1 kg/m²
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toAreaDensity() }
        assertFailsWith<IllegalStateException> { ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toAreaDensity() }
        // two terms, but exactly one term carries a wrong exponent for its group
        val g = (1000 of grams).toUnit()
        val m = (1 of meters).toUnit()
        assertFailsWith<IllegalStateException> { ((g pow 2) / (m pow 2)).toAreaDensity() } // mass^2
        assertFailsWith<IllegalStateException> { (g / m).toAreaDensity() }                  // length^-1
    }
}
