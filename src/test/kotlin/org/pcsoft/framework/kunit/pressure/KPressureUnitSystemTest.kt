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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KPressureUnitInstance` surface: `of`/`into` round-trip, equality, `toString`, same-type operators, conversions. */
class KPressureUnitSystemTest {

    /** `n of token` round-trips through `into` and normalizes to pascals. */
    @Test
    fun `construction and round-trip`() {
        assertEquals(2.0, (2 of bars) into bars, 1e-9)
        assertEquals(200000.0, (2 of bars) into pascals, 1e-6)
        assertEquals(1.0, (1 of atmospheres) into atmospheres, 1e-9)
    }

    /** Equality/hash by normalized component value (`1 kPa == 1000 Pa`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.pascals, 1000 of pascals)
        assertEquals((1 of kilo.pascals).hashCode(), (1000 of pascals).hashCode())
        assertFalse((1 of pascals) == (2 of pascals))
        assertFalse((1 of pascals).equals(1.0))
    }

    /** `toString` renders the value in pascals. */
    @Test
    fun `toString base unit`() {
        assertEquals("50.0 Pa", (50 of pascals).toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val p1 = 10 of pascals
        val p2 = 4 of pascals
        assertEquals(6.0, (p1 - p2) into pascals, 1e-9)
        assertEquals(14.0, (p1 + p2) into pascals, 1e-9)
        assertTrue(p1 > p2)
        assertIs<KMixedUnitInstance>(p1 * p2)
        assertIs<KMixedUnitInstance>(p1 / p2)
    }

    /** A mass·length⁻¹·time⁻² mixed unit converts back to a pure pressure; a wrong shape fails. */
    @Test
    fun `toPressure round-trip and failure`() {
        val raw = (1000 of grams).toUnit() / (1 of meters).toUnit() / ((1 of seconds).toUnit() pow 2)
        assertEquals(1.0, raw.toPressure() into pascals, 1e-9) // 1 kg/(m·s²) = 1 Pa
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toPressure() }
        assertFailsWith<IllegalStateException> { ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toPressure() }
        // three terms, but exactly one term carries a wrong exponent for its group
        val g = (1000 of grams).toUnit()
        val m = (1 of meters).toUnit()
        val s2 = (1 of seconds).toUnit() pow 2
        assertFailsWith<IllegalStateException> { ((g pow 2) / m / s2).toPressure() }       // mass^2
        assertFailsWith<IllegalStateException> { (g / (m pow 2) / s2).toPressure() }        // length^-2
        assertFailsWith<IllegalStateException> { (g / m / ((1 of seconds).toUnit() pow 3)).toPressure() } // time^-3
    }

    /** `format` a pressure into its base dimensions kg*m^-1*s^-2. */
    @Test
    fun `format compositions`() {
        assertEquals("50.0 kg*m^-1*s^-2", (50 of pascals) format (kilo.grams / meters / (seconds pow 2)))
    }
}
