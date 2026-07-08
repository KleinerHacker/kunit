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

import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.feet
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.into
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

/**
 * Behaviour matrix for the constructed speed group (length·time⁻¹): core→composed (`length / time = speed`)
 * across a length×time matrix, composed→core decomposition, whole-unit speed tokens, and `of`/`into`.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KSpeedTest {

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

    /** Speeds are built as expressions; km/h reads back via a `kilo.meters / hours` template. */
    @Test
    fun `expression speeds and named special tokens`() {
        assertEquals(10.0, (36 of kilo.meters / hours).value, 1e-9)
        assertEquals(36.0, ((100 of meters) / (10 of seconds)) into (kilo.meters / hours), 1e-9)
        // klammerfreier prefixed length rate
        val r = 10 of kilo.meters / hours
        assertIs<KSpeedUnitInstance>(r)
        assertEquals(10_000.0 / 3600.0, r.value, 1e-9)
        // genuinely single-named specials survive as tokens
        assertEquals(KSpeedUnit.KNOT.baseValue, (1 of knots).value, 1e-9)
        assertEquals(340.29, (1 of mach).value, 1e-9)
    }

    /** Adding two speeds (built as expressions) normalizes to m/s. */
    @Test
    fun `speed arithmetic`() {
        assertEquals(20.0, ((36 of kilo.meters / hours) + (10 of meters / seconds)).value, 1e-9)
    }

    /** A non-speed shape (area / time) cannot be read as a speed / fails as data-rate would. */
    @Test
    fun `invalid speed decomposition fails`() {
        val area = (2 of meters) * (3 of meters)
        assertFailsWith<IllegalStateException> { (area.toUnit() / (1 of seconds).toUnit()).toSpeed() }
    }
}
