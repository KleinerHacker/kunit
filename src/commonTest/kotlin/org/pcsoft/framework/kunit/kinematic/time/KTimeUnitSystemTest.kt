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

package org.pcsoft.framework.kunit.kinematic.time

import kotlin.math.abs
import kotlin.test.*
import kotlin.test.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow

/**
 * `KTimeUnitInstance` surface: `of`/`into` construction and round-trip, incompatible-unit read failure,
 * and `kotlin.time.Duration` interop.
 */
class KTimeUnitSystemTest {

    private val timeTokens: List<Pair<KTimeUnitInstance, Double>> = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue,
        days to KTimeUnit.DAY.baseValue
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokens(): List<Array<Any>> = timeTokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of <unit>` normalizes to seconds and round-trips back through `into`. */
    @Test
    fun `construction and round-trip`() = forEachCase(tokens()) { case ->
        `construction and round-trip`(case[0] as KTimeUnitInstance, case[1] as Double)
    }

    private fun `construction and round-trip`(token: KTimeUnitInstance, base: Double) {
        assertEquals(3.0 * base, (3 of token).value, rel(3.0 * base))
        assertEquals(3.0, (3 of token) into token, rel(3.0))
    }

    /** Reading a time in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds) into ((1 of seconds) pow 2) }
    }

    /** Interop: a kotlin.time.Duration round-trips through toTime()/toKotlinDuration(). */
    @Test
    fun `duration interop`() {
        val ninetyMinutes = 90.toDuration(DurationUnit.MINUTES)
        assertEquals(90.0 * 60, ninetyMinutes.toTime().value, 1e-9)
        assertEquals(1.5, ninetyMinutes.toTime() into hours, 1e-9)
        assertEquals(ninetyMinutes, ninetyMinutes.toTime().toKotlinDuration())
    }

    /**
     * The second/nanosecond components follow the `java.time` convention: the seconds part is floored
     * towards negative infinity and the nanosecond part stays in `0..999_999_999`, so that
     * `getSeconds() + getNano() / 1e9` reconstructs the value for negative durations too.
     */
    @Test
    fun `negative duration components`() {
        val t = (-1.5) of seconds
        assertEquals(-2L, t.getSeconds())
        assertEquals(500_000_000, t.getNano())
        assertEquals(-1.5, t.getSeconds() + t.getNano() / 1e9, 1e-9)
        assertTrue(t.isNegative())
        assertFalse(t.isZero())
        assertTrue((0 of seconds).isZero())
    }

    /** The full kotlin.time.Duration facade forwarded by KTimeUnitInstance (queries and copy operations). */
    @Test
    fun `duration facade`() {
        val t = 90 of minutes // 5400 s = PT1H30M

        // queries
        assertEquals(5400L, t.getSeconds())
        assertEquals(0, t.getNano())
        assertFalse(t.isZero())
        assertFalse(t.isNegative())
        assertEquals(0L, t.toDays())
        assertEquals(1L, t.toHours())
        assertEquals(90L, t.toMinutes())
        assertEquals(5400L, t.toSeconds())
        assertEquals(5_400_000L, t.toMillis())
        assertEquals(5_400_000_000_000L, t.toNanos())
        assertEquals(0L, t.toDaysPart())
        assertEquals(1, t.toHoursPart())
        assertEquals(30, t.toMinutesPart())
        assertEquals(0, t.toSecondsPart())
        assertEquals(0, t.toMillisPart())
        assertEquals(0, t.toNanosPart())
        assertEquals(5400.toDuration(DurationUnit.SECONDS), t.toKotlinDuration())

        // additive copies
        assertEquals(5460.0, t.plus(1.toDuration(DurationUnit.MINUTES)).value, 1e-9)
        assertEquals(5460.0, t.plus(1, DurationUnit.MINUTES).value, 1e-9)
        assertEquals(91_800.0, t.plusDays(1).value, 1e-9)
        assertEquals(9_000.0, t.plusHours(1).value, 1e-9)
        assertEquals(5460.0, t.plusMinutes(1).value, 1e-9)
        assertEquals(5401.0, t.plusSeconds(1).value, 1e-9)
        assertEquals(5401.0, t.plusMillis(1000).value, 1e-9)
        assertEquals(5401.0, t.plusNanos(1_000_000_000).value, 1e-9)

        // subtractive copies
        assertEquals(5340.0, t.minus(1.toDuration(DurationUnit.MINUTES)).value, 1e-9)
        assertEquals(5340.0, t.minus(1, DurationUnit.MINUTES).value, 1e-9)
        assertEquals(-81_000.0, t.minusDays(1).value, 1e-9)
        assertEquals(1_800.0, t.minusHours(1).value, 1e-9)
        assertEquals(5340.0, t.minusMinutes(1).value, 1e-9)
        assertEquals(5399.0, t.minusSeconds(1).value, 1e-9)
        assertEquals(5399.0, t.minusMillis(1000).value, 1e-9)
        assertEquals(5399.0, t.minusNanos(1_000_000_000).value, 1e-9)

        // scaling / sign / rounding / with
        assertEquals(10_800.0, t.multipliedBy(2).value, 1e-9)
        assertEquals(2_700.0, t.dividedBy(2).value, 1e-9)
        assertEquals(3L, t.dividedBy(30 of minutes))
        assertEquals(-5400.0, t.negated().value, 1e-9)
        assertTrue(t.negated().isNegative())
        assertEquals(5400.0, t.negated().abs().value, 1e-9)
        assertEquals(5400.0, t.truncatedTo(DurationUnit.SECONDS).value, 1e-9)
        assertEquals(60.0, t.withSeconds(60).value, 1e-9)
        assertEquals(5400.0000005, t.withNanos(500).value, 1e-6)
    }

    /** A mixed unit that is not a single time term cannot be converted to a time value. */
    @Test
    fun `toTime on non-time fails`() {
        assertFailsWith<IllegalStateException> { (5 of meters).toUnit().toTime() }
        // a multi-term mixed unit (a speed shape) also fails (singleOrNull returns null)
        assertFailsWith<IllegalStateException> { ((10 of meters).toUnit() / (2 of seconds).toUnit()).toTime() }
    }

    /** `toString` (seconds) and `format` into hours/minutes. */
    @Test
    fun `toString and format compositions`() {
        assertEquals("5400.0 s", (90 of minutes).toString())
        assertEquals("1.5 h", (90 of minutes) format hours)
        assertEquals("90.0 min", (90 of minutes) format minutes)
    }
}
