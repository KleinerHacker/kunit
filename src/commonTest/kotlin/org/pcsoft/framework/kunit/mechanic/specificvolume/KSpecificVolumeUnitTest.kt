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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named specific-volume tokens carry the correct m³/kg factor. */
class KSpecificVolumeUnitTest {

    @Test
    fun `named tokens in cubic meters per kilogram`() {
        assertEquals(1.0, (1 of cubicMetersPerKilogram) into cubicMetersPerKilogram, 1e-9)
        assertEquals(0.001, (1 of litersPerKilogram) into cubicMetersPerKilogram, 1e-12)
        assertEquals(0.001, (1 of cubicCentimetersPerGram) into cubicMetersPerKilogram, 1e-12)
        assertEquals(
            0.028316846592 / 0.45359237,
            (1 of cubicFeetPerPound) into cubicMetersPerKilogram,
            1e-9,
        )
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("m^3/kg", KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM.symbol)
        assertEquals("l/kg", KSpecificVolumeUnit.LITERS_PER_KILOGRAM.symbol)
        assertEquals("cm^3/g", KSpecificVolumeUnit.CUBIC_CENTIMETERS_PER_GRAM.symbol)
        assertEquals("ft^3/lb", KSpecificVolumeUnit.CUBIC_FEET_PER_POUND.symbol)
        assertEquals(1.0, KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM.baseValue, 1e-12)
        assertEquals(1.0e-3, KSpecificVolumeUnit.LITERS_PER_KILOGRAM.baseValue, 1e-15)
        assertEquals(1.0e-3, KSpecificVolumeUnit.CUBIC_CENTIMETERS_PER_GRAM.baseValue, 1e-15)
        assertEquals(
            0.028316846592 / 0.45359237,
            KSpecificVolumeUnit.CUBIC_FEET_PER_POUND.baseValue,
            1e-12,
        )
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM, KSpecificVolumeUnit.BASE)
        assertEquals(1.0, KSpecificVolumeUnit.BASE.baseValue, 1e-12)
        assertEquals("m^3/kg", KSpecificVolumeUnit.BASE.symbol)
    }
}
