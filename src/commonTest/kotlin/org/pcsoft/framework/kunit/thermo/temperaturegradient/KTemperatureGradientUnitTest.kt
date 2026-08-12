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

package org.pcsoft.framework.kunit.thermo.temperaturegradient

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named temperature gradient token carries the correct K/m factor. */
class KTemperatureGradientUnitTest {

    @Test
    fun `named tokens in kelvin per meter`() {
        assertEquals(1.0, (1 of kelvinPerMeter) into kelvinPerMeter, 1e-12)
        assertEquals(1.0e-3, (1 of kelvinPerKilometer) into kelvinPerMeter, 1e-15)
        assertEquals((5.0 / 9.0) / 0.3048, (1 of fahrenheitPerFoot) into kelvinPerMeter, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("K/m", KTemperatureGradientUnit.KELVIN_PER_METER.symbol)
        assertEquals(1.0, KTemperatureGradientUnit.KELVIN_PER_METER.baseValue, 1e-12)
        assertEquals("K/km", KTemperatureGradientUnit.KELVIN_PER_KILOMETER.symbol)
        assertEquals(1.0e-3, KTemperatureGradientUnit.KELVIN_PER_KILOMETER.baseValue, 1e-15)
        assertEquals("°F/ft", KTemperatureGradientUnit.FAHRENHEIT_PER_FOOT.symbol)
        assertEquals((5.0 / 9.0) / 0.3048, KTemperatureGradientUnit.FAHRENHEIT_PER_FOOT.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KTemperatureGradientUnit.KELVIN_PER_METER, KTemperatureGradientUnit.BASE)
        assertEquals(1.0, KTemperatureGradientUnit.BASE.baseValue, 1e-12)
        assertEquals("K/m", KTemperatureGradientUnit.BASE.symbol)
    }
}
