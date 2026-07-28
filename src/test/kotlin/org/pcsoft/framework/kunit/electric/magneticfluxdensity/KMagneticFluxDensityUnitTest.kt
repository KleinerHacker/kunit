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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete magnetic flux density units (`KMagneticFluxDensityUnitBareValues`): every unit builds to
 * its tesla value, round-trips through `into`, and reads correctly against teslas. Covers all bare tokens
 * (symbol, relative value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxDensityUnitTest {

    private val tokens: List<Triple<String, KMagneticFluxDensityUnitInstance, KMagneticFluxDensityUnit>> = listOf(
        Triple("teslas", teslas, KMagneticFluxDensityUnit.TESLA),
        Triple("webersPerSquareMeter", webersPerSquareMeter, KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER),
        Triple("gauss", gauss, KMagneticFluxDensityUnit.GAUSS),
        Triple("gammas", gammas, KMagneticFluxDensityUnit.GAMMA),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented tesla value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KMagneticFluxDensityUnitInstance, unit: KMagneticFluxDensityUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into teslas, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("T", KMagneticFluxDensityUnit.BASE.symbol)
        assertEquals(unit, KMagneticFluxDensityUnit.valueOf(unit.name))
    }

    /** The declared symbols and base values of every magnetic flux density unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("T", KMagneticFluxDensityUnit.TESLA.symbol)
        assertEquals("Wb/m²", KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER.symbol)
        assertEquals("G", KMagneticFluxDensityUnit.GAUSS.symbol)
        assertEquals("γ", KMagneticFluxDensityUnit.GAMMA.symbol)
        assertEquals(1.0, KMagneticFluxDensityUnit.TESLA.baseValue)
        assertEquals(1.0, KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER.baseValue)
        assertEquals(1e-4, KMagneticFluxDensityUnit.GAUSS.baseValue)
        assertEquals(1e-9, KMagneticFluxDensityUnit.GAMMA.baseValue)
        assertEquals(KMagneticFluxDensityUnit.TESLA, KMagneticFluxDensityUnit.BASE)
    }
}
