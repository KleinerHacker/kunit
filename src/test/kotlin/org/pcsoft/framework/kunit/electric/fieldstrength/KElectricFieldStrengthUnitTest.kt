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

package org.pcsoft.framework.kunit.electric.fieldstrength

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete electric field strength units (`KElectricFieldStrengthUnitBareValues`): every unit builds to
 * its volt-per-meter value, round-trips through `into`, and reads correctly against volts per meter. Covers
 * all bare tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricFieldStrengthUnitTest {

    private val tokens: List<Triple<String, KElectricFieldStrengthUnitInstance, KElectricFieldStrengthUnit>> =
        listOf(
            Triple("voltsPerMeter", voltsPerMeter, KElectricFieldStrengthUnit.VOLT_PER_METER),
            Triple("voltsPerCentimeter", voltsPerCentimeter, KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER),
            Triple(
                "statvoltsPerCentimeter",
                statvoltsPerCentimeter,
                KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER,
            ),
        )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented volt-per-meter value and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(
        name: String,
        token: KElectricFieldStrengthUnitInstance,
        unit: KElectricFieldStrengthUnit,
    ) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into voltsPerMeter, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KElectricFieldStrengthUnit.valueOf(unit.name))
    }

    /** The declared symbols of every electric field strength unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("V/m", KElectricFieldStrengthUnit.VOLT_PER_METER.symbol)
        assertEquals("V/cm", KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.symbol)
        assertEquals("statV/cm", KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.symbol)
        assertEquals(KElectricFieldStrengthUnit.VOLT_PER_METER, KElectricFieldStrengthUnit.BASE)
        assertEquals(3, KElectricFieldStrengthUnit.entries.size)
    }

    /** The declared base values (relative to the volt per meter) of every electric field strength unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KElectricFieldStrengthUnit.VOLT_PER_METER.baseValue, 1e-30)
        assertEquals(100.0, KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue, 1e-9)
        assertEquals(29979.2458, KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.baseValue, 1e-6)
    }
}
