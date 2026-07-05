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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.speed.metersPerSecond
import org.pcsoft.framework.kunit.speed.times
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Cross-group behaviour of the distance group: distance x time through the mixed engine and the typed speed operators. */
class KDistanceMixedUnitTest {

    /** Dividing a length by a time through the mixed engine forms a raw two-term `[m¹, s⁻¹]` mixed unit with the right value. */
    @Test
    fun `length over time forms a two-term mixed unit`() {
        val raw = 10.meters.toUnit() / 2.seconds.toUnit()
        assertEquals(5.0, raw.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), raw.units)
    }

    /** The typed cross-group operator `length / time` returns a strongly typed [KSpeedUnitInstance] with the right value. */
    @Test
    fun `length divided by time is a strongly typed speed`() {
        val speed: KSpeedUnitInstance = 100.meters / 10.seconds
        assertEquals(10.0, speed.value, 1e-9)
    }

    /** `speed * time` recovers a typed length (600 m) and reads back correctly in **every** distance unit (composed → core decomposition). */
    @Test
    fun `speed times time recovers a length in every distance unit`() {
        val length: KLengthUnitInstance = 10.metersPerSecond * 60.seconds // 600 m
        for ((_, unit) in lengthUnitGenerators) {
            val expected = 600.0 / unit.baseValue
            assertEquals(expected, length.valueAs(unit), distanceDelta(expected), "read back in $unit")
        }
    }

    /** `length / speed` recovers a time (60 s) and reads back correctly in **every** time unit (composed → core decomposition). */
    @Test
    fun `length divided by speed recovers a time in every time unit`() {
        val time = 600.meters / 10.metersPerSecond // 60 s
        for (unit in KTimeUnit.entries) {
            val expected = 60.0 / unit.baseValue
            assertEquals(expected, time.valueAs(unit), 1e-6, "read back in $unit")
        }
    }

    /** Dividing an area (exponent 2) by a time is not a valid speed shape and throws `IllegalStateException`. */
    @Test
    fun `an area divided by a time is not a speed`() {
        val area: KDistanceUnitInstance = 200.meters * 50.meters // exponent 2
        assertFailsWith<IllegalStateException> { area / 10.seconds }
    }

    /** `speed * time` and `time * speed` yield the same typed length — the cross-group multiplication is commutative. */
    @Test
    fun `time times speed is commutative`() {
        val a: KLengthUnitInstance = 10.metersPerSecond * 60.seconds
        val b: KLengthUnitInstance = 60.seconds * 10.metersPerSecond
        assertTrue(a == b)
    }
}
