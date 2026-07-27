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

package org.pcsoft.framework.kunit.chargedensity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The charge density unit enum: it carries only the base marker (the group has no bare tokens and no
 * prefix builders), so its single entry, symbol, base value and [KChargeDensityUnit.BASE] are asserted here.
 */
class KChargeDensityUnitTest {

    /** The single enum entry with its declared symbol and base value. */
    @Test
    fun `unit symbol and base value`() {
        assertEquals("C/m³", KChargeDensityUnit.COULOMB_PER_CUBIC_METER.symbol)
        assertEquals(1.0, KChargeDensityUnit.COULOMB_PER_CUBIC_METER.baseValue)
    }

    /** The group base unit is the coulomb per cubic meter, and the enum resolves by name. */
    @Test
    fun `base unit and enum access`() {
        assertEquals(KChargeDensityUnit.COULOMB_PER_CUBIC_METER, KChargeDensityUnit.BASE)
        assertEquals("C/m³", KChargeDensityUnit.BASE.symbol)
        assertEquals(1.0, KChargeDensityUnit.BASE.baseValue)
        assertEquals(
            KChargeDensityUnit.COULOMB_PER_CUBIC_METER,
            KChargeDensityUnit.valueOf("COULOMB_PER_CUBIC_METER"),
        )
        assertEquals(1, KChargeDensityUnit.entries.size)
    }
}
