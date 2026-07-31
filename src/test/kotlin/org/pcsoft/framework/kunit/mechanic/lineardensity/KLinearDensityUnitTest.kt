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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named linear-density tokens carry the correct kg/m factor. */
class KLinearDensityUnitTest {

    @Test
    fun `named tokens in kilograms per meter`() {
        assertEquals(1.0, (1 of kilogramsPerMeter) into kilogramsPerMeter, 1e-9)
        assertEquals(0.001, (1 of gramsPerMeter) into kilogramsPerMeter, 1e-12)
        assertEquals(0.1, (1 of gramsPerCentimeter) into kilogramsPerMeter, 1e-12)
        assertEquals(1.0e-6, (1 of tex) into kilogramsPerMeter, 1e-15)
        assertEquals(1.0e-3 / 9000.0, (1 of denier) into kilogramsPerMeter, 1e-15)
        assertEquals(0.45359237 / 0.3048, (1 of poundsPerFoot) into kilogramsPerMeter, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("kg/m", KLinearDensityUnit.KILOGRAMS_PER_METER.symbol)
        assertEquals("g/m", KLinearDensityUnit.GRAMS_PER_METER.symbol)
        assertEquals("g/cm", KLinearDensityUnit.GRAMS_PER_CENTIMETER.symbol)
        assertEquals("tex", KLinearDensityUnit.TEX.symbol)
        assertEquals("den", KLinearDensityUnit.DENIER.symbol)
        assertEquals("lb/ft", KLinearDensityUnit.POUNDS_PER_FOOT.symbol)
        assertEquals(1.0, KLinearDensityUnit.KILOGRAMS_PER_METER.baseValue, 1e-12)
        assertEquals(1.0e-3, KLinearDensityUnit.GRAMS_PER_METER.baseValue, 1e-15)
        assertEquals(0.1, KLinearDensityUnit.GRAMS_PER_CENTIMETER.baseValue, 1e-12)
        assertEquals(1.0e-6, KLinearDensityUnit.TEX.baseValue, 1e-18)
        assertEquals(1.0e-3 / 9000.0, KLinearDensityUnit.DENIER.baseValue, 1e-18)
        assertEquals(0.45359237 / 0.3048, KLinearDensityUnit.POUNDS_PER_FOOT.baseValue, 1e-12)
    }

    @Test
    fun `textile relation between tex and denier`() {
        assertEquals(9.0, (1 of tex) into denier, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLinearDensityUnit.KILOGRAMS_PER_METER, KLinearDensityUnit.BASE)
        assertEquals(1.0, KLinearDensityUnit.BASE.baseValue, 1e-12)
        assertEquals("kg/m", KLinearDensityUnit.BASE.symbol)
    }
}
