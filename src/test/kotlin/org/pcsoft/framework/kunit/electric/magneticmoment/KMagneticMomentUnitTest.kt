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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named magnetic moment token carries the correct A·m² factor. */
class KMagneticMomentUnitTest {

    @Test
    fun `named tokens in ampere square meters`() {
        assertEquals(1.0, (1 of ampereSquareMeters) into ampereSquareMeters, 1e-12)
        assertEquals(1.0, (1 of joulesPerTesla) into ampereSquareMeters, 1e-12)
        assertEquals(9.2740100783e-24, (1 of bohrMagnetons) into ampereSquareMeters, 1e-34)
        assertEquals(5.0507837461e-27, (1 of nuclearMagnetons) into ampereSquareMeters, 1e-37)
    }

    /** The joule per tesla is a second spelling of the ampere square meter. */
    @Test
    fun `joule per tesla is the base unit spelling`() {
        assertEquals(1 of ampereSquareMeters, 1 of joulesPerTesla)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("A*m^2", KMagneticMomentUnit.AMPERE_SQUARE_METER.symbol)
        assertEquals(1.0, KMagneticMomentUnit.AMPERE_SQUARE_METER.baseValue, 1e-12)
        assertEquals("J/T", KMagneticMomentUnit.JOULE_PER_TESLA.symbol)
        assertEquals(1.0, KMagneticMomentUnit.JOULE_PER_TESLA.baseValue, 1e-12)
        assertEquals("μB", KMagneticMomentUnit.BOHR_MAGNETON.symbol)
        assertEquals(9.2740100783e-24, KMagneticMomentUnit.BOHR_MAGNETON.baseValue, 1e-34)
        assertEquals("μN", KMagneticMomentUnit.NUCLEAR_MAGNETON.symbol)
        assertEquals(5.0507837461e-27, KMagneticMomentUnit.NUCLEAR_MAGNETON.baseValue, 1e-37)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMagneticMomentUnit.AMPERE_SQUARE_METER, KMagneticMomentUnit.BASE)
        assertEquals(1.0, KMagneticMomentUnit.BASE.baseValue, 1e-12)
        assertEquals("A*m^2", KMagneticMomentUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(4, KMagneticMomentUnit.entries.size)
        assertEquals(
            listOf("A*m^2", "J/T", "μB", "μN"),
            KMagneticMomentUnit.entries.map { it.symbol },
        )
    }
}
