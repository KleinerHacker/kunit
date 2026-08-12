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

package org.pcsoft.framework.kunit.mechanic.solidangle

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/** The named solid-angle tokens carry the correct steradian factor. */
class KSolidAngleUnitTest {

    @Test
    fun `named tokens in steradians`() {
        assertEquals(1.0, (1 of steradians) into steradians, 1e-12)
        assertEquals((PI / 180.0) * (PI / 180.0), (1 of squareDegrees) into steradians, 1e-15)
        assertEquals(4.0 * PI, (1 of spats) into steradians, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("sr", KSolidAngleUnit.STERADIAN.symbol)
        assertEquals("deg²", KSolidAngleUnit.SQUARE_DEGREE.symbol)
        assertEquals("sp", KSolidAngleUnit.SPAT.symbol)
        assertEquals(1.0, KSolidAngleUnit.STERADIAN.baseValue, 1e-12)
        assertEquals(3.0461741978e-4, KSolidAngleUnit.SQUARE_DEGREE.baseValue, 1e-12)
        assertEquals(4.0 * PI, KSolidAngleUnit.SPAT.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSolidAngleUnit.STERADIAN, KSolidAngleUnit.BASE)
        assertEquals(1.0, KSolidAngleUnit.BASE.baseValue, 1e-12)
        assertEquals("sr", KSolidAngleUnit.BASE.symbol)
    }

    @Test
    fun `full sphere in square degrees`() {
        assertEquals(41252.96, (1 of spats) into squareDegrees, 1e-2)
    }
}
