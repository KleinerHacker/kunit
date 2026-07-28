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

package org.pcsoft.framework.kunit.electric.reluctance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * `KReluctanceUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KReluctanceUnitSystemTest {

    private val tokens: List<Pair<KReluctanceUnitInstance, Double>> = listOf(
        amperesPerWeber to KReluctanceUnit.AMPERE_PER_WEBER.baseValue,
        inverseHenries to KReluctanceUnit.INVERSE_HENRY.baseValue,
        ampereTurnsPerWeber to KReluctanceUnit.AMPERE_TURN_PER_WEBER.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to amperes per weber and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KReluctanceUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` model the series connection of a magnetic circuit. */
    @Test
    fun `same-type operators`() {
        assertEquals(2.0, ((1 of amperesPerWeber) + (1 of inverseHenries)).value, 1e-9)
        assertEquals(1.0, ((3 of amperesPerWeber) - (2 of amperesPerWeber)).value, 1e-9)
        assertTrue((3 of amperesPerWeber) > (2 of amperesPerWeber))
        assertEquals(0, (1 of amperesPerWeber).compareTo(1 of inverseHenries))
    }

    /** Multiplying/dividing two reluctances leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of amperesPerWeber) * (3 of amperesPerWeber)).value, 1e-9)
        assertEquals(2.0, ((6 of amperesPerWeber) / (3 of amperesPerWeber)).value, 1e-9)
    }

    /** Equality/hash by normalized ampere-per-weber value (`1 A/Wb == 1 H⁻¹`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of amperesPerWeber, 1 of inverseHenries)
        assertEquals((1 of amperesPerWeber).hashCode(), (1 of inverseHenries).hashCode())
        assertFalse((1 of amperesPerWeber) == (2 of amperesPerWeber))
        assertFalse((1 of amperesPerWeber).equals(1.0)) // not a KReluctanceUnitInstance
    }

    /** `toString` renders the normalized ampere-per-weber value. */
    @Test
    fun `toString base unit`() {
        assertEquals("2.0 A/Wb", (2 of inverseHenries).toString())
    }

    /** The real-world reading: an air-gapped iron core has a reluctance of about 2 MA/Wb. */
    @Test
    fun `iron core real world`() {
        assertEquals(2e6, (2 of mega.amperesPerWeber).value, 1e-3)
        assertEquals(2.0, (2 of mega.amperesPerWeber) into mega.inverseHenries, 1e-9)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a reluctance. */
    @Test
    fun `toReluctance on non-reluctance fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toReluctance() }
        assertFailsWith<IllegalStateException> {
            ((1 of amperesPerWeber).toUnit() * (1 of amperesPerWeber).toUnit()).toReluctance()
        }
    }
}
