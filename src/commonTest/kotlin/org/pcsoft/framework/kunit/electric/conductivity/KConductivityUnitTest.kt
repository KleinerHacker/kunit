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

package org.pcsoft.framework.kunit.electric.conductivity

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete conductivity units (`KConductivityUnitBareValues`): every unit builds to its siemens-per-meter
 * value, round-trips through `into`, and reads correctly against siemens per meter. Covers all bare tokens
 * (symbol, relative value).
 */
class KConductivityUnitTest {

    private val tokens: List<Triple<String, KConductivityUnitInstance, KConductivityUnit>> = listOf(
        Triple("siemensPerMeter", siemensPerMeter, KConductivityUnit.SIEMENS_PER_METER),
        Triple("siemensPerCentimeter", siemensPerCentimeter, KConductivityUnit.SIEMENS_PER_CENTIMETER),
        Triple(
            "microsiemensPerCentimeter",
            microsiemensPerCentimeter,
            KConductivityUnit.MICROSIEMENS_PER_CENTIMETER,
        ),
        Triple("megasiemensPerMeter", megasiemensPerMeter, KConductivityUnit.MEGASIEMENS_PER_METER),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented S/m value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KConductivityUnitInstance, case[2] as KConductivityUnit)
    }

    private fun `unit conversion`(name: String, token: KConductivityUnitInstance, unit: KConductivityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into siemensPerMeter, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("S/m", KConductivityUnit.BASE.symbol)
        assertEquals(unit, KConductivityUnit.valueOf(unit.name))
    }

    /** The declared symbols of every conductivity unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("S/m", KConductivityUnit.SIEMENS_PER_METER.symbol)
        assertEquals("S/cm", KConductivityUnit.SIEMENS_PER_CENTIMETER.symbol)
        assertEquals("µS/cm", KConductivityUnit.MICROSIEMENS_PER_CENTIMETER.symbol)
        assertEquals("MS/m", KConductivityUnit.MEGASIEMENS_PER_METER.symbol)
        assertEquals(KConductivityUnit.SIEMENS_PER_METER, KConductivityUnit.BASE)
        assertEquals(4, KConductivityUnit.entries.size)
    }

    /** The declared base values (relative to siemens per meter) of every conductivity unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KConductivityUnit.SIEMENS_PER_METER.baseValue, 1e-30)
        assertEquals(100.0, KConductivityUnit.SIEMENS_PER_CENTIMETER.baseValue, 1e-12)
        assertEquals(1e-4, KConductivityUnit.MICROSIEMENS_PER_CENTIMETER.baseValue, 1e-30)
        assertEquals(1e6, KConductivityUnit.MEGASIEMENS_PER_METER.baseValue, 1e-6)
    }
}
