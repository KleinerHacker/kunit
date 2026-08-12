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

package org.pcsoft.framework.kunit.electric.capacitance

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete capacitance units (`KCapacitanceUnitBareValues`): every unit builds to its farad value,
 * round-trips through `into`, and reads correctly against farads. Covers all bare tokens (symbol, relative value).
 */
class KCapacitanceUnitTest {

    private val tokens: List<Triple<String, KCapacitanceUnitInstance, KCapacitanceUnit>> = listOf(
        Triple("farads", farads, KCapacitanceUnit.FARAD),
        Triple("abfarads", abfarads, KCapacitanceUnit.ABFARAD),
        Triple("statfarads", statfarads, KCapacitanceUnit.STATFARAD),
        Triple("jars", jars, KCapacitanceUnit.JAR),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented farad value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KCapacitanceUnitInstance, case[2] as KCapacitanceUnit)
    }

    private fun `unit conversion`(name: String, token: KCapacitanceUnitInstance, unit: KCapacitanceUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into farads, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("F", KCapacitanceUnit.BASE.symbol)
        assertEquals(unit, KCapacitanceUnit.valueOf(unit.name))
    }

    /** The declared symbols of every capacitance unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("F", KCapacitanceUnit.FARAD.symbol)
        assertEquals("abF", KCapacitanceUnit.ABFARAD.symbol)
        assertEquals("statF", KCapacitanceUnit.STATFARAD.symbol)
        assertEquals("jar", KCapacitanceUnit.JAR.symbol)
        assertEquals(KCapacitanceUnit.FARAD, KCapacitanceUnit.BASE)
    }

    /** The declared base values (relative to the farad) of every capacitance unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KCapacitanceUnit.FARAD.baseValue, 1e-30)
        assertEquals(1.0e9, KCapacitanceUnit.ABFARAD.baseValue, 1.0)
        assertEquals(1.112650056e-12, KCapacitanceUnit.STATFARAD.baseValue, 1e-30)
        assertEquals(1.11265e-9, KCapacitanceUnit.JAR.baseValue, 1e-30)
    }
}
