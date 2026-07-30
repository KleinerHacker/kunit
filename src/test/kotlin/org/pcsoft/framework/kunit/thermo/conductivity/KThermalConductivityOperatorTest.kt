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

package org.pcsoft.framework.kunit.thermo.conductivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.KHeatFluxDensityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.KTemperatureGradientUnitInstance
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Fourier's law as typed operators, plus the equivalence of all decompositions. */
class KThermalConductivityOperatorTest {

    /** `heatFluxDensity / temperatureGradient = thermal conductivity`. */
    @Test
    fun `heat flux density over temperature gradient is thermal conductivity`() {
        val k = (80 of wattsPerSquareMeter) / (2 of kelvinPerMeter)
        assertIs<KThermalConductivityUnitInstance>(k)
        assertEquals(40.0, k into wattsPerMeterKelvin, 1e-9)
    }

    /** `thermal conductivity * temperatureGradient = heatFluxDensity` and its commutative counterpart. */
    @Test
    fun `thermal conductivity times temperature gradient is heat flux density`() {
        val k = 40 of wattsPerMeterKelvin
        val g = 2 of kelvinPerMeter
        val q1 = k * g
        val q2 = g * k
        assertIs<KHeatFluxDensityUnitInstance>(q1)
        assertIs<KHeatFluxDensityUnitInstance>(q2)
        assertEquals(80.0, q1 into wattsPerSquareMeter, 1e-9)
        assertEquals(80.0, q2 into wattsPerSquareMeter, 1e-9)
    }

    /** `heatFluxDensity / thermal conductivity = temperatureGradient`. */
    @Test
    fun `heat flux density over thermal conductivity is temperature gradient`() {
        val g = (80 of wattsPerSquareMeter) / (40 of wattsPerMeterKelvin)
        assertIs<KTemperatureGradientUnitInstance>(g)
        assertEquals(2.0, g into kelvinPerMeter, 1e-9)
    }

    /**
     * Both decompositions - the typed `heatFluxDensity / temperatureGradient` operator and the native
     * `mass·distance·time⁻³·temperature⁻¹` expression through `toThermalConductivity()` - yield the same
     * typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
        val native = (
            (1000 of grams).toUnit() *
                (1 of meters).toUnit() /
                ((1 of seconds).toUnit() pow 3) /
                KTemperatureDifference.ofKelvin(1).toUnit()
            ).toThermalConductivity()
        assertIs<KThermalConductivityUnitInstance>(typed)
        assertIs<KThermalConductivityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into wattsPerMeterKelvin, 1e-9)
    }
}
