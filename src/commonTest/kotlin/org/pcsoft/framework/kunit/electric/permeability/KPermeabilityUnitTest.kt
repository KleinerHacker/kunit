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

package org.pcsoft.framework.kunit.electric.permeability

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete permeability units (`KPermeabilityUnitBareValues`): every unit builds to its henry-per-meter
 * value, round-trips through `into`, and reads correctly against henries per meter. Covers all bare tokens
 * (symbol, relative value) plus the vacuum permeability constant.
 */
class KPermeabilityUnitTest {

    private val tokens: List<Triple<String, KPermeabilityUnitInstance, KPermeabilityUnit>> = listOf(
        Triple("henriesPerMeter", henriesPerMeter, KPermeabilityUnit.HENRY_PER_METER),
        Triple("henriesPerCentimeter", henriesPerCentimeter, KPermeabilityUnit.HENRY_PER_CENTIMETER),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented henry-per-meter value and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KPermeabilityUnitInstance, case[2] as KPermeabilityUnit)
    }

    private fun `unit conversion`(name: String, token: KPermeabilityUnitInstance, unit: KPermeabilityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into henriesPerMeter, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KPermeabilityUnit.valueOf(unit.name))
    }

    /** The declared symbols of every permeability unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("H/m", KPermeabilityUnit.HENRY_PER_METER.symbol)
        assertEquals("H/cm", KPermeabilityUnit.HENRY_PER_CENTIMETER.symbol)
        assertEquals(KPermeabilityUnit.HENRY_PER_METER, KPermeabilityUnit.BASE)
        assertEquals(2, KPermeabilityUnit.entries.size)
    }

    /** The declared base values (relative to the henry per meter) of every permeability unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KPermeabilityUnit.HENRY_PER_METER.baseValue, 1e-30)
        assertEquals(100.0, KPermeabilityUnit.HENRY_PER_CENTIMETER.baseValue, 1e-9)
    }

    /** The vacuum permeability constant and its bare token. */
    @Test
    fun `vacuum permeability constant`() {
        assertEquals(1.25663706127e-6, KPermeabilityUnit.VACUUM_PERMEABILITY, 1e-18)
        assertEquals(1.25663706127e-6, (1 of vacuumPermeability).value, 1e-18)
        assertEquals(1.25663706127e-6, (1 of vacuumPermeability) into henriesPerMeter, 1e-18)
    }
}
