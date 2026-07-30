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

package org.pcsoft.framework.kunit.thermo.heatcapacity

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group heat capacity operators and the equivalence of all decompositions. */
class KHeatCapacityOperatorTest {

    /** `energy / temperatureDifference = heat capacity`. */
    @Test
    fun `energy over temperature difference is heat capacity`() {
        val c = (4184 of joules) / KTemperatureDifference.ofKelvin(2)
        assertIs<KHeatCapacityUnitInstance>(c)
        assertEquals(2092.0, c into joulesPerKelvin, 1e-9)
    }

    /** `heat capacity * temperatureDifference = energy` and its commutative counterpart. */
    @Test
    fun `heat capacity times temperature difference is energy`() {
        val c = 2092 of joulesPerKelvin
        val d = KTemperatureDifference.ofKelvin(2)
        val e1 = c * d
        val e2 = d * c
        assertIs<KEnergyUnitInstance>(e1)
        assertIs<KEnergyUnitInstance>(e2)
        assertEquals(4184.0, e1 into joules, 1e-9)
        assertEquals(4184.0, e2 into joules, 1e-9)
    }

    /** `energy / heat capacity = temperatureDifference`. */
    @Test
    fun `energy over heat capacity is temperature difference`() {
        val d = (4184 of joules) / (2092 of joulesPerKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(2.0, d into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /**
     * Both decompositions - the typed `energy / temperatureDifference` operator and the native
     * `mass·distance²·time⁻²·temperature⁻¹` expression through `toHeatCapacity()` - yield the same typed,
     * value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)
        val native = (
            (1000 of grams).toUnit() *
                ((1 of meters).toUnit() pow 2) /
                ((1 of seconds).toUnit() pow 2) /
                KTemperatureDifference.ofKelvin(1).toUnit()
            ).toHeatCapacity()
        assertIs<KHeatCapacityUnitInstance>(typed)
        assertIs<KHeatCapacityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into joulesPerKelvin, 1e-9)
    }
}
