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

package org.pcsoft.framework.kunit.electric.resistivity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KResistivityUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistivityUnitSystemTest {

    private val tokens: List<Pair<KResistivityUnitInstance, Double>> = listOf(
        ohmMeters to KResistivityUnit.OHM_METER.baseValue,
        ohmCentimeters to KResistivityUnit.OHM_CENTIMETER.baseValue,
        statohmCentimeters to KResistivityUnit.STATOHM_CENTIMETER.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to ohm meters and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KResistivityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized ohm meter value (`1 Ω·m == 100 Ω·cm`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of ohmMeters, 100 of ohmCentimeters)
        assertEquals((1 of ohmMeters).hashCode(), (100 of ohmCentimeters).hashCode())
        assertFalse((1 of ohmMeters) == (2 of ohmMeters))
        assertFalse((1 of ohmMeters).equals(1.0)) // not a KResistivityUnitInstance
    }

    /** `toString` renders the normalized ohm meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.01 Ω·m", (1 of ohmCentimeters).toString())
    }

    /** The real-world material reading: copper has a resistivity of 17 nΩ·m. */
    @Test
    fun `copper resistivity real world`() {
        assertEquals(1.7e-8, (17 of nano.ohmMeters).value, 1e-20)
        assertEquals(1.7e-6, (17 of nano.ohmMeters) into ohmCentimeters, 1e-18)
    }

    /** A mixed unit that is not a canonical resistivity normal form cannot be converted to a resistivity. */
    @Test
    fun `toResistivity on non-resistivity fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toResistivity() }
        assertFailsWith<IllegalStateException> {
            ((1 of ohmMeters).toUnit() * (1 of ohmMeters).toUnit()).toResistivity()
        }
    }
}
