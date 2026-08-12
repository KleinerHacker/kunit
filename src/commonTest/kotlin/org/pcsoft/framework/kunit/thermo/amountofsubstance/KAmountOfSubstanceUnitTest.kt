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

package org.pcsoft.framework.kunit.thermo.amountofsubstance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named amount-of-substance token carries the correct mole factor. */
class KAmountOfSubstanceUnitTest {

    @Test
    fun `named tokens in moles`() {
        assertEquals(1.0, (1 of moles) into moles, 1e-12)
        assertEquals(453.59237, (1 of poundMoles) into moles, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("mol", KAmountOfSubstanceUnit.MOLE.symbol)
        assertEquals(1.0, KAmountOfSubstanceUnit.MOLE.baseValue, 1e-12)
        assertEquals("lbmol", KAmountOfSubstanceUnit.POUND_MOLE.symbol)
        assertEquals(453.59237, KAmountOfSubstanceUnit.POUND_MOLE.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KAmountOfSubstanceUnit.MOLE, KAmountOfSubstanceUnit.BASE)
        assertEquals(1.0, KAmountOfSubstanceUnit.BASE.baseValue, 1e-12)
        assertEquals("mol", KAmountOfSubstanceUnit.BASE.symbol)
    }

    @Test
    fun `avogadro constant and particle count`() {
        assertEquals(6.02214076e23, AVOGADRO_CONSTANT, 1e12)
        assertEquals(2.0 * 6.02214076e23, (2 of moles).particleCount(), 1e12)
    }
}
