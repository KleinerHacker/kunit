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

package org.pcsoft.framework.kunit.electric.dipolemoment

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete electric dipole moment units (`KElectricDipoleMomentUnitBareValues`): every unit builds to its
 * coulomb-meter value, round-trips through `into`, and reads correctly against coulomb meters. Covers all
 * bare tokens (symbol, relative value).
 */
class KElectricDipoleMomentUnitTest {

    private val tokens: List<Triple<String, KElectricDipoleMomentUnitInstance, KElectricDipoleMomentUnit>> =
        listOf(
            Triple("coulombMeters", coulombMeters, KElectricDipoleMomentUnit.COULOMB_METER),
            Triple("debyes", debyes, KElectricDipoleMomentUnit.DEBYE),
        )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-45)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented coulomb-meter value and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KElectricDipoleMomentUnitInstance, case[2] as KElectricDipoleMomentUnit)
    }

    private fun `unit conversion`(
        name: String,
        token: KElectricDipoleMomentUnitInstance,
        unit: KElectricDipoleMomentUnit,
    ) {
        val base = unit.baseValue
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into coulombMeters, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
        assertEquals(unit, KElectricDipoleMomentUnit.valueOf(unit.name))
    }

    /** The declared symbols of every electric dipole moment unit. */
    @Test
    fun `unit symbols`() {
        assertEquals("C·m", KElectricDipoleMomentUnit.COULOMB_METER.symbol)
        assertEquals("D", KElectricDipoleMomentUnit.DEBYE.symbol)
        assertEquals(KElectricDipoleMomentUnit.COULOMB_METER, KElectricDipoleMomentUnit.BASE)
        assertEquals(2, KElectricDipoleMomentUnit.entries.size)
    }

    /** The declared base values (relative to the coulomb meter) of every electric dipole moment unit. */
    @Test
    fun `unit base values`() {
        assertEquals(1.0, KElectricDipoleMomentUnit.COULOMB_METER.baseValue, 1e-30)
        assertEquals(3.335640952e-30, KElectricDipoleMomentUnit.DEBYE.baseValue, 1e-45)
    }

    /** The real-world reading: the water molecule has a dipole moment of 1.85 D. */
    @Test
    fun `water molecule real world`() {
        assertEquals(6.1709357612e-30, (1.85 of debyes).value, 1e-42)
    }
}
