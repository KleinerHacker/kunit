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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named angular-momentum tokens carry the correct kg·m²/s factor. */
class KAngularMomentumUnitTest {

    @Test
    fun `named tokens in kilogram meters squared per second`() {
        assertEquals(1.0, (1 of kilogramMetersSquaredPerSecond) into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(1.0, (1 of newtonMeterSeconds) into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(1.0, (1 of jouleSeconds) into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(
            1.0e-7,
            (1 of gramCentimetersSquaredPerSecond) into kilogramMetersSquaredPerSecond,
            1e-17,
        )
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("kg*m^2/s", KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND.symbol)
        assertEquals("N*m*s", KAngularMomentumUnit.NEWTON_METER_SECOND.symbol)
        assertEquals("J*s", KAngularMomentumUnit.JOULE_SECOND.symbol)
        assertEquals("g*cm^2/s", KAngularMomentumUnit.GRAM_CENTIMETERS_SQUARED_PER_SECOND.symbol)
        assertEquals(1.0, KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND.baseValue, 1e-12)
        assertEquals(1.0, KAngularMomentumUnit.NEWTON_METER_SECOND.baseValue, 1e-12)
        assertEquals(1.0, KAngularMomentumUnit.JOULE_SECOND.baseValue, 1e-12)
        assertEquals(1.0e-7, KAngularMomentumUnit.GRAM_CENTIMETERS_SQUARED_PER_SECOND.baseValue, 1e-20)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(
            KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND,
            KAngularMomentumUnit.BASE,
        )
        assertEquals(1.0, KAngularMomentumUnit.BASE.baseValue, 1e-12)
        assertEquals("kg*m^2/s", KAngularMomentumUnit.BASE.symbol)
    }
}
