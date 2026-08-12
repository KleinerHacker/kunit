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

package org.pcsoft.framework.kunit.thermo.molarmass

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molar mass token carries the correct g/mol factor. */
class KMolarMassUnitTest {

    @Test
    fun `named tokens in grams per mole`() {
        assertEquals(1.0, (1 of gramsPerMole) into gramsPerMole, 1e-12)
        assertEquals(1000.0, (1 of kilogramsPerMole) into gramsPerMole, 1e-9)
        assertEquals(1.0, (1 of poundsPerPoundMole) into gramsPerMole, 1e-12)
        assertEquals(1.00000000105, (1 of daltonsPerEntity) into gramsPerMole, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("g/mol", KMolarMassUnit.GRAM_PER_MOLE.symbol)
        assertEquals(1.0, KMolarMassUnit.GRAM_PER_MOLE.baseValue, 1e-12)
        assertEquals("kg/mol", KMolarMassUnit.KILOGRAM_PER_MOLE.symbol)
        assertEquals(1000.0, KMolarMassUnit.KILOGRAM_PER_MOLE.baseValue, 1e-12)
        assertEquals("lb/lbmol", KMolarMassUnit.POUND_PER_POUND_MOLE.symbol)
        assertEquals(1.0, KMolarMassUnit.POUND_PER_POUND_MOLE.baseValue, 1e-12)
        assertEquals("Da", KMolarMassUnit.DALTON_PER_ENTITY.symbol)
        assertEquals(1.00000000105, KMolarMassUnit.DALTON_PER_ENTITY.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolarMassUnit.GRAM_PER_MOLE, KMolarMassUnit.BASE)
        assertEquals(1.0, KMolarMassUnit.BASE.baseValue, 1e-12)
        assertEquals("g/mol", KMolarMassUnit.BASE.symbol)
    }
}
