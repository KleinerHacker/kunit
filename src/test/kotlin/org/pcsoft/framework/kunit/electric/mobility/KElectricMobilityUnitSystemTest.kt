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

package org.pcsoft.framework.kunit.electric.mobility

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * `KElectricMobilityUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricMobilityUnitSystemTest {

    private val tokens: List<Pair<KElectricMobilityUnitInstance, Double>> = listOf(
        squareMetersPerVoltSecond to KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND.baseValue,
        squareCentimetersPerVoltSecond to KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to m²/(V·s) and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KElectricMobilityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(
            1.0001,
            ((1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)).value,
            1e-12,
        )
        assertEquals(
            0.9999,
            ((1 of squareMetersPerVoltSecond) - (1 of squareCentimetersPerVoltSecond)).value,
            1e-12,
        )
        assertTrue((1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond))
        assertEquals(
            0,
            (1 of squareMetersPerVoltSecond).compareTo(10000 of squareCentimetersPerVoltSecond),
        )
    }

    /** Multiplying/dividing two mobilities leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)).value, 1e-9)
        assertEquals(2.0, ((6 of squareMetersPerVoltSecond) / (3 of squareMetersPerVoltSecond)).value, 1e-9)
    }

    /** Equality/hash by normalized m²/(V·s) value. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of squareMetersPerVoltSecond, 10000 of squareCentimetersPerVoltSecond)
        assertEquals(
            (1 of squareMetersPerVoltSecond).hashCode(),
            (10000 of squareCentimetersPerVoltSecond).hashCode(),
        )
        assertFalse((1 of squareMetersPerVoltSecond) == (2 of squareMetersPerVoltSecond))
        assertFalse((1 of squareMetersPerVoltSecond).equals(1.0)) // not a KElectricMobilityUnitInstance
    }

    /** `toString` renders the normalized m²/(V·s) value. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.14 m²/(V·s)", (1400 of squareCentimetersPerVoltSecond).toString())
    }

    /** The real-world reading: the electron mobility in silicon is 1400 cm²/(V·s). */
    @Test
    fun `silicon electron mobility real world`() {
        assertEquals(0.14, (1400 of squareCentimetersPerVoltSecond).value, 1e-12)
        assertEquals(1400.0, (0.14 of squareMetersPerVoltSecond) into squareCentimetersPerVoltSecond, 1e-6)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to an electric mobility. */
    @Test
    fun `toElectricMobility on non-mobility fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toElectricMobility() }
        assertFailsWith<IllegalStateException> {
            ((1 of squareMetersPerVoltSecond).toUnit() * (1 of squareMetersPerVoltSecond).toUnit())
                .toElectricMobility()
        }
    }
}
