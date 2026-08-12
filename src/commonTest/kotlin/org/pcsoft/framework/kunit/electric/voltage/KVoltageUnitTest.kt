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

package org.pcsoft.framework.kunit.electric.voltage

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete voltage units (`KVoltageUnitBareValues`): every unit builds to its volt value, round-trips
 * through `into`, and reads correctly against volts. Covers all bare tokens (symbol, relative value).
 */
class KVoltageUnitTest {

    private val tokens: List<Triple<String, KVoltageUnitInstance, KVoltageUnit>> = listOf(
        Triple("volts", volts, KVoltageUnit.VOLT),
        Triple("statvolts", statvolts, KVoltageUnit.STATVOLT),
        Triple("abvolts", abvolts, KVoltageUnit.ABVOLT),
        Triple("westonCells", westonCells, KVoltageUnit.WESTON_CELL),
        Triple("daniells", daniells, KVoltageUnit.DANIELL),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented volt value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KVoltageUnitInstance, case[2] as KVoltageUnit)
    }

    private fun `unit conversion`(name: String, token: KVoltageUnitInstance, unit: KVoltageUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into volts, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("V", KVoltageUnit.BASE.symbol)
        assertEquals(unit, KVoltageUnit.valueOf(unit.name))
    }
}
