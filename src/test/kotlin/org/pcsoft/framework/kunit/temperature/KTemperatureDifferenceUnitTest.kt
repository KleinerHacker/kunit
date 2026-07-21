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

package org.pcsoft.framework.kunit.temperature

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/** The concrete temperature difference unit ([KTemperatureDifferenceUnit]): its scale metadata. */
class KTemperatureDifferenceUnitTest {

    /** Kelvin is the linear base unit: symbol "ΔK" (distinct from absolute "K"), baseValue 1.0, BASE. */
    @Test
    fun `kelvin is the base unit`() {
        assertEquals("ΔK", KTemperatureDifferenceUnit.KELVIN.symbol)
        assertEquals(1.0, KTemperatureDifferenceUnit.KELVIN.baseValue, 1e-9)
        assertSame(KTemperatureDifferenceUnit.KELVIN, KTemperatureDifferenceUnit.BASE)
    }

    /** `ofKelvin` builds a difference carrying exactly the given kelvin interval. */
    @Test
    fun `ofKelvin builds the kelvin interval`() {
        assertEquals(20.0, KTemperatureDifference.ofKelvin(20).value, 1e-9)
    }
}
