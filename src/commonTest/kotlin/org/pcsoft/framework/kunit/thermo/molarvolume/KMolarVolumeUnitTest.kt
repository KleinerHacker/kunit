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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molar volume token carries the correct m³/mol factor. */
class KMolarVolumeUnitTest {

    @Test
    fun `named tokens in cubic meters per mole`() {
        assertEquals(1.0, (1 of cubicMetersPerMole) into cubicMetersPerMole, 1e-12)
        assertEquals(1.0e-3, (1 of litersPerMole) into cubicMetersPerMole, 1e-15)
        assertEquals(1.0e-6, (1 of cubicCentimetersPerMole) into cubicMetersPerMole, 1e-18)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m^3/mol", KMolarVolumeUnit.CUBIC_METERS_PER_MOLE.symbol)
        assertEquals(1.0, KMolarVolumeUnit.CUBIC_METERS_PER_MOLE.baseValue, 1e-12)
        assertEquals("l/mol", KMolarVolumeUnit.LITERS_PER_MOLE.symbol)
        assertEquals(1.0e-3, KMolarVolumeUnit.LITERS_PER_MOLE.baseValue, 1e-15)
        assertEquals("cm^3/mol", KMolarVolumeUnit.CUBIC_CENTIMETERS_PER_MOLE.symbol)
        assertEquals(1.0e-6, KMolarVolumeUnit.CUBIC_CENTIMETERS_PER_MOLE.baseValue, 1e-18)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolarVolumeUnit.CUBIC_METERS_PER_MOLE, KMolarVolumeUnit.BASE)
        assertEquals(1.0, KMolarVolumeUnit.BASE.baseValue, 1e-12)
        assertEquals("m^3/mol", KMolarVolumeUnit.BASE.symbol)
    }

    /** The ideal gas constant of this package: 22.711 l/mol at STP. */
    @Test
    fun `ideal gas molar volume constant`() {
        assertEquals(0.02271095464, MOLAR_VOLUME_IDEAL_GAS_STP, 1e-14)
        assertEquals(22.71095464, (MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole) into litersPerMole, 1e-9)
    }
}
