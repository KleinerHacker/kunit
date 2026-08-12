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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named reciprocal length token carries the correct m⁻¹ factor. */
class KReciprocalLengthUnitTest {

    @Test
    fun `named tokens in reciprocal meters`() {
        assertEquals(1.0, (1 of reciprocalMeters) into reciprocalMeters, 1e-12)
        assertEquals(1.0, (1 of dioptres) into reciprocalMeters, 1e-12)
        assertEquals(100.0, (1 of reciprocalCentimeters) into reciprocalMeters, 1e-9)
        assertEquals(100.0, (1 of kaysers) into reciprocalMeters, 1e-9)
    }

    /** `dioptres` and `kaysers` are alternative spellings, not units of their own. */
    @Test
    fun `alternative spellings`() {
        assertEquals(1 of reciprocalMeters, 1 of dioptres)
        assertEquals(1 of reciprocalCentimeters, 1 of kaysers)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("1/m", KReciprocalLengthUnit.RECIPROCAL_METER.symbol)
        assertEquals(1.0, KReciprocalLengthUnit.RECIPROCAL_METER.baseValue, 1e-12)
        assertEquals("dpt", KReciprocalLengthUnit.DIOPTRE.symbol)
        assertEquals(1.0, KReciprocalLengthUnit.DIOPTRE.baseValue, 1e-12)
        assertEquals("1/cm", KReciprocalLengthUnit.RECIPROCAL_CENTIMETER.symbol)
        assertEquals(100.0, KReciprocalLengthUnit.RECIPROCAL_CENTIMETER.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KReciprocalLengthUnit.RECIPROCAL_METER, KReciprocalLengthUnit.BASE)
        assertEquals(1.0, KReciprocalLengthUnit.BASE.baseValue, 1e-12)
        assertEquals("1/m", KReciprocalLengthUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KReciprocalLengthUnit.entries.size)
        assertEquals(listOf("1/m", "dpt", "1/cm"), KReciprocalLengthUnit.entries.map { it.symbol })
    }
}
