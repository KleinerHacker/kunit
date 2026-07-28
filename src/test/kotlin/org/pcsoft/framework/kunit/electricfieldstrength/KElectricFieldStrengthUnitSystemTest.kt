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

package org.pcsoft.framework.kunit.electricfieldstrength

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * `KElectricFieldStrengthUnitInstance` surface: `of`/`into` construction and round-trip, same-type
 * arithmetic, comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricFieldStrengthUnitSystemTest {

    private val tokens: List<Pair<KElectricFieldStrengthUnitInstance, Double>> = listOf(
        voltsPerMeter to KElectricFieldStrengthUnit.VOLT_PER_METER.baseValue,
        voltsPerCentimeter to KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue,
        statvoltsPerCentimeter to KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to volts per meter and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KElectricFieldStrengthUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(101.0, ((1 of voltsPerMeter) + (1 of voltsPerCentimeter)).value, 1e-9)
        assertEquals(99.0, ((1 of voltsPerCentimeter) - (1 of voltsPerMeter)).value, 1e-9)
        assertTrue((1 of voltsPerCentimeter) > (1 of voltsPerMeter))
        assertEquals(0, (1 of voltsPerCentimeter).compareTo(100 of voltsPerMeter))
    }

    /** Multiplying/dividing two field strengths leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of voltsPerMeter) * (3 of voltsPerMeter)).value, 1e-9)
        assertEquals(2.0, ((6 of voltsPerMeter) / (3 of voltsPerMeter)).value, 1e-9)
    }

    /** Equality/hash by normalized volt-per-meter value (`1 V/cm == 100 V/m`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of voltsPerCentimeter, 100 of voltsPerMeter)
        assertEquals((1 of voltsPerCentimeter).hashCode(), (100 of voltsPerMeter).hashCode())
        assertFalse((1 of voltsPerMeter) == (2 of voltsPerMeter))
        assertFalse((1 of voltsPerMeter).equals(1.0)) // not a KElectricFieldStrengthUnitInstance
    }

    /** `toString` renders the normalized volt-per-meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("100.0 V/m", (1 of voltsPerCentimeter).toString())
    }

    /** The real-world reading: the dielectric strength of air is about 3 kV/mm = 3 MV/m. */
    @Test
    fun `dielectric strength real world`() {
        assertEquals(3.0e6, (30 of kilo.voltsPerCentimeter).value, 1e-3)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a field strength. */
    @Test
    fun `toElectricFieldStrength on non-field fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toElectricFieldStrength() }
        assertFailsWith<IllegalStateException> {
            ((1 of voltsPerMeter).toUnit() * (1 of voltsPerMeter).toUnit()).toElectricFieldStrength()
        }
    }
}
