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

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete resistivity units (`KResistivityUnitBareValues`): every unit builds to its ohm meter value,
 * round-trips through `into`, and reads correctly against ohm meters. Covers all bare tokens (symbol,
 * relative value).
 */
class KResistivityUnitTest {

    private val tokens: List<Triple<String, KResistivityUnitInstance, KResistivityUnit>> = listOf(
        Triple("ohmMeters", ohmMeters, KResistivityUnit.OHM_METER),
        Triple("ohmCentimeters", ohmCentimeters, KResistivityUnit.OHM_CENTIMETER),
        Triple("statohmCentimeters", statohmCentimeters, KResistivityUnit.STATOHM_CENTIMETER),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented ohm meter value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KResistivityUnitInstance, case[2] as KResistivityUnit)
    }

    private fun `unit conversion`(name: String, token: KResistivityUnitInstance, unit: KResistivityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into ohmMeters, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("Ω·m", KResistivityUnit.BASE.symbol)
        assertEquals(unit, KResistivityUnit.valueOf(unit.name))
    }

    /** The declared symbols of every resistivity unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("Ω·m", KResistivityUnit.OHM_METER.symbol)
        assertEquals("Ω·cm", KResistivityUnit.OHM_CENTIMETER.symbol)
        assertEquals("statΩ·cm", KResistivityUnit.STATOHM_CENTIMETER.symbol)
        assertEquals(KResistivityUnit.OHM_METER, KResistivityUnit.BASE)
        assertEquals(3, KResistivityUnit.entries.size)
    }

    /** The declared base values (relative to the ohm meter) of every resistivity unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KResistivityUnit.OHM_METER.baseValue, 1e-30)
        assertEquals(0.01, KResistivityUnit.OHM_CENTIMETER.baseValue, 1e-30)
        assertEquals(8.98755179e9, KResistivityUnit.STATOHM_CENTIMETER.baseValue, 1.0)
    }
}
