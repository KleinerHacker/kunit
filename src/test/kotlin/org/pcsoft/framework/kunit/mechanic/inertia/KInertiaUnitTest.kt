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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named moment-of-inertia tokens carry the correct kg·m² factor. */
class KInertiaUnitTest {

    @Test
    fun `named tokens in kilogram meters squared`() {
        assertEquals(1.0, (1 of kilogramMetersSquared) into kilogramMetersSquared, 1e-9)
        assertEquals(1.0e-7, (1 of gramCentimetersSquared) into kilogramMetersSquared, 1e-17)
        assertEquals(0.04214011009, (1 of poundFeetSquared) into kilogramMetersSquared, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("kg*m^2", KInertiaUnit.KILOGRAM_METERS_SQUARED.symbol)
        assertEquals("g*cm^2", KInertiaUnit.GRAM_CENTIMETERS_SQUARED.symbol)
        assertEquals("lb*ft^2", KInertiaUnit.POUND_FEET_SQUARED.symbol)
        assertEquals(1.0, KInertiaUnit.KILOGRAM_METERS_SQUARED.baseValue, 1e-12)
        assertEquals(1.0e-7, KInertiaUnit.GRAM_CENTIMETERS_SQUARED.baseValue, 1e-20)
        assertEquals(0.04214011009, KInertiaUnit.POUND_FEET_SQUARED.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KInertiaUnit.KILOGRAM_METERS_SQUARED, KInertiaUnit.BASE)
        assertEquals(1.0, KInertiaUnit.BASE.baseValue, 1e-12)
        assertEquals("kg*m^2", KInertiaUnit.BASE.symbol)
    }
}
