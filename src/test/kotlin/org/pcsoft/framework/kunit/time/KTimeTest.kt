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

package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Behaviour matrix for the time group (Duration-backed) under the `of`/`into`/builder DSL. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimeTest {

    private val timeTokens: List<Pair<KTimeUnitInstance, Double>> = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue,
        days to KTimeUnit.DAY.baseValue
    )

    private val prefixes: List<Pair<KPrefixBuilder, Double>> =
        listOf(kilo to 1e3, milli to 1e-3, micro to 1e-6, nano to 1e-9)

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokens(): List<Array<Any>> = timeTokens.map { arrayOf<Any>(it.first, it.second) }
    private fun pairs(): List<Array<Any>> =
        timeTokens.flatMap { a -> timeTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** `n of <unit>` normalizes to seconds and round-trips back through `into`. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `construction and round-trip`(token: KTimeUnitInstance, base: Double) {
        assertEquals(3.0 * base, (3 of token).value, rel(3.0 * base))
        assertEquals(3.0, (3 of token) into token, rel(3.0))
    }

    /** Every time unit converts into every other as `n * from.base / to.base`. */
    @ParameterizedTest
    @MethodSource("pairs")
    fun `conversion matrix`(from: KTimeUnitInstance, fb: Double, to: KTimeUnitInstance, tb: Double) {
        val expected = 5.0 * fb / tb
        assertEquals(expected, (5 of from) into to, rel(expected))
    }

    /** Adding/subtracting two times of any units normalizes both (Duration-exact). */
    @ParameterizedTest
    @MethodSource("pairs")
    fun `plus and minus matrix`(a: KTimeUnitInstance, ab: Double, b: KTimeUnitInstance, bb: Double) {
        assertEquals(2.0 * ab + 3.0 * bb, ((2 of a) + (3 of b)).value, rel(2.0 * ab + 3.0 * bb))
        assertEquals(4.0 * ab - 1.0 * bb, ((4 of a) - (1 of b)).value, rel(abs(4.0 * ab) + abs(bb)))
    }

    /** Comparisons use the normalized second value. */
    @Test
    fun `comparison`() {
        assertTrue((1 of hours) > (59 of minutes))
        assertTrue((60 of minutes) >= (1 of hours))
        assertEquals(0, (60 of minutes).compareTo(1 of hours))
    }

    /** Every prefix builder scales a seconds template by its factor (`1 of milli.seconds == 0.001 s`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `prefix standalone`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.seconds).value, rel(factor))
    }

    /** Prefixed time reads back correctly: 5000 s read in kilo.seconds is 5. */
    @Test
    fun `prefixed read`() {
        assertEquals(5.0, (5000 of seconds) into kilo.seconds, 1e-9)
        assertEquals(2.0, (0.002 of seconds) into milli.seconds, 1e-9)
    }

    /** `time pow 2` escapes to a dimensionless mixed unit of s² (no Duration for time²). */
    @Test
    fun `pow escapes to mixed`() {
        val sq = (2 of seconds) pow 2
        assertEquals(4.0, sq.value, 1e-9)
        assertEquals(2, sq.units.single().exponent)
    }

    /** Reading a time in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds) into ((1 of seconds) pow 2) }
    }

    /** Interop: a java.time.Duration round-trips through toTime()/toDuration(). */
    @Test
    fun `duration interop`() {
        assertEquals(90.0 * 60, java.time.Duration.ofMinutes(90).toTime().value, 1e-9)
        assertEquals(1.5, java.time.Duration.ofMinutes(90).toTime() into hours, 1e-9)
    }
}
