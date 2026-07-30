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

package org.pcsoft.framework.kunit.thermo.diffusivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named thermal diffusivity token carries the correct m²/s factor. */
class KThermalDiffusivityUnitTest {

    @Test
    fun `named tokens in square meters per second`() {
        assertEquals(1.0, (1 of squareMetersPerSecond) into squareMetersPerSecond, 1e-12)
        assertEquals(1.0e-6, (1 of squareMillimetersPerSecond) into squareMetersPerSecond, 1e-18)
        assertEquals(
            0.3048 * 0.3048 / 3600.0,
            (1 of squareFeetPerHour) into squareMetersPerSecond,
            1e-15,
        )
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m²/s", KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND.symbol)
        assertEquals(1.0, KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND.baseValue, 1e-12)
        assertEquals("mm²/s", KThermalDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND.symbol)
        assertEquals(1.0e-6, KThermalDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND.baseValue, 1e-18)
        assertEquals("ft²/h", KThermalDiffusivityUnit.SQUARE_FOOT_PER_HOUR.symbol)
        assertEquals(
            0.3048 * 0.3048 / 3600.0,
            KThermalDiffusivityUnit.SQUARE_FOOT_PER_HOUR.baseValue,
            1e-15,
        )
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND, KThermalDiffusivityUnit.BASE)
        assertEquals(1.0, KThermalDiffusivityUnit.BASE.baseValue, 1e-12)
        assertEquals("m²/s", KThermalDiffusivityUnit.BASE.symbol)
    }
}
