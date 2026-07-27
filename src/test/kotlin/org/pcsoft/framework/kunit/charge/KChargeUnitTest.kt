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

package org.pcsoft.framework.kunit.charge

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete charge units (`KChargeUnitBareValues`): every unit builds to its coulomb value,
 * round-trips through `into`, and reads correctly against coulombs. Covers all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KChargeUnitTest {

    private val tokens: List<Triple<String, KChargeUnitInstance, KChargeUnit>> = listOf(
        Triple("coulombs", coulombs, KChargeUnit.COULOMB),
        Triple("ampereSeconds", ampereSeconds, KChargeUnit.AMPERE_SECOND),
        Triple("ampereHours", ampereHours, KChargeUnit.AMPERE_HOUR),
        Triple("abcoulombs", abcoulombs, KChargeUnit.ABCOULOMB),
        Triple("statcoulombs", statcoulombs, KChargeUnit.STATCOULOMB),
        Triple("faradays", faradays, KChargeUnit.FARADAY),
        Triple("elementaryCharges", elementaryCharges, KChargeUnit.ELEMENTARY_CHARGE),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented coulomb value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KChargeUnitInstance, unit: KChargeUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into coulombs, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("C", KChargeUnit.BASE.symbol)
        assertEquals(unit, KChargeUnit.valueOf(unit.name))
    }

    /** The declared symbols of every charge unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("C", KChargeUnit.COULOMB.symbol)
        assertEquals("As", KChargeUnit.AMPERE_SECOND.symbol)
        assertEquals("Ah", KChargeUnit.AMPERE_HOUR.symbol)
        assertEquals("abC", KChargeUnit.ABCOULOMB.symbol)
        assertEquals("statC", KChargeUnit.STATCOULOMB.symbol)
        assertEquals("F_c", KChargeUnit.FARADAY.symbol)
        assertEquals("e", KChargeUnit.ELEMENTARY_CHARGE.symbol)
        assertEquals(KChargeUnit.COULOMB, KChargeUnit.BASE)
    }
}
