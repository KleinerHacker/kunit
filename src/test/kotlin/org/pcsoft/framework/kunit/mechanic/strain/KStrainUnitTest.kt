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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named strain tokens carry the correct ratio factor. */
class KStrainUnitTest {

    @Test
    fun `named tokens as plain ratios`() {
        assertEquals(1.0, (1 of ratio) into ratio, 1e-12)
        assertEquals(0.01, (1 of percent) into ratio, 1e-15)
        assertEquals(0.001, (1 of perMille) into ratio, 1e-15)
        assertEquals(1.0e-6, (1 of microstrain) into ratio, 1e-18)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("1", KStrainUnit.RATIO.symbol)
        assertEquals("%", KStrainUnit.PERCENT.symbol)
        assertEquals("‰", KStrainUnit.PER_MILLE.symbol)
        assertEquals("µe", KStrainUnit.MICROSTRAIN.symbol)
        assertEquals(1.0, KStrainUnit.RATIO.baseValue, 1e-12)
        assertEquals(0.01, KStrainUnit.PERCENT.baseValue, 1e-15)
        assertEquals(1.0e-3, KStrainUnit.PER_MILLE.baseValue, 1e-15)
        assertEquals(1.0e-6, KStrainUnit.MICROSTRAIN.baseValue, 1e-18)
    }

    @Test
    fun `conventional conversions`() {
        assertEquals(10.0, (1 of percent) into perMille, 1e-9)
        assertEquals(10000.0, (1 of percent) into microstrain, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KStrainUnit.RATIO, KStrainUnit.BASE)
        assertEquals(1.0, KStrainUnit.BASE.baseValue, 1e-12)
        assertEquals("1", KStrainUnit.BASE.symbol)
    }
}
