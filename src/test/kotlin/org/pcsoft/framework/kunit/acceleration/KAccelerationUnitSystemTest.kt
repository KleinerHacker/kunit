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

package org.pcsoft.framework.kunit.acceleration

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
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

/** `KAccelerationUnitInstance` surface: `of`/`into` round-trip, equality, `toString`, same-type operators, conversions. */
class KAccelerationUnitSystemTest {

    /** `n of token` normalizes to m/s² and round-trips through `into`. */
    @Test
    fun `construction and round-trip`() {
        assertEquals(0.05, (5 of gals).value, 1e-12)
        assertEquals(5.0, (5 of gals) into gals, 1e-9)
        assertEquals(9.80665, (1 of standardGravities).value, 1e-9)
        assertEquals(2.0, (2 of standardGravities) into standardGravities, 1e-9)
    }

    /** Equality/hash by normalized m/s² value. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(2 of gals, 2 of gals)
        assertEquals((2 of gals).hashCode(), (2 of gals).hashCode())
        assertFalse((1 of gals) == (2 of gals))
        assertFalse((1 of gals).equals(1.0))
    }

    /** `toString` renders the normalized m/s² value. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.01 m/s²", (1 of gals).toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val a1 = 10 of gals // 0.1 m/s²
        val a2 = 4 of gals   // 0.04 m/s²
        assertEquals(0.06, (a1 - a2).value, 1e-12)
        assertEquals(0.14, (a1 + a2).value, 1e-12)
        assertTrue(a1 > a2)
        assertIs<KMixedUnitInstance>(a1 * a2)
        assertIs<KMixedUnitInstance>(a1 / a2)
    }

    /** A length·time⁻² mixed unit converts back to a pure acceleration; a wrong shape fails. */
    @Test
    fun `toAcceleration round-trip and failure`() {
        val raw = (10 of meters).toUnit() / ((1 of seconds).toUnit() pow 2) // [METER^1, SECOND^-2]
        assertEquals(10.0, raw.toAcceleration().value, 1e-9)
        assertFailsWith<IllegalStateException> { (100 of meters).toUnit().toAcceleration() }
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() / (1 of seconds).toUnit()).toAcceleration() }
        // two terms, but the distance term has the wrong exponent (area, not length)
        val areaOverTimeSq = ((2 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)
        assertFailsWith<IllegalStateException> { areaOverTimeSq.toAcceleration() }
    }

    /** `format` into the base-dimension m/s^2. */
    @Test
    fun `format compositions`() {
        assertEquals("0.01 m/s^2", (1 of gals) format (meters / (seconds pow 2)))
    }
}
