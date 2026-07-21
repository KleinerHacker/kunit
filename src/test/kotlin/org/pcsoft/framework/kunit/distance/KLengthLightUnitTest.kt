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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The prefix-free `light` grouping builder ([KLengthLightUnitBuilder]): every `light.<unit>` token
 * builds and reads through `of`/`into` and normalizes to the expected meters, and it composes with the
 * length operators.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthLightUnitTest {

    /** All `light.<unit>` tokens paired with their base value (meters per 1 unit). */
    private val lightTokens: List<Pair<KLengthUnitInstance, Double>> = listOf(
        light.seconds to KDistanceUnit.LIGHT_SECOND.baseValue,
        light.minutes to KDistanceUnit.LIGHT_MINUTE.baseValue,
        light.hours to KDistanceUnit.LIGHT_HOUR.baseValue,
        light.days to KDistanceUnit.LIGHT_DAY.baseValue,
        light.weeks to KDistanceUnit.LIGHT_WEEK.baseValue,
        light.years to KDistanceUnit.LIGHT_YEAR.baseValue
    )

    private fun rel(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    private fun tokens(): List<Array<Any>> = lightTokens.map { arrayOf<Any>(it.first, it.second) }

    /** Each `light.<unit>` builds via `of` and normalizes to `n * base` meters, read back as `n`. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `light token builds and reads`(token: KLengthUnitInstance, base: Double) {
        assertEquals(5.0 * base, (5 of token).value, rel(5.0 * base))
        assertEquals(5.0, ((5 of token) into meters) / base, rel(5.0))
    }

    /** `light.seconds` normalizes to c meters (299 792 458 m). */
    @Test
    fun `light second equals speed of light in meters`() {
        assertEquals(299_792_458.0, (1 of light.seconds) into meters, 1e-3)
    }

    /** `light.<unit>` composes with the length operators just like any other length token. */
    @Test
    fun `light token combines with operators`() {
        val expected = 2.0 * KDistanceUnit.LIGHT_YEAR.baseValue / KDistanceUnit.METER.baseValue
        assertEquals(expected, (2 of light.years) into meters, rel(expected))
    }
}
