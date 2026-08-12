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

package org.pcsoft.framework.kunit.electric.permittivity

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete permittivity units (`KPermittivityUnitBareValues`): every unit builds to its farad-per-meter
 * value, round-trips through `into`, and reads correctly against farads per meter. Covers all bare tokens
 * (symbol, relative value) plus the vacuum permittivity constant.
 */
class KPermittivityUnitTest {

    private val tokens: List<Triple<String, KPermittivityUnitInstance, KPermittivityUnit>> = listOf(
        Triple("faradsPerMeter", faradsPerMeter, KPermittivityUnit.FARAD_PER_METER),
        Triple("faradsPerCentimeter", faradsPerCentimeter, KPermittivityUnit.FARAD_PER_CENTIMETER),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented farad-per-meter value and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KPermittivityUnitInstance, case[2] as KPermittivityUnit)
    }

    private fun `unit conversion`(name: String, token: KPermittivityUnitInstance, unit: KPermittivityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into faradsPerMeter, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KPermittivityUnit.valueOf(unit.name))
    }

    /** The declared symbols of every permittivity unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("F/m", KPermittivityUnit.FARAD_PER_METER.symbol)
        assertEquals("F/cm", KPermittivityUnit.FARAD_PER_CENTIMETER.symbol)
        assertEquals(KPermittivityUnit.FARAD_PER_METER, KPermittivityUnit.BASE)
        assertEquals(2, KPermittivityUnit.entries.size)
    }

    /** The declared base values (relative to the farad per meter) of every permittivity unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KPermittivityUnit.FARAD_PER_METER.baseValue, 1e-30)
        assertEquals(100.0, KPermittivityUnit.FARAD_PER_CENTIMETER.baseValue, 1e-9)
    }

    /** The vacuum permittivity constant and its bare token. */
    @Test
    fun `vacuum permittivity constant`() {
        assertEquals(8.8541878188e-12, KPermittivityUnit.VACUUM_PERMITTIVITY, 1e-24)
        assertEquals(8.8541878188e-12, (1 of vacuumPermittivity).value, 1e-24)
        assertEquals(8.8541878188e-12, (1 of vacuumPermittivity) into faradsPerMeter, 1e-24)
    }
}
