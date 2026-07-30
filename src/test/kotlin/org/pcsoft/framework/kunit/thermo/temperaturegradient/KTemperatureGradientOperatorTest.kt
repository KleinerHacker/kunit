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
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group temperature gradient operators and the equivalence of all decompositions. */
class KTemperatureGradientOperatorTest {

    /** `temperatureDifference / length = temperature gradient`. */
    @Test
    fun `temperature difference over length is gradient`() {
        val g = KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)
        assertIs<KTemperatureGradientUnitInstance>(g)
        assertEquals(25.0, g into kelvinPerKilometer, 1e-9)
    }

    /** `temperature gradient * length = temperatureDifference` and its commutative counterpart. */
    @Test
    fun `gradient times length is temperature difference`() {
        val g = 25 of kelvinPerKilometer
        val depth = 2 of kilo.meters
        val d1 = g * depth
        val d2 = depth * g
        assertIs<KTemperatureDifferenceUnitInstance>(d1)
        assertIs<KTemperatureDifferenceUnitInstance>(d2)
        assertEquals(50.0, d1 into KTemperatureDifference.ofKelvin(1), 1e-9)
        assertEquals(50.0, d2 into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** `temperatureDifference / temperature gradient = length`. */
    @Test
    fun `temperature difference over gradient is length`() {
        val depth = KTemperatureDifference.ofKelvin(50) / (25 of kelvinPerKilometer)
        assertIs<KLengthUnitInstance>(depth)
        assertEquals(2000.0, depth into meters, 1e-6)
    }

    /**
     * Both decompositions - the typed `temperatureDifference / length` operator and the native
     * `temperature·distance⁻¹` expression through `toTemperatureGradient()` - yield the same typed,
     * value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = KTemperatureDifference.ofKelvin(1) / (1 of meters)
        val native = (
            KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()
            ).toTemperatureGradient()
        assertIs<KTemperatureGradientUnitInstance>(typed)
        assertIs<KTemperatureGradientUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into kelvinPerMeter, 1e-9)
    }
}
