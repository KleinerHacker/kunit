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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed thermal expansion operators and the equivalence of all decompositions. */
class KThermalExpansionOperatorTest {

    /** `1 / temperatureDifference = thermal expansion coefficient`, with a typed result. */
    @Test
    fun `reciprocal of temperature difference is thermal expansion`() {
        val alpha = 1 / KTemperatureDifference.ofKelvin(2)
        assertIs<KThermalExpansionUnitInstance>(alpha)
        assertEquals(0.5, alpha into perKelvin, 1e-12)
    }

    /** `1 / thermal expansion coefficient = temperatureDifference`, with a typed result. */
    @Test
    fun `reciprocal of thermal expansion is temperature difference`() {
        val d = 1 / (0.5 of perKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(2.0, d into KTemperatureDifference.ofKelvin(1), 1e-12)
    }

    /** `α · ΔT` is the dimensionless relative change, in both operand orders. */
    @Test
    fun `expansion times temperature difference is relative change`() {
        val steel = 12 of ppmPerKelvin
        val rise = KTemperatureDifference.ofKelvin(50)
        assertEquals(6.0e-4, steel * rise, 1e-15)
        assertEquals(6.0e-4, rise * steel, 1e-15)
    }

    /** `elongationOf` turns the relative change into an absolute, typed length change. */
    @Test
    fun `steel beam elongation`() {
        val steel = 12 of ppmPerKelvin
        val beam = 10 of meters
        val rise = KTemperatureDifference.ofKelvin(50)

        val growth = steel.elongationOf(beam, rise)
        assertIs<KLengthUnitInstance>(growth)
        assertEquals(6.0, growth into milli.meters, 1e-9) // 6 mm over 10 m
        // consistent with the dimensionless product
        assertEquals(beam.value * (steel * rise), growth.value, 1e-15)
    }

    /**
     * Both decompositions - the typed reciprocal operator and the native `temperature⁻¹` expression
     * through `toThermalExpansion()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = 1 / KTemperatureDifference.ofKelvin(1)
        val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()
        assertIs<KThermalExpansionUnitInstance>(typed)
        assertIs<KThermalExpansionUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into perKelvin, 1e-12)
    }
}
