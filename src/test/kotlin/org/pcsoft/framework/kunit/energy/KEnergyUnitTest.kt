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

package org.pcsoft.framework.kunit.energy

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete energy units (`KEnergyUnitBareValues`): every unit builds to its joule value, round-trips
 * through `into`, and reads correctly against joules. Covers all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KEnergyUnitTest {

    private val tokens: List<Triple<String, KEnergyUnitInstance, KEnergyUnit>> = listOf(
        Triple("joules", joules, KEnergyUnit.JOULE),
        Triple("ergs", ergs, KEnergyUnit.ERG),
        Triple("calories", calories, KEnergyUnit.CALORIE),
        Triple("electronVolts", electronVolts, KEnergyUnit.ELECTRON_VOLT),
        Triple("britishThermalUnits", britishThermalUnits, KEnergyUnit.BRITISH_THERMAL_UNIT),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented joule value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KEnergyUnitInstance, unit: KEnergyUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into joules, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("J", KEnergyUnit.BASE.symbol)
        assertEquals(unit, KEnergyUnit.valueOf(unit.name))
    }

    /** The declared symbols of every energy unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("J", KEnergyUnit.JOULE.symbol)
        assertEquals("erg", KEnergyUnit.ERG.symbol)
        assertEquals("cal", KEnergyUnit.CALORIE.symbol)
        assertEquals("eV", KEnergyUnit.ELECTRON_VOLT.symbol)
        assertEquals("BTU", KEnergyUnit.BRITISH_THERMAL_UNIT.symbol)
        assertEquals(KEnergyUnit.JOULE, KEnergyUnit.BASE)
        assertEquals(5, KEnergyUnit.entries.size)
    }

    /** The declared base values (relative to the joule) of every energy unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KEnergyUnit.JOULE.baseValue, 1e-30)
        assertEquals(1.0e-7, KEnergyUnit.ERG.baseValue, 1e-30)
        assertEquals(4.184, KEnergyUnit.CALORIE.baseValue, 1e-12)
        assertEquals(1.602176634e-19, KEnergyUnit.ELECTRON_VOLT.baseValue, 1e-30)
        assertEquals(1055.05585262, KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue, 1e-9)
    }
}
