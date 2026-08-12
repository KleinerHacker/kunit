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

package org.pcsoft.framework.kunit.electric.magneticflux

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete magnetic flux units (`KMagneticFluxUnitBareValues`): every unit builds to its weber value,
 * round-trips through `into`, and reads correctly against webers. Covers all bare tokens (symbol, relative
 * value).
 */
class KMagneticFluxUnitTest {

    private val tokens: List<Triple<String, KMagneticFluxUnitInstance, KMagneticFluxUnit>> = listOf(
        Triple("webers", webers, KMagneticFluxUnit.WEBER),
        Triple("maxwells", maxwells, KMagneticFluxUnit.MAXWELL),
        Triple("unitPoles", unitPoles, KMagneticFluxUnit.UNIT_POLE),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented weber value, exposes its symbol, and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KMagneticFluxUnitInstance, case[2] as KMagneticFluxUnit)
    }

    private fun `unit conversion`(name: String, token: KMagneticFluxUnitInstance, unit: KMagneticFluxUnit) {
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
