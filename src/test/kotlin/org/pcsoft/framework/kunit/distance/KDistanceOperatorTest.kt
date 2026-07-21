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

import org.pcsoft.framework.kunit.kilo
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
 * All distance operators: `+`/`-` (length arithmetic), `*`/`/` (typed dimension transitions), comparison,
 * `pow`, and the compile-time-safe cross-dimension failure via the engine.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceOperatorTest {

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
        light.years to KDistanceUnit.LIGHT_YEAR.baseValue,
        parsecs to KDistanceUnit.PARSEC.baseValue
    )

    private fun rel(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    private fun lengthPairs(): List<Array<Any>> =
        lengthTokens.flatMap { a -> lengthTokens.map { b -> arrayOf<Any>(a.first, a.second, b.first, b.second) } }

    private fun tokens(): List<Array<Any>> = lengthTokens.map { arrayOf<Any>(it.first, it.second) }

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

    /** Adding an area and a length (same group, different exponent) is a compile error, checked here via the
     *  runtime mixed-engine equivalent failing. */
    @Test
    fun `area plus length is invalid via engine`() {
        assertFailsWith<IllegalStateException> { (1 of hectares).toUnit() + (1 of meters).toUnit() }
    }

    /** The typed length leaf operators: `length * area = volume`, `length * volume`/`length / area`/
     *  `length / volume` escape to the general distance type at the matching exponent. */
    @Test
    fun `length leaf operators`() {
        val len = 3 of meters
        val area = (2 of meters) * (2 of meters)                    // 4 m²
        val vol = (2 of meters) * (2 of meters) * (2 of meters)     // 8 m³
        assertIs<KVolumeUnitInstance>(len * area)                   // length * area = volume
        assertEquals(12.0, (len * area).value, 1e-9)
        assertEquals(4, (len * vol).exponent)                      // length * volume = m⁴
        assertEquals(-1, (len / area).exponent)                    // length / area = m⁻¹
        assertEquals(-2, (len / vol).exponent)                     // length / volume = m⁻²
    }

    /** The typed area leaf operators: `+`/`-`/comparison and the `*`/`/` transitions. */
    @Test
    fun `area leaf operators`() {
        val a1 = (2 of meters) * (2 of meters) // 4 m²
        val a2 = (3 of meters) * (3 of meters) // 9 m²
        val len = 2 of meters
        val vol = (2 of meters) * (2 of meters) * (2 of meters) // 8 m³
        assertEquals(13.0, (a1 + a2).value, 1e-9)
        assertEquals(-5.0, (a1 - a2).value, 1e-9)
        assertTrue(a2 > a1)
        assertEquals(4, (a1 * a2).exponent)                    // area * area = m⁴
        assertEquals(5, (a1 * vol).exponent)                   // area * volume = m⁵
        assertIs<KLengthUnitInstance>(a1 / len)                // area / length = length
        assertEquals(2.0, (a1 / len).value, 1e-9)
        assertTrue((a1 / a2).units.isEmpty())                  // area / area = dimensionless
        assertEquals(-1, (a1 / vol).exponent)                  // area / volume = m⁻¹
    }

    /** The typed volume leaf operators: `+`/`-`/comparison and the `*`/`/` transitions. */
    @Test
    fun `volume leaf operators`() {
        val v1 = (2 of meters) * (2 of meters) * (2 of meters) // 8 m³
        val v2 = (1 of meters) * (1 of meters) * (1 of meters) // 1 m³
        val len = 2 of meters
        val area = (2 of meters) * (2 of meters) // 4 m²
        assertEquals(9.0, (v1 + v2).value, 1e-9)
        assertEquals(7.0, (v1 - v2).value, 1e-9)
        assertTrue(v1 > v2)
        assertEquals(4, (v1 * len).exponent)                   // volume * length = m⁴
        assertEquals(5, (v1 * area).exponent)                  // volume * area = m⁵
        assertEquals(6, (v1 * v2).exponent)                    // volume * volume = m⁶
        assertIs<KAreaUnitInstance>(v1 / len)                  // volume / length = area
        assertTrue((v1 / v2).units.isEmpty())                  // volume / volume = dimensionless
    }
}
