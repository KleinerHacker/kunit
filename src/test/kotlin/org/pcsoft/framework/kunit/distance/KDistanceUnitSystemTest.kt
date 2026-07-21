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

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.*

/**
 * `KDistanceUnitInstance`/leaf surface: `of`/`into` construction and round-trip reading, the strong type
 * preservation of `of`, in-hierarchy narrowing, the general (exotic-exponent) type, equality, and the
 * conversions from the generic engine.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceUnitSystemTest {

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

    private fun tokens(): List<Array<Any>> = lengthTokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of <unit>` normalizes to meters as `n * baseValue`, and reads back as `n` in its own unit. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `construction and round-trip read`(token: KLengthUnitInstance, base: Double) {
        val v = 7 of token
        assertEquals(7.0 * base, v.value, rel(7.0 * base))
        assertEquals(7.0, v into token, rel(7.0))
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

    /** An exponent outside `{1,2,3}` is represented by the general [KDistanceUnitInstance], whose own
     *  `times`/`div` stay in the generic engine. */
    @Test
    fun `general distance type for exotic exponents`() {
        val m4 = (2 of meters) pow 4
        assertIs<KDistanceUnitInstance>(m4)
        assertEquals(4, m4.exponent)
        assertEquals(16.0, m4.value, 1e-9)
        assertEquals(8, (m4 * m4).units.single().exponent) // general times -> m^8
        assertEquals(1.0, (m4 / m4).value, 1e-9)           // general div -> dimensionless
        assertTrue((m4 / m4).units.isEmpty())
    }

    /** In-hierarchy narrowing ([toLength]/[toArea]/[toVolume]) succeeds for the matching exponent and
     *  fails otherwise. */
    @Test
    fun `in-hierarchy narrowing`() {
        val lenD: KDistanceUnitInstance = 5 of meters
        assertIs<KLengthUnitInstance>(lenD.toLength())
        assertFailsWith<IllegalStateException> { lenD.toArea() }
        assertFailsWith<IllegalStateException> { lenD.toVolume() }

        val areaD: KDistanceUnitInstance = (2 of meters) * (2 of meters)
        assertIs<KAreaUnitInstance>(areaD.toArea())
        assertFailsWith<IllegalStateException> { areaD.toLength() }
        assertFailsWith<IllegalStateException> { areaD.toVolume() }

        val volD: KDistanceUnitInstance = (2 of meters) * (2 of meters) * (2 of meters)
        assertIs<KVolumeUnitInstance>(volD.toVolume())
        assertFailsWith<IllegalStateException> { volD.toLength() }
        assertFailsWith<IllegalStateException> { volD.toArea() }
    }

    /** Equality is by concrete type, exponent and normalized value. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(5 of meters, 5 of meters)
        assertEquals((5 of meters).hashCode(), (5 of meters).hashCode())
        assertNotEquals<KDistanceUnitInstance>(5 of meters, (5 of meters) * (1 of meters)) // length != area
        assertNotEquals(5 of meters, 6 of meters)
        assertFalse((5 of meters).equals(1.0)) // not a KDistanceUnitInstance
        assertNotEquals<KDistanceUnitInstance>((2 of meters) pow 4, (2 of meters) pow 5) // same class, different exponent
    }

    /** Converting a single-term mixed unit back to the pure distance type, with the exponent-specific
     *  narrowing failing on a mismatch and non-distance terms rejected. */
    @Test
    fun `engine conversions and failures`() {
        val lengthMix = (2 of meters).toUnit()
        val areaMix = ((2 of meters) * (2 of meters)).toUnit()
        val volumeMix = ((2 of meters) * (2 of meters) * (2 of meters)).toUnit()

        assertIs<KLengthUnitInstance>(lengthMix.toDistance())
        assertIs<KLengthUnitInstance>(lengthMix.toLength())
        assertIs<KAreaUnitInstance>(areaMix.toArea())
        assertIs<KVolumeUnitInstance>(volumeMix.toVolume())

        assertFailsWith<IllegalStateException> { lengthMix.toArea() }
        assertFailsWith<IllegalStateException> { lengthMix.toVolume() }
        assertFailsWith<IllegalStateException> { areaMix.toLength() }
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toDistance() }
        // a dimensionless (no-term) mixed unit also fails (the term is null)
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() / (1 of meters).toUnit()).toDistance() }
    }
}
