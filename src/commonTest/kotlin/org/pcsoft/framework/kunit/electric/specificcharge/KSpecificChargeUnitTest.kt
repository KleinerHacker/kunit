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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named specific charge token carries the correct C/kg factor. */
class KSpecificChargeUnitTest {

    @Test
    fun `named tokens in coulombs per kilogram`() {
        assertEquals(1.0, (1 of coulombsPerKilogram) into coulombsPerKilogram, 1e-12)
        assertEquals(2.58e-4, (1 of roentgens) into coulombsPerKilogram, 1e-16)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("C/kg", KSpecificChargeUnit.COULOMB_PER_KILOGRAM.symbol)
        assertEquals(1.0, KSpecificChargeUnit.COULOMB_PER_KILOGRAM.baseValue, 1e-12)
        assertEquals("R", KSpecificChargeUnit.ROENTGEN.symbol)
        assertEquals(2.58e-4, KSpecificChargeUnit.ROENTGEN.baseValue, 1e-16)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSpecificChargeUnit.COULOMB_PER_KILOGRAM, KSpecificChargeUnit.BASE)
        assertEquals(1.0, KSpecificChargeUnit.BASE.baseValue, 1e-12)
        assertEquals("C/kg", KSpecificChargeUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KSpecificChargeUnit.entries.size)
        assertEquals(listOf("C/kg", "R"), KSpecificChargeUnit.entries.map { it.symbol })
    }

    /** The electron's charge-to-mass ratio, the quantity Thomson measured. */
    @Test
    fun `electron specific charge constant`() {
        assertEquals(1.75882001076e11, ELECTRON_SPECIFIC_CHARGE, 1e1)
        assertEquals(
            1.75882001076e11,
            (ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram) into coulombsPerKilogram,
            1e1,
        )
    }
}
