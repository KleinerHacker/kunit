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

package org.pcsoft.framework.kunit.thermo.molarenergy

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molar energy token carries the correct J/mol factor. */
class KMolarEnergyUnitTest {

    @Test
    fun `named tokens in joules per mole`() {
        assertEquals(1.0, (1 of joulesPerMole) into joulesPerMole, 1e-12)
        assertEquals(4.184, (1 of caloriesPerMole) into joulesPerMole, 1e-9)
        assertEquals(96485.33212, (1 of electronVoltsPerEntity) into joulesPerMole, 1e-6)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/mol", KMolarEnergyUnit.JOULE_PER_MOLE.symbol)
        assertEquals(1.0, KMolarEnergyUnit.JOULE_PER_MOLE.baseValue, 1e-12)
        assertEquals("cal/mol", KMolarEnergyUnit.CALORIE_PER_MOLE.symbol)
        assertEquals(4.184, KMolarEnergyUnit.CALORIE_PER_MOLE.baseValue, 1e-12)
        assertEquals("eV/entity", KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY.symbol)
        assertEquals(96485.33212, KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY.baseValue, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolarEnergyUnit.JOULE_PER_MOLE, KMolarEnergyUnit.BASE)
        assertEquals(1.0, KMolarEnergyUnit.BASE.baseValue, 1e-12)
        assertEquals("J/mol", KMolarEnergyUnit.BASE.symbol)
    }
}
