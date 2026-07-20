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
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete distance units (`KLengthUnitBareValues` + `KLengthUnitExtensions`): the full conversion
 * matrix between all length units, and the named area/volume special units.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceUnitTest {

    /** All length bare tokens paired with their base value (meters per 1 unit), for expected-value maths. */
    private val lengthTokens: List<Pair<KLengthUnitInstance, Double>> = listOf(
        meters to KDistanceUnit.METER.baseValue,
        miles to KDistanceUnit.MILE.baseValue,
        nauticalMiles to KDistanceUnit.NAUTICAL_MILE.baseValue,
        yards to KDistanceUnit.YARD.baseValue,
        feet to KDistanceUnit.FOOT.baseValue,
        inches to KDistanceUnit.INCH.baseValue,
        fathoms to KDistanceUnit.FATHOM.baseValue,
        chains to KDistanceUnit.CHAIN.baseValue,
        furlongs to KDistanceUnit.FURLONG.baseValue,
        lightYears to KDistanceUnit.LIGHT_YEAR.baseValue,
        parsecs to KDistanceUnit.PARSEC.baseValue
    )

    private fun rel(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    private fun lengthPairs(): List<Array<Any>> =
        lengthTokens.flatMap { a -> lengthTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }

    /** Converting one length unit into every other equals `n * from.base / to.base`. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `conversion matrix`(from: KLengthUnitInstance, fromBase: Double, to: KLengthUnitInstance, toBase: Double) {
        val expected = 3.0 * fromBase / toBase
        assertEquals(expected, (3 of from) into to, rel(expected))
    }

    /** The named area special units build and read via `of`/`into` (2 ha = 20 000 m²). */
    @Test
    fun `area special units`() {
        assertEquals(20_000.0, (2 of hectares).value, 1e-6)
        assertEquals(2.0, (2 of hectares) into hectares, 1e-9)
        assertEquals(2.0, (20_000 of meters * (1 of meters)).let { it into hectares }, 1e-9)
        assertEquals(100.0, (1 of hectares) into ares, 1e-9)
    }

    /** The named volume special units build and read via `of`/`into` (1 L = 0.001 m³). */
    @Test
    fun `volume special units and milli prefix`() {
        assertEquals(0.001, (1 of liters).value, 1e-12)
        assertEquals(1000.0, (1 of meters pow 3) into liters, 1e-6)
        assertEquals(1.0, (1 of milli.liters).value / 1e-6, 1e-9) // 1 mL = 1e-6 m³
    }
}
