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

package org.pcsoft.framework.kunit.electric.currentdensity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The current density unit enum: it carries only the base marker (the group has no bare tokens and no prefix
 * builders), so its single entry, symbol, base value and [KCurrentDensityUnit.BASE] are asserted here.
 */
class KCurrentDensityUnitTest {

    /** The single enum entry with its declared symbol and base value. */
    @Test
    fun `unit symbol and base value`() {
        assertEquals("A/m²", KCurrentDensityUnit.AMPERE_PER_SQUARE_METER.symbol)
        assertEquals(1.0, KCurrentDensityUnit.AMPERE_PER_SQUARE_METER.baseValue)
    }

    /** The group base unit is the ampere per square meter, and the enum resolves by name. */
    @Test
    fun `base unit and enum access`() {
        assertEquals(KCurrentDensityUnit.AMPERE_PER_SQUARE_METER, KCurrentDensityUnit.BASE)
        assertEquals("A/m²", KCurrentDensityUnit.BASE.symbol)
        assertEquals(1.0, KCurrentDensityUnit.BASE.baseValue)
        assertEquals(
            KCurrentDensityUnit.AMPERE_PER_SQUARE_METER,
            KCurrentDensityUnit.valueOf("AMPERE_PER_SQUARE_METER"),
        )
        assertEquals(1, KCurrentDensityUnit.entries.size)
    }
}
