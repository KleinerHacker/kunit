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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KVolumeFlowUnitInstance` surface: round-trip, equality, `toString`, same-type operators, `toVolumeFlow`. */
class KVolumeFlowUnitSystemTest {

    /** `n of token` round-trips through `into` and normalizes to m³/s. */
    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of litersPerSecond) into litersPerSecond, 1e-12)
        assertEquals(0.005, (5 of litersPerSecond) into cubicMetersPerSecond, 1e-12)
        assertEquals(300.0, (5 of litersPerSecond) into litersPerMinute, 1e-9)
    }

    /** Equality/hash by normalized value (`1 l/s == 60 l/min`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of litersPerSecond, 60 of litersPerMinute)
        assertEquals((1 of litersPerSecond).hashCode(), (60 of litersPerMinute).hashCode())
        assertFalse((1 of litersPerSecond) == (2 of litersPerSecond))
        assertFalse((1 of litersPerSecond).equals(1.0))
    }

    /** `toString` renders the value in cubic meters per second. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.005 m³/s", (5 of litersPerSecond).toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val q1 = 10 of litersPerSecond
        val q2 = 4 of litersPerSecond
        assertEquals(6.0, (q1 - q2) into litersPerSecond, 1e-9)
        assertEquals(14.0, (q1 + q2) into litersPerSecond, 1e-9)
        assertTrue(q1 > q2)
        assertIs<KMixedUnitInstance>(q1 * q2)
        assertIs<KMixedUnitInstance>(q1 / q2)
    }

    /** A distance³·time⁻¹ mixed unit converts back to a pure volumetric flow; a wrong shape fails. */
    @Test
    fun `toVolumeFlow round-trip and failure`() {
        val raw = ((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()
        assertEquals(2.0, raw.toVolumeFlow() into cubicMetersPerSecond, 1e-9) // 8 m³ / 4 s
        assertFailsWith<IllegalStateException> { (2 of meters).toUnit().toVolumeFlow() }
        // two terms, but a wrong exponent for its group
        val m3 = (2 of meters).toUnit() pow 3
        val s = (4 of seconds).toUnit()
        assertFailsWith<IllegalStateException> { (((2 of meters).toUnit() pow 2) / s).toVolumeFlow() }
        assertFailsWith<IllegalStateException> { (m3 / (s pow 2)).toVolumeFlow() }
    }
}
