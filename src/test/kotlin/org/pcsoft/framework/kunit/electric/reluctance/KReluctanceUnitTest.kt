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
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete reluctance units (`KReluctanceUnitBareValues`): every unit builds to its ampere-per-weber
 * value, round-trips through `into`, and reads correctly against amperes per weber. Covers all bare tokens
 * (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KReluctanceUnitTest {

    private val tokens: List<Triple<String, KReluctanceUnitInstance, KReluctanceUnit>> = listOf(
        Triple("amperesPerWeber", amperesPerWeber, KReluctanceUnit.AMPERE_PER_WEBER),
        Triple("inverseHenries", inverseHenries, KReluctanceUnit.INVERSE_HENRY),
        Triple("ampereTurnsPerWeber", ampereTurnsPerWeber, KReluctanceUnit.AMPERE_TURN_PER_WEBER),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented ampere-per-weber value and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KReluctanceUnitInstance, unit: KReluctanceUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into amperesPerWeber, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KReluctanceUnit.valueOf(unit.name))
    }

    /** The declared symbols of every reluctance unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("A/Wb", KReluctanceUnit.AMPERE_PER_WEBER.symbol)
        assertEquals("H⁻¹", KReluctanceUnit.INVERSE_HENRY.symbol)
        assertEquals("At/Wb", KReluctanceUnit.AMPERE_TURN_PER_WEBER.symbol)
        assertEquals(KReluctanceUnit.AMPERE_PER_WEBER, KReluctanceUnit.BASE)
        assertEquals(3, KReluctanceUnit.entries.size)
    }

    /** The declared base values (relative to the ampere per weber) of every reluctance unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KReluctanceUnit.AMPERE_PER_WEBER.baseValue, 1e-30)
        assertEquals(1.0, KReluctanceUnit.INVERSE_HENRY.baseValue, 1e-30)
        assertEquals(1.0, KReluctanceUnit.AMPERE_TURN_PER_WEBER.baseValue, 1e-30)
    }

    /** All three spellings describe the same quantity and are therefore value-equal. */
    @Test
    fun `all spellings are equal`() {
        assertEquals(1 of amperesPerWeber, 1 of inverseHenries)
        assertEquals(1 of amperesPerWeber, 1 of ampereTurnsPerWeber)
    }
}
