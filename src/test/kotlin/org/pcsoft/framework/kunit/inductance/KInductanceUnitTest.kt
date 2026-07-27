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

package org.pcsoft.framework.kunit.inductance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete inductance units (`KInductanceUnitBareValues`): every unit builds to its henry value,
 * round-trips through `into`, and reads correctly against henries. Covers all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KInductanceUnitTest {

    private val tokens: List<Triple<String, KInductanceUnitInstance, KInductanceUnit>> = listOf(
        Triple("henries", henries, KInductanceUnit.HENRY),
        Triple("webersPerAmpere", webersPerAmpere, KInductanceUnit.WEBER_PER_AMPERE),
        Triple("abhenries", abhenries, KInductanceUnit.ABHENRY),
        Triple("stathenries", stathenries, KInductanceUnit.STATHENRY),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented henry value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KInductanceUnitInstance, unit: KInductanceUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into henries, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("H", KInductanceUnit.BASE.symbol)
        assertEquals(unit, KInductanceUnit.valueOf(unit.name))
    }

    /** The declared symbols and base values of every inductance unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("H", KInductanceUnit.HENRY.symbol)
        assertEquals("Wb/A", KInductanceUnit.WEBER_PER_AMPERE.symbol)
        assertEquals("abH", KInductanceUnit.ABHENRY.symbol)
        assertEquals("statH", KInductanceUnit.STATHENRY.symbol)
        assertEquals(1.0, KInductanceUnit.HENRY.baseValue, 0.0)
        assertEquals(1.0, KInductanceUnit.WEBER_PER_AMPERE.baseValue, 0.0)
        assertEquals(1e-9, KInductanceUnit.ABHENRY.baseValue, 1e-30)
        assertEquals(8.987551787e11, KInductanceUnit.STATHENRY.baseValue, 1e2)
        assertEquals(KInductanceUnit.HENRY, KInductanceUnit.BASE)
    }
}
