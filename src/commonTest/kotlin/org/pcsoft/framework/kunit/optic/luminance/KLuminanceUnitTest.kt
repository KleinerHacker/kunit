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

package org.pcsoft.framework.kunit.optic.luminance

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/** Every named luminance token carries the correct cd/m² factor. */
class KLuminanceUnitTest {

    @Test
    fun `named tokens in candelas per square meter`() {
        assertEquals(1.0, (1 of candelasPerSquareMeter) into candelasPerSquareMeter, 1e-12)
        assertEquals(1.0, (1 of nits) into candelasPerSquareMeter, 1e-12)
        assertEquals(1.0e4, (1 of stilbs) into candelasPerSquareMeter, 1e-6)
        assertEquals(1.0 / PI, (1 of apostilbs) into candelasPerSquareMeter, 1e-12)
        assertEquals(1.0e4 / PI, (1 of lamberts) into candelasPerSquareMeter, 1e-9)
        assertEquals(3.4262590996353905, (1 of footLamberts) into candelasPerSquareMeter, 1e-12)
    }

    /** `nits` is a second spelling of the base unit, not a unit of its own. */
    @Test
    fun `nits is the base unit spelling`() {
        assertEquals(1 of candelasPerSquareMeter, 1 of nits)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("cd/m^2", KLuminanceUnit.CANDELA_PER_SQUARE_METER.symbol)
        assertEquals(1.0, KLuminanceUnit.CANDELA_PER_SQUARE_METER.baseValue, 1e-12)
        assertEquals("sb", KLuminanceUnit.STILB.symbol)
        assertEquals(1.0e4, KLuminanceUnit.STILB.baseValue, 1e-6)
        assertEquals("asb", KLuminanceUnit.APOSTILB.symbol)
        assertEquals(1.0 / PI, KLuminanceUnit.APOSTILB.baseValue, 1e-12)
        assertEquals("L", KLuminanceUnit.LAMBERT.symbol)
        assertEquals(1.0e4 / PI, KLuminanceUnit.LAMBERT.baseValue, 1e-9)
        assertEquals("fL", KLuminanceUnit.FOOT_LAMBERT.symbol)
        assertEquals(3.4262590996353905, KLuminanceUnit.FOOT_LAMBERT.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminanceUnit.CANDELA_PER_SQUARE_METER, KLuminanceUnit.BASE)
        assertEquals(1.0, KLuminanceUnit.BASE.baseValue, 1e-12)
        assertEquals("cd/m^2", KLuminanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(5, KLuminanceUnit.entries.size)
        assertEquals(
            listOf("cd/m^2", "sb", "asb", "L", "fL"),
            KLuminanceUnit.entries.map { it.symbol },
        )
    }
}
