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

package org.pcsoft.framework.kunit.mechanic.angle

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/** The named angle tokens carry the correct radian factor. */
class KAngleUnitTest {

    @Test
    fun `named tokens in radians`() {
        assertEquals(1.0, (1 of radians) into radians, 1e-12)
        assertEquals(PI / 180.0, (1 of degrees) into radians, 1e-12)
        assertEquals(PI / 10800.0, (1 of arcminutes) into radians, 1e-15)
        assertEquals(PI / 648000.0, (1 of arcseconds) into radians, 1e-17)
        assertEquals(PI / 200.0, (1 of gradians) into radians, 1e-12)
        assertEquals(2.0 * PI, (1 of turns) into radians, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("rad", KAngleUnit.RADIAN.symbol)
        assertEquals("°", KAngleUnit.DEGREE.symbol)
        assertEquals("'", KAngleUnit.ARCMINUTE.symbol)
        assertEquals("\"", KAngleUnit.ARCSECOND.symbol)
        assertEquals("gon", KAngleUnit.GRADIAN.symbol)
        assertEquals("tr", KAngleUnit.TURN.symbol)
        assertEquals(1.0, KAngleUnit.RADIAN.baseValue, 1e-12)
        assertEquals(PI / 180.0, KAngleUnit.DEGREE.baseValue, 1e-12)
        assertEquals(PI / 10800.0, KAngleUnit.ARCMINUTE.baseValue, 1e-15)
        assertEquals(PI / 648000.0, KAngleUnit.ARCSECOND.baseValue, 1e-17)
        assertEquals(PI / 200.0, KAngleUnit.GRADIAN.baseValue, 1e-12)
        assertEquals(2.0 * PI, KAngleUnit.TURN.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KAngleUnit.RADIAN, KAngleUnit.BASE)
        assertEquals(1.0, KAngleUnit.BASE.baseValue, 1e-12)
        assertEquals("rad", KAngleUnit.BASE.symbol)
    }

    @Test
    fun `conventional conversions`() {
        assertEquals(360.0, (1 of turns) into degrees, 1e-9)
        assertEquals(400.0, (1 of turns) into gradians, 1e-9)
        assertEquals(60.0, (1 of degrees) into arcminutes, 1e-9)
        assertEquals(3600.0, (1 of degrees) into arcseconds, 1e-9)
    }
}
