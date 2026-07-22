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

package org.pcsoft.framework.kunit.resistance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.assertEquals

/**
 * The concrete resistance units (`KResistanceUnitBareValues`): every unit builds to its ohm value,
 * round-trips through `into`, and reads correctly against ohms. Covers all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistanceUnitTest {

    private val tokens: List<Triple<String, KResistanceUnitInstance, KResistanceUnit>> = listOf(
        Triple("ohms", ohms, KResistanceUnit.OHM),
        Triple("statohms", statohms, KResistanceUnit.STATOHM),
        Triple("abohms", abohms, KResistanceUnit.ABOHM),
        Triple("internationalOhms", internationalOhms, KResistanceUnit.INTERNATIONAL_OHM),
        Triple("legalOhms", legalOhms, KResistanceUnit.LEGAL_OHM),
        Triple("siemensUnits", siemensUnits, KResistanceUnit.SIEMENS_UNIT),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented ohm value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KResistanceUnitInstance, unit: KResistanceUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into ohms, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("Ω", KResistanceUnit.BASE.symbol)
        assertEquals(unit, KResistanceUnit.valueOf(unit.name))
    }
}
