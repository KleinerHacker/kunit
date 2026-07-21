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
        parsecs to KDistanceUnit.PARSEC.baseValue,
        cubits to KDistanceUnit.CUBIT.baseValue,
        romanFeet to KDistanceUnit.ROMAN_FOOT.baseValue,
        romanPaces to KDistanceUnit.ROMAN_PACE.baseValue,
        stadia to KDistanceUnit.STADIUM.baseValue,
        romanMiles to KDistanceUnit.ROMAN_MILE.baseValue,
        rods to KDistanceUnit.ROD.baseValue,
        leagues to KDistanceUnit.LEAGUE.baseValue,
        cableLengths to KDistanceUnit.CABLE_LENGTH.baseValue,
        versts to KDistanceUnit.VERST.baseValue,
        prussianMiles to KDistanceUnit.PRUSSIAN_MILE.baseValue
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

    /** The named historical area special units build and read via `of`/`into` (normalized to m²). */
    @Test
    fun `historical area special units`() {
        assertEquals(4046.8564224, (1 of acres).value, 1e-6)
        assertEquals(1011.7141056, (1 of roods).value, 1e-6)
        assertEquals(25.29285264, (1 of squarePerches).value, 1e-6)
        assertEquals(2553.22, (1 of morgens).value, 1e-6)
        assertEquals(5754.642, (1 of jochs).value, 1e-6)
        assertEquals(3407.27, (1 of tagwerks).value, 1e-6)
        assertEquals(4.0, (1 of acres) into roods, 1e-9) // 1 acre = 4 roods
        assertEquals(160.0, (1 of acres) into squarePerches, 1e-9) // 1 acre = 160 square perches
        assertEquals(2.0, (2 of morgens) into morgens, 1e-9)
        assertEquals(3.0, (3 of jochs) into jochs, 1e-9)
        assertEquals(5.0, (5 of tagwerks) into tagwerks, 1e-9)
    }

    /** The named volume special units build and read via `of`/`into` (1 L = 0.001 m³). */
    @Test
    fun `volume special units and milli prefix`() {
        assertEquals(0.001, (1 of liters).value, 1e-12)
        assertEquals(1000.0, (1 of meters pow 3) into liters, 1e-6)
        assertEquals(1.0, (1 of milli.liters).value / 1e-6, 1e-9) // 1 mL = 1e-6 m³
    }

    /** The named historical volume special units build and read via `of`/`into` (normalized to m³). */
    @Test
    fun `historical volume special units`() {
        assertEquals(0.00454609, (1 of imperialGallons).value, 1e-12)
        assertEquals(0.003785411784, (1 of usGallons).value, 1e-12)
        assertEquals(0.03636872, (1 of imperialBushels).value, 1e-12)
        assertEquals(0.158987294928, (1 of oilBarrels).value, 1e-12)
        assertEquals(0.32731785, (1 of hogsheads).value, 1e-12)
        assertEquals(0.00056826125, (1 of imperialPints).value, 1e-12)
        assertEquals(0.0011365225, (1 of imperialQuarts).value, 1e-12)
        assertEquals(2.0, (1 of imperialQuarts) into imperialPints, 1e-9) // 1 quart = 2 pints
        assertEquals(8.0, (1 of imperialGallons) into imperialPints, 1e-9) // 1 gallon = 8 pints
        assertEquals(8.0, (1 of imperialBushels) into imperialGallons, 1e-9) // 1 bushel = 8 gallons
    }
}
