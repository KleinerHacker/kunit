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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molality token carries the correct mol/kg factor. */
class KMolalityUnitTest {

    @Test
    fun `named tokens in moles per kilogram`() {
        assertEquals(1.0, (1 of molesPerKilogram) into molesPerKilogram, 1e-12)
        assertEquals(1.0e-3, (1 of millimolesPerKilogram) into molesPerKilogram, 1e-15)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("mol/kg", KMolalityUnit.MOLES_PER_KILOGRAM.symbol)
        assertEquals(1.0, KMolalityUnit.MOLES_PER_KILOGRAM.baseValue, 1e-12)
        assertEquals("mmol/kg", KMolalityUnit.MILLIMOLES_PER_KILOGRAM.symbol)
        assertEquals(1.0e-3, KMolalityUnit.MILLIMOLES_PER_KILOGRAM.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolalityUnit.MOLES_PER_KILOGRAM, KMolalityUnit.BASE)
        assertEquals(1.0, KMolalityUnit.BASE.baseValue, 1e-12)
        assertEquals("mol/kg", KMolalityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KMolalityUnit.entries.size)
        assertEquals(listOf("mol/kg", "mmol/kg"), KMolalityUnit.entries.map { it.symbol })
    }
}
