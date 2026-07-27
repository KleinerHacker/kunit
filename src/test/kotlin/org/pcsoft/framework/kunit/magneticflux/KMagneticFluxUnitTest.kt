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

package org.pcsoft.framework.kunit.magneticflux

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete magnetic flux units (`KMagneticFluxUnitBareValues`): every unit builds to its weber value,
 * round-trips through `into`, and reads correctly against webers. Covers all bare tokens (symbol, relative
 * value).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxUnitTest {

    private val tokens: List<Triple<String, KMagneticFluxUnitInstance, KMagneticFluxUnit>> = listOf(
        Triple("webers", webers, KMagneticFluxUnit.WEBER),
        Triple("maxwells", maxwells, KMagneticFluxUnit.MAXWELL),
        Triple("unitPoles", unitPoles, KMagneticFluxUnit.UNIT_POLE),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented weber value, exposes its symbol, and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KMagneticFluxUnitInstance, unit: KMagneticFluxUnit) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into webers, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals("Wb", KMagneticFluxUnit.BASE.symbol)
        assertEquals(unit, KMagneticFluxUnit.valueOf(unit.name))
    }

    /** The declared symbols of every magnetic flux unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("Wb", KMagneticFluxUnit.WEBER.symbol)
        assertEquals("Mx", KMagneticFluxUnit.MAXWELL.symbol)
        assertEquals("pole", KMagneticFluxUnit.UNIT_POLE.symbol)
        assertEquals(KMagneticFluxUnit.WEBER, KMagneticFluxUnit.BASE)
        assertEquals(3, KMagneticFluxUnit.entries.size)
    }

    /** The declared base values (relative to the weber) of every magnetic flux unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KMagneticFluxUnit.WEBER.baseValue, 1e-30)
        assertEquals(1.0e-8, KMagneticFluxUnit.MAXWELL.baseValue, 1e-30)
        assertEquals(1.2566370614359173e-7, KMagneticFluxUnit.UNIT_POLE.baseValue, 1e-30)
    }
}
