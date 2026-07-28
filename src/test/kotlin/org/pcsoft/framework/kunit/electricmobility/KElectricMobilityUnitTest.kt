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

package org.pcsoft.framework.kunit.electricmobility

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete electric mobility units (`KElectricMobilityUnitBareValues`): every unit builds to its
 * m²/(V·s) value, round-trips through `into`, and reads correctly against the base unit. Covers all bare
 * tokens (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricMobilityUnitTest {

    private val tokens: List<Triple<String, KElectricMobilityUnitInstance, KElectricMobilityUnit>> = listOf(
        Triple(
            "squareMetersPerVoltSecond",
            squareMetersPerVoltSecond,
            KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND,
        ),
        Triple(
            "squareCentimetersPerVoltSecond",
            squareCentimetersPerVoltSecond,
            KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND,
        ),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented m²/(V·s) value and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KElectricMobilityUnitInstance, unit: KElectricMobilityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into squareMetersPerVoltSecond, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KElectricMobilityUnit.valueOf(unit.name))
    }

    /** The declared symbols of every electric mobility unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("m²/(V·s)", KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND.symbol)
        assertEquals("cm²/(V·s)", KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND.symbol)
        assertEquals(KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND, KElectricMobilityUnit.BASE)
        assertEquals(2, KElectricMobilityUnit.entries.size)
    }

    /** The declared base values (relative to m²/(V·s)) of every electric mobility unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND.baseValue, 1e-30)
        assertEquals(1.0e-4, KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND.baseValue, 1e-20)
    }

    /** The real-world reading: the electron mobility in silicon is about 1400 cm²/(V·s). */
    @Test
    fun `silicon electron mobility real world`() {
        assertEquals(0.14, (1400 of squareCentimetersPerVoltSecond).value, 1e-12)
    }
}
