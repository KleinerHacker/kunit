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

package org.pcsoft.framework.kunit.conductance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.assertEquals

/**
 * The concrete conductance units (`KConductanceUnitBareValues`): every unit builds to its siemens value,
 * round-trips through `into`, and reads correctly against siemens. Covers all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KConductanceUnitTest {

    private val tokens: List<Triple<String, KConductanceUnitInstance, KConductanceUnit>> = listOf(
        Triple("siemens", siemens, KConductanceUnit.SIEMENS),
        Triple("mhos", mhos, KConductanceUnit.MHO),
        Triple("abmhos", abmhos, KConductanceUnit.ABMHO),
        Triple("statmhos", statmhos, KConductanceUnit.STATMHO),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented siemens value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KConductanceUnitInstance, unit: KConductanceUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into siemens, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("S", KConductanceUnit.BASE.symbol)
        assertEquals(unit, KConductanceUnit.valueOf(unit.name))
    }

    /** The symbols of every conductance unit, including the mho sign U+2127. */
    @kotlin.test.Test
    fun `unit symbols`() {
        assertEquals("S", KConductanceUnit.SIEMENS.symbol)
        assertEquals("℧", KConductanceUnit.MHO.symbol)
        assertEquals("ab℧", KConductanceUnit.ABMHO.symbol)
        assertEquals("stat℧", KConductanceUnit.STATMHO.symbol)
        assertEquals(KConductanceUnit.SIEMENS, KConductanceUnit.BASE)
    }
}
