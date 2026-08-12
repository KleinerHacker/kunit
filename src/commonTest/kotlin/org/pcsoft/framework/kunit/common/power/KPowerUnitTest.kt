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

package org.pcsoft.framework.kunit.common.power

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete power units (`KPowerUnitBareValues`): every unit builds to its watt value, round-trips through
 * `into`, and reads correctly against watts. Covers all bare tokens (symbol, relative value).
 */
class KPowerUnitTest {

    private val tokens: List<Triple<String, KPowerUnitInstance, KPowerUnit>> = listOf(
        Triple("watts", watts, KPowerUnit.WATT),
        Triple("metricHorsePowers", metricHorsePowers, KPowerUnit.METRIC_HORSE_POWER),
        Triple("mechanicalHorsePowers", mechanicalHorsePowers, KPowerUnit.MECHANICAL_HORSE_POWER),
        Triple("ergsPerSecond", ergsPerSecond, KPowerUnit.ERG_PER_SECOND),
        Triple("voltAmperes", voltAmperes, KPowerUnit.VOLT_AMPERE),
        Triple("vars", vars, KPowerUnit.VAR),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented watt value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KPowerUnitInstance, case[2] as KPowerUnit)
    }

    private fun `unit conversion`(name: String, token: KPowerUnitInstance, unit: KPowerUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into watts, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("W", KPowerUnit.BASE.symbol)
        assertEquals(unit, KPowerUnit.valueOf(unit.name))
    }

    /** The declared symbols of every power unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("W", KPowerUnit.WATT.symbol)
        assertEquals("PS", KPowerUnit.METRIC_HORSE_POWER.symbol)
        assertEquals("hp", KPowerUnit.MECHANICAL_HORSE_POWER.symbol)
        assertEquals("erg/s", KPowerUnit.ERG_PER_SECOND.symbol)
        assertEquals("VA", KPowerUnit.VOLT_AMPERE.symbol)
        assertEquals("var", KPowerUnit.VAR.symbol)
        assertEquals(KPowerUnit.WATT, KPowerUnit.BASE)
        assertEquals(6, KPowerUnit.entries.size)
    }

    /** The declared base values (relative to the watt) of every power unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KPowerUnit.WATT.baseValue, 1e-30)
        assertEquals(735.49875, KPowerUnit.METRIC_HORSE_POWER.baseValue, 1e-9)
        assertEquals(745.6998715822702, KPowerUnit.MECHANICAL_HORSE_POWER.baseValue, 1e-9)
        assertEquals(1.0e-7, KPowerUnit.ERG_PER_SECOND.baseValue, 1e-30)
        assertEquals(1.0, KPowerUnit.VOLT_AMPERE.baseValue, 1e-30)
        assertEquals(1.0, KPowerUnit.VAR.baseValue, 1e-30)
    }
}
