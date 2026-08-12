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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named dose rate token carries the correct Gy/s factor. */
class KDoseRateUnitTest {

    @Test
    fun `named tokens in grays per second`() {
        assertEquals(1.0, (1 of graysPerSecond) into graysPerSecond, 1e-12)
        assertEquals(1.0 / 3600.0, (1 of graysPerHour) into graysPerSecond, 1e-15)
        assertEquals(1.0, (1 of sievertsPerSecond) into graysPerSecond, 1e-12)
        assertEquals(1.0 / 3600.0, (1 of sievertsPerHour) into graysPerSecond, 1e-15)
    }

    /** Gray and sievert share one dimension, so the spellings coincide numerically. */
    @Test
    fun `gray and sievert spellings coincide`() {
        assertEquals(1 of graysPerSecond, 1 of sievertsPerSecond)
        assertEquals(1 of graysPerHour, 1 of sievertsPerHour)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("Gy/s", KDoseRateUnit.GRAY_PER_SECOND.symbol)
        assertEquals(1.0, KDoseRateUnit.GRAY_PER_SECOND.baseValue, 1e-12)
        assertEquals("Gy/h", KDoseRateUnit.GRAY_PER_HOUR.symbol)
        assertEquals(1.0 / 3600.0, KDoseRateUnit.GRAY_PER_HOUR.baseValue, 1e-15)
        assertEquals("Sv/s", KDoseRateUnit.SIEVERT_PER_SECOND.symbol)
        assertEquals(1.0, KDoseRateUnit.SIEVERT_PER_SECOND.baseValue, 1e-12)
        assertEquals("Sv/h", KDoseRateUnit.SIEVERT_PER_HOUR.symbol)
        assertEquals(1.0 / 3600.0, KDoseRateUnit.SIEVERT_PER_HOUR.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KDoseRateUnit.GRAY_PER_SECOND, KDoseRateUnit.BASE)
        assertEquals(1.0, KDoseRateUnit.BASE.baseValue, 1e-12)
        assertEquals("Gy/s", KDoseRateUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(4, KDoseRateUnit.entries.size)
        assertEquals(
            listOf("Gy/s", "Gy/h", "Sv/s", "Sv/h"),
            KDoseRateUnit.entries.map { it.symbol },
        )
    }
}
