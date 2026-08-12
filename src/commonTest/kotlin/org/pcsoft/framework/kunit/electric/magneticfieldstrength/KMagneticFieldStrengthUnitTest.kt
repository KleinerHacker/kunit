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

package org.pcsoft.framework.kunit.electric.magneticfieldstrength

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete magnetic field strength units (`KMagneticFieldStrengthUnitBareValues`): every unit builds to
 * its A/m value, round-trips through `into`, and reads correctly against amperes per meter. Covers all bare
 * tokens (symbol, relative value).
 */
class KMagneticFieldStrengthUnitTest {

    private val tokens: List<Triple<String, KMagneticFieldStrengthUnitInstance, KMagneticFieldStrengthUnit>> = listOf(
        Triple("amperesPerMeter", amperesPerMeter, KMagneticFieldStrengthUnit.AMPERE_PER_METER),
        Triple("oersteds", oersteds, KMagneticFieldStrengthUnit.OERSTED),
        Triple("gilbertsPerCentimeter", gilbertsPerCentimeter, KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER),
        Triple("ampereTurnsPerInch", ampereTurnsPerInch, KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented A/m value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KMagneticFieldStrengthUnitInstance, case[2] as KMagneticFieldStrengthUnit)
    }

    private fun `unit conversion`(name: String, token: KMagneticFieldStrengthUnitInstance, unit: KMagneticFieldStrengthUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into amperesPerMeter, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("A/m", KMagneticFieldStrengthUnit.BASE.symbol)
        assertEquals(unit, KMagneticFieldStrengthUnit.valueOf(unit.name))
    }

    /** Every unit exposes its documented symbol. */
    @Test
    fun `unit symbols`() = forEachCase(tokenArgs()) { case ->
        `unit symbols`(case[0] as String, case[1] as KMagneticFieldStrengthUnitInstance, case[2] as KMagneticFieldStrengthUnit)
    }

    private fun `unit symbols`(name: String, token: KMagneticFieldStrengthUnitInstance, unit: KMagneticFieldStrengthUnit) {
        val expected = when (unit) {
            KMagneticFieldStrengthUnit.AMPERE_PER_METER -> "A/m"
            KMagneticFieldStrengthUnit.OERSTED -> "Oe"
            KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER -> "Gb/cm"
            KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH -> "At/in"
        }
        assertEquals(expected, unit.symbol)
    }
}
