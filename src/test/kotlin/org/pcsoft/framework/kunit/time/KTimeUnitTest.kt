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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.assertEquals

/** The concrete time units (`KTimeUnitBareValues` + `KTimeUnitExtensions`): the full conversion matrix. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimeUnitTest {

    private val timeTokens: List<Pair<KTimeUnitInstance, Double>> = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue,
        days to KTimeUnit.DAY.baseValue
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun pairs(): List<Array<Any>> =
        timeTokens.flatMap { a -> timeTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }

    /** Every time unit converts into every other as `n * from.base / to.base`. */
    @ParameterizedTest
    @MethodSource("pairs")
    fun `conversion matrix`(from: KTimeUnitInstance, fb: Double, to: KTimeUnitInstance, tb: Double) {
        val expected = 5.0 * fb / tb
        assertEquals(expected, (5 of from) into to, rel(expected))
    }
}
