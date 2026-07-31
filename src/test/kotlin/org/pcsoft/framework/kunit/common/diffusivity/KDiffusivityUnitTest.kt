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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named diffusivity token carries the correct m²/s factor. */
class KDiffusivityUnitTest {

    @Test
    fun `named tokens in square meters per second`() {
        assertEquals(1.0, (1 of squareMetersPerSecond) into squareMetersPerSecond, 1e-12)
        assertEquals(1.0e-6, (1 of squareMillimetersPerSecond) into squareMetersPerSecond, 1e-18)
        assertEquals(
            0.3048 * 0.3048 / 3600.0,
            (1 of squareFeetPerHour) into squareMetersPerSecond,
            1e-15,
        )
        assertEquals(1.0e-4, (1 of stokes) into squareMetersPerSecond, 1e-16)
        assertEquals(1.0e-6, (1 of centistokes) into squareMetersPerSecond, 1e-18)
    }

    /** The kinematic-viscosity readings coincide with the metric ones (1 cSt = 1 mm²/s). */
    @Test
    fun `stokes readings`() {
        assertEquals(100.0, (1 of stokes) into centistokes, 1e-9)
        assertEquals(1.0, (1 of centistokes) into squareMillimetersPerSecond, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m²/s", KDiffusivityUnit.SQUARE_METER_PER_SECOND.symbol)
        assertEquals(1.0, KDiffusivityUnit.SQUARE_METER_PER_SECOND.baseValue, 1e-12)
        assertEquals("mm²/s", KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND.symbol)
        assertEquals(1.0e-6, KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND.baseValue, 1e-18)
        assertEquals("ft²/h", KDiffusivityUnit.SQUARE_FOOT_PER_HOUR.symbol)
        assertEquals(
            0.3048 * 0.3048 / 3600.0,
            KDiffusivityUnit.SQUARE_FOOT_PER_HOUR.baseValue,
            1e-15,
        )
        assertEquals("St", KDiffusivityUnit.STOKES.symbol)
        assertEquals(1.0e-4, KDiffusivityUnit.STOKES.baseValue, 1e-16)
        assertEquals("cSt", KDiffusivityUnit.CENTISTOKES.symbol)
        assertEquals(1.0e-6, KDiffusivityUnit.CENTISTOKES.baseValue, 1e-18)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KDiffusivityUnit.SQUARE_METER_PER_SECOND, KDiffusivityUnit.BASE)
        assertEquals(1.0, KDiffusivityUnit.BASE.baseValue, 1e-12)
        assertEquals("m²/s", KDiffusivityUnit.BASE.symbol)
    }
}
