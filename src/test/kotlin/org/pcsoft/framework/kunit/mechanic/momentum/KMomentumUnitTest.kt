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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named momentum tokens carry the correct kg·m/s factor. */
class KMomentumUnitTest {

    @Test
    fun `named tokens in kilogram meters per second`() {
        assertEquals(1.0, (1 of kilogramMetersPerSecond) into kilogramMetersPerSecond, 1e-9)
        assertEquals(1.0, (1 of newtonSeconds) into kilogramMetersPerSecond, 1e-9)
        assertEquals(1.0e-5, (1 of gramCentimetersPerSecond) into kilogramMetersPerSecond, 1e-15)
        assertEquals(0.1382549544, (1 of poundFeetPerSecond) into kilogramMetersPerSecond, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("kg*m/s", KMomentumUnit.KILOGRAM_METERS_PER_SECOND.symbol)
        assertEquals("N*s", KMomentumUnit.NEWTON_SECOND.symbol)
        assertEquals("g*cm/s", KMomentumUnit.GRAM_CENTIMETERS_PER_SECOND.symbol)
        assertEquals("lb*ft/s", KMomentumUnit.POUND_FEET_PER_SECOND.symbol)
        assertEquals(1.0, KMomentumUnit.KILOGRAM_METERS_PER_SECOND.baseValue, 1e-12)
        assertEquals(1.0, KMomentumUnit.NEWTON_SECOND.baseValue, 1e-12)
        assertEquals(1.0e-5, KMomentumUnit.GRAM_CENTIMETERS_PER_SECOND.baseValue, 1e-18)
        assertEquals(0.1382549544, KMomentumUnit.POUND_FEET_PER_SECOND.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMomentumUnit.KILOGRAM_METERS_PER_SECOND, KMomentumUnit.BASE)
        assertEquals(1.0, KMomentumUnit.BASE.baseValue, 1e-12)
        assertEquals("kg*m/s", KMomentumUnit.BASE.symbol)
    }
}
