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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named catalytic activity token carries the correct katal factor. */
class KCatalyticActivityUnitTest {

    @Test
    fun `named tokens in katals`() {
        assertEquals(1.0, (1 of katals) into katals, 1e-12)
        assertEquals(1.0e-6 / 60.0, (1 of enzymeUnits) into katals, 1e-18)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("kat", KCatalyticActivityUnit.KATAL.symbol)
        assertEquals(1.0, KCatalyticActivityUnit.KATAL.baseValue, 1e-12)
        assertEquals("U", KCatalyticActivityUnit.ENZYME_UNIT.symbol)
        assertEquals(1.0e-6 / 60.0, KCatalyticActivityUnit.ENZYME_UNIT.baseValue, 1e-18)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KCatalyticActivityUnit.KATAL, KCatalyticActivityUnit.BASE)
        assertEquals(1.0, KCatalyticActivityUnit.BASE.baseValue, 1e-12)
        assertEquals("kat", KCatalyticActivityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KCatalyticActivityUnit.entries.size)
        assertEquals(listOf("kat", "U"), KCatalyticActivityUnit.entries.map { it.symbol })
    }
}
