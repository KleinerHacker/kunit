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

package org.pcsoft.framework.kunit.speed

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.KDistanceUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.feet
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The typed cross-group speed operators: core→composed (`length / time = speed`) across a length×time
 * matrix, composed→core decomposition (`speed * time`, `length / speed`), `+`, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KSpeedOperatorTest {

    private val lengths = listOf(
        meters to KDistanceUnit.METER.baseValue,
        miles to KDistanceUnit.MILE.baseValue,
        feet to KDistanceUnit.FOOT.baseValue
    )
    private val times = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun matrix(): List<Array<Any>> =
        lengths.flatMap { l -> times.map { t -> arrayOf<Any>(l.first, l.second, t.first, t.second) } }

    /** core→composed: `length / time` is a typed speed whose m/s value is `l.base / t.base`. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `length over time is speed`(l: KLengthUnitInstance, lb: Double, t: KTimeUnitInstance, tb: Double) {
        val v = (10 of l) / (2 of t)
        assertIs<KSpeedUnitInstance>(v)
        val expected = 10.0 * lb / (2.0 * tb)
        assertEquals(expected, v.value, rel(expected))
    }

    /** composed→core: `speed * time = length`, recovered value read back in meters. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `speed times time is length`(l: KLengthUnitInstance, lb: Double, t: KTimeUnitInstance, tb: Double) {
        val v = (10 of l) / (2 of t)
        val length = v * (2 of t)
        assertIs<KLengthUnitInstance>(length)
        assertEquals(10.0 * lb, length into meters, rel(10.0 * lb))
    }

    /** composed→core: `length / speed = time`, recovered in seconds. */
    @Test
    fun `length over speed is time`() {
        val v = (100 of meters) / (10 of seconds) // 10 m/s
        val time = (600 of meters) / v
        assertIs<KTimeUnitInstance>(time)
        assertEquals(60.0, time into seconds, 1e-9)
    }

    /** Adding two speeds (built as expressions) normalizes to m/s. */
    @Test
    fun `speed arithmetic`() {
        assertEquals(20.0, ((36 of kilo.meters / hours) + (10 of meters / seconds)).value, 1e-9)
    }

    /** A non-speed shape (area / time, or a single length term) cannot be read as a speed. */
    @Test
    fun `invalid speed decomposition fails`() {
        val area = (2 of meters) * (3 of meters)
        assertFailsWith<IllegalStateException> { (area.toUnit() / (1 of seconds).toUnit()).toSpeed() }
        // a single length term (size != 2, no time term) also fails
        assertFailsWith<IllegalStateException> { (100 of meters).toUnit().toSpeed() }
        // two terms, length present, but the time term has the wrong exponent (+1, not -1)
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() * (1 of seconds).toUnit()).toSpeed() }
    }

    /** Same-type speed operators: `-`, comparison, and `speed*speed`/`speed/speed` escaping to a mixed unit. */
    @Test
    fun `speed same-type operators`() {
        val v1 = (100 of meters) / (10 of seconds) // 10 m/s
        val v2 = (20 of meters) / (10 of seconds)  // 2 m/s
        assertEquals(8.0, (v1 - v2).value, 1e-9)
        assertTrue(v1 > v2)
        assertIs<KMixedUnitInstance>(v1 * v2)
        assertIs<KMixedUnitInstance>(v1 / v2)
    }

    /** The commutative `time * speed = length`. */
    @Test
    fun `time times speed is length`() {
        val v = (100 of meters) / (10 of seconds) // 10 m/s
        val length = (60 of seconds) * v
        assertIs<KLengthUnitInstance>(length)
        assertEquals(600.0, length into meters, 1e-9)
    }

    /** The general-distance fallbacks: `distance / time = speed` and `distance / speed = time` for a
     *  distance whose exponent is only known at runtime (static type [KDistanceUnitInstance]). */
    @Test
    fun `general distance fallbacks`() {
        val d: KDistanceUnitInstance = 100 of meters
        val v = d / (10 of seconds)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(10.0, v.value, 1e-9)
        val d2: KDistanceUnitInstance = 600 of meters
        val time = d2 / v
        assertIs<KTimeUnitInstance>(time)
        assertEquals(60.0, time into seconds, 1e-9)
    }
}
