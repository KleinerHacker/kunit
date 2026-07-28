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

package org.pcsoft.framework.kunit.electric.linearchargedensity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The linear charge density unit enum: it carries only the base marker (the group has no bare tokens and no
 * prefix builders), so its single entry, symbol, base value and [KLinearChargeDensityUnit.BASE] are asserted
 * here.
 */
class KLinearChargeDensityUnitTest {

    /** The single enum entry with its declared symbol and base value. */
    @Test
    fun `unit symbol and base value`() {
        assertEquals("C/m", KLinearChargeDensityUnit.COULOMB_PER_METER.symbol)
        assertEquals(1.0, KLinearChargeDensityUnit.COULOMB_PER_METER.baseValue)
    }

    /** The group base unit is the coulomb per meter, and the enum resolves by name. */
    @Test
    fun `base unit and enum access`() {
        assertEquals(KLinearChargeDensityUnit.COULOMB_PER_METER, KLinearChargeDensityUnit.BASE)
        assertEquals("C/m", KLinearChargeDensityUnit.BASE.symbol)
        assertEquals(1.0, KLinearChargeDensityUnit.BASE.baseValue)
        assertEquals(
            KLinearChargeDensityUnit.COULOMB_PER_METER,
            KLinearChargeDensityUnit.valueOf("COULOMB_PER_METER"),
        )
        assertEquals(1, KLinearChargeDensityUnit.entries.size)
    }
}
