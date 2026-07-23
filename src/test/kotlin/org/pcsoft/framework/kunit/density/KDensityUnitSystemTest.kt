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

package org.pcsoft.framework.kunit.density

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KDensityUnitInstance` surface: construction via expressions, `into`, equality, `toString`, operators. */
class KDensityUnitSystemTest {

    /** A density built as `mass / volume` reads back through an expression `into` target. */
    @Test
    fun `construction and round-trip`() {
        val d = (6 of kilo.grams) / (2 of liters) // 3 kg/L = 3000 kg/m³
        assertEquals(3000.0, d into (kilo.grams / (meters pow 3)), 1e-6)
        assertEquals(3.0, (d into (kilo.grams / (centi.meters pow 3))) * 1000.0, 1e-6)
    }

    /** `of` on a density template scales it (covers scaledBy). */
    @Test
    fun `of scales density`() {
        val d = (2 of kilo.grams) / (1 of liters) // 2000 kg/m³
        val scaled = 3 of d
        assertEquals(6000.0, scaled into (kilo.grams / (meters pow 3)), 1e-6)
    }

    /** Equality/hash by normalized component value. */
    @Test
    fun `equals and hashCode`() {
        val a = (2 of kilo.grams) / (1 of liters)
        val b = (4 of kilo.grams) / (2 of liters)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertFalse(a == (1 of kilo.grams) / (1 of liters))
        assertFalse(a.equals(1.0))
    }

    /** `toString` renders the value in kg/m³. */
    @Test
    fun `toString base unit`() {
        val d = (1 of kilo.grams) / (1 of liters) // 1000 kg/m³
        assertEquals("1000.0 kg/m³", d.toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val d1 = (3 of kilo.grams) / (1 of liters) // 3000 kg/m³
        val d2 = (1 of kilo.grams) / (1 of liters) // 1000 kg/m³
        assertEquals(2000.0, (d1 - d2) into (kilo.grams / (meters pow 3)), 1e-6)
        assertEquals(4000.0, (d1 + d2) into (kilo.grams / (meters pow 3)), 1e-6)
        assertTrue(d1 > d2)
        assertIs<KMixedUnitInstance>(d1 * d2)
        assertIs<KMixedUnitInstance>(d1 / d2)
    }

    /** A mass·length⁻³ mixed unit converts back to a pure density; a wrong shape fails. */
    @Test
    fun `toDensity round-trip and failure`() {
        val raw = (1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3) // [GRAM^1, METER^-3], 1000 g/m³
        assertEquals(1.0, raw.toDensity() into (kilo.grams / (meters pow 3)), 1e-9) // 1 kg/m³
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toDensity() }
        assertFailsWith<IllegalStateException> { ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toDensity() }
        // two terms, but exactly one term carries a wrong exponent for its group
        val g = (1000 of grams).toUnit()
        val m = (1 of meters).toUnit()
        assertFailsWith<IllegalStateException> { ((g pow 2) / (m pow 3)).toDensity() } // mass^2
        assertFailsWith<IllegalStateException> { (g / m).toDensity() }                 // length^-1
    }

    /** `format` a density into base dimensions kg/m^3. */
    @Test
    fun `format compositions`() {
        val d = (1 of kilo.grams) / (1 of liters)
        assertEquals("1000.0 kg/m^3", d format (kilo.grams / (meters pow 3)))
    }
}
