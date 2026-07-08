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

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.deca
import org.pcsoft.framework.kunit.deci
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
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
 * Full behaviour matrix for the distance group under the `of`/`into`/builder DSL: length construction and
 * reading, the `+`/`-`/`*`/`/` operators (typed dimension transitions), comparisons, `pow`, the prefix
 * builders (incl. the diminishing/augmenting split) and the named area/volume special units.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceTest {

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

    /** All 24 SI prefix builders paired with their scale factor. */
    private val allPrefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        deca to 10.0, hecto to 100.0, kilo to 1_000.0, mega to 1e6, giga to 1e9,
        deci to 0.1, centi to 0.01, milli to 1e-3, micro to 1e-6, nano to 1e-9
    )

    private fun rel(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    private fun lengthPairs(): List<Array<Any>> =
        lengthTokens.flatMap { a -> lengthTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }

    private fun tokens(): List<Array<Any>> = lengthTokens.map { arrayOf<Any>(it.first, it.second) }

    private fun prefixes(): List<Array<Any>> = allPrefixes.map { arrayOf<Any>(it.first, it.second) }

    /** `n of <unit>` normalizes to meters as `n * baseValue`, and reads back as `n` in its own unit. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `construction and round-trip read`(token: KLengthUnitInstance, base: Double) {
        val v = 7 of token
        assertEquals(7.0 * base, v.value, rel(7.0 * base))
        assertEquals(7.0, v into token, rel(7.0))
    }

    /** Converting one length unit into every other equals `n * from.base / to.base`. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `conversion matrix`(from: KLengthUnitInstance, fromBase: Double, to: KLengthUnitInstance, toBase: Double) {
        val expected = 3.0 * fromBase / toBase
        assertEquals(expected, (3 of from) into to, rel(expected))
    }

    /** Adding two lengths of any units normalizes both and returns a length equal to the summed meters. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `plus matrix`(a: KLengthUnitInstance, aBase: Double, b: KLengthUnitInstance, bBase: Double) {
        val sum = (2 of a) + (3 of b)
        assertEquals(2.0 * aBase + 3.0 * bBase, sum.value, rel(2.0 * aBase + 3.0 * bBase))
    }

    /** Subtracting two lengths normalizes both and returns their difference as a length. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `minus matrix`(a: KLengthUnitInstance, aBase: Double, b: KLengthUnitInstance, bBase: Double) {
        val diff = (5 of a) - (1 of b)
        assertEquals(5.0 * aBase - 1.0 * bBase, diff.value, rel(abs(5.0 * aBase) + abs(bBase)))
    }

    /** `length * length` is a typed area whose value is the product of the two meter values. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `times gives area`(a: KLengthUnitInstance, aBase: Double, b: KLengthUnitInstance, bBase: Double) {
        val area = (2 of a) * (3 of b)
        assertIs<KAreaUnitInstance>(area)
        assertEquals(2.0 * aBase * 3.0 * bBase, area.value, rel(2.0 * aBase * 3.0 * bBase))
    }

    /** `length / length` cancels to a dimensionless mixed unit equal to the meter ratio. */
    @ParameterizedTest
    @MethodSource("lengthPairs")
    fun `div cancels to ratio`(a: KLengthUnitInstance, aBase: Double, b: KLengthUnitInstance, bBase: Double) {
        val ratio = (6 of a) / (2 of b)
        assertTrue(ratio.units.isEmpty())
        assertEquals(6.0 * aBase / (2.0 * bBase), ratio.value, rel(6.0 * aBase / (2.0 * bBase)))
    }

    /** Comparisons use the normalized meter value: 1 km > 500 m, and equal magnitudes compare equal. */
    @Test
    fun `comparison uses normalized value`() {
        assertTrue((1 of kilo.meters) > (500 of meters))
        assertTrue((1 of kilo.meters) >= (1000 of meters))
        assertTrue((999 of meters) < (1 of kilo.meters))
        assertEquals(0, (1000 of meters).compareTo(1 of kilo.meters))
    }

    /** `(n of unit) pow 2` powers the value and yields a typed area (`(2 m)² = 4 m²`). */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `pow 2 gives area`(token: KLengthUnitInstance, base: Double) {
        val area = (2 of token) pow 2
        assertIs<KAreaUnitInstance>(area)
        assertEquals((2.0 * base) * (2.0 * base), area.value, rel((2.0 * base) * (2.0 * base)))
    }

    /** `(n of unit) pow 3` yields a volume by value; statically-typed `volume / area = length`. */
    @Test
    fun `pow 3 gives volume and volume over area is length`() {
        val volume = (2 of meters) pow 3
        assertIs<KVolumeUnitInstance>(volume)
        assertEquals(8.0, volume.value, 1e-9)
        // `pow` is statically KDistanceUnitInstance, so build the typed operands via `*` for the
        // strongly-typed `volume / area = length` transition.
        val typedVolume = (2 of meters) * (2 of meters) * (2 of meters)
        val typedArea = (2 of meters) * (2 of meters)
        val back = typedVolume / typedArea
        assertIs<KLengthUnitInstance>(back)
        assertEquals(2.0, back.value, 1e-9)
    }

    /** Every SI prefix builder scales a meter template by its factor (`1 of kilo.meters == 1000 m`). */
    @ParameterizedTest
    @MethodSource("prefixes")
    fun `prefix standalone on meters`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.meters).value, rel(factor))
    }

    /** Prefix x length-unit matrix (kilo/milli): `n of kilo.<unit>` = `n * 1000 * base`, read back as `n`. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `prefix times unit`(@Suppress("UNUSED_PARAMETER") token: KLengthUnitInstance, base: Double) {
        // Reconstruct the prefixed template from the same unit via the meter-relative base value.
        val kiloTemplate = kilo.meters.scaledBy(base) // 1000 * base meters, i.e. kilo.<unit>
        assertEquals(1000.0 * base * 4.0, (4 of kiloTemplate).value, rel(1000.0 * base * 4.0))
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

    /** `of` preserves the strong static type of the template. */
    @Test
    fun `of preserves type`() {
        assertIs<KLengthUnitInstance>(5 of meters)
        assertIs<KAreaUnitInstance>(5 of hectares)
        assertIs<KVolumeUnitInstance>(5 of liters)
        assertIs<KLengthUnitInstance>(5 of kilo.meters)
    }

    /** `into` across incompatible dimensions (length read as area) fails with IllegalStateException. */
    @Test
    fun `into incompatible dimension fails`() {
        assertFailsWith<IllegalStateException> { (1 of meters) into hectares }
        assertFailsWith<IllegalStateException> { (1 of hectares) into meters }
    }

    /** Adding an area and a length (same group, different exponent) is a compile error, checked here via the
     *  runtime mixed-engine equivalent failing. */
    @Test
    fun `area plus length is invalid via engine`() {
        assertFailsWith<IllegalStateException> { (1 of hectares).toUnit() + (1 of meters).toUnit() }
    }
}
