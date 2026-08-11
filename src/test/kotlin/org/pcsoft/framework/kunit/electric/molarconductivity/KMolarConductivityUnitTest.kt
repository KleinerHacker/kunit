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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molar conductivity token carries the correct S·m²/mol factor. */
class KMolarConductivityUnitTest {

    @Test
    fun `named tokens in siemens square meters per mole`() {
        assertEquals(1.0, (1 of siemensSquareMetersPerMole) into siemensSquareMetersPerMole, 1e-12)
        assertEquals(1.0e-4, (1 of siemensSquareCentimetersPerMole) into siemensSquareMetersPerMole, 1e-16)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("S*m^2/mol", KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE.symbol)
        assertEquals(1.0, KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE.baseValue, 1e-12)
        assertEquals("S*cm^2/mol", KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE.symbol)
        assertEquals(1.0e-4, KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE.baseValue, 1e-16)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE, KMolarConductivityUnit.BASE)
        assertEquals(1.0, KMolarConductivityUnit.BASE.baseValue, 1e-12)
        assertEquals("S*m^2/mol", KMolarConductivityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KMolarConductivityUnit.entries.size)
        assertEquals(
            listOf("S*m^2/mol", "S*cm^2/mol"),
            KMolarConductivityUnit.entries.map { it.symbol },
        )
    }
}
