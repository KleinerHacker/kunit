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

import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** All time operators: `+`/`-` (Duration-exact), comparison, and `pow` escaping to a mixed unit. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimeOperatorTest {

    private val timeTokens: List<Pair<KTimeUnitInstance, Double>> = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue,
        days to KTimeUnit.DAY.baseValue
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun pairs(): List<Array<Any>> =
        timeTokens.flatMap { a -> timeTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }

    /** Adding/subtracting two times of any units normalizes both (Duration-exact). */
    @ParameterizedTest
    @MethodSource("pairs")
    fun `plus and minus matrix`(a: KTimeUnitInstance, ab: Double, b: KTimeUnitInstance, bb: Double) {
        assertEquals(2.0 * ab + 3.0 * bb, ((2 of a) + (3 of b)).value, rel(2.0 * ab + 3.0 * bb))
        assertEquals(4.0 * ab - 1.0 * bb, ((4 of a) - (1 of b)).value, rel(abs(4.0 * ab) + abs(bb)))
    }

    /** Comparisons use the normalized second value. */
    @Test
    fun comparison() {
        assertTrue((1 of hours) > (59 of minutes))
        assertTrue((60 of minutes) >= (1 of hours))
        assertEquals(0, (60 of minutes).compareTo(1 of hours))
    }

    /** `time pow 2` escapes to a dimensionless mixed unit of s² (no Duration for time²). */
    @Test
    fun `pow escapes to mixed`() {
        val sq = (2 of seconds) pow 2
        assertEquals(4.0, sq.value, 1e-9)
        assertEquals(2, sq.units.single().exponent)
    }

    /** `time * time` escapes to s²; `time / time` is dimensionless. */
    @Test
    fun `times and div escape to mixed`() {
        val product = (2 of seconds) * (3 of seconds)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.single().exponent)
        val ratio = (6 of seconds) / (2 of seconds)
        assertEquals(3.0, ratio.value, 1e-9)
        assertTrue(ratio.units.isEmpty())
    }

    /** Equality is nanosecond-exact on the underlying Duration (1 h == 60 min). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of hours, 60 of minutes)
        assertEquals((1 of hours).hashCode(), (60 of minutes).hashCode())
        assertNotEquals(1 of hours, 2 of hours)
        assertFalse((1 of hours).equals(1.0)) // not a KTimeUnitInstance
    }
}
