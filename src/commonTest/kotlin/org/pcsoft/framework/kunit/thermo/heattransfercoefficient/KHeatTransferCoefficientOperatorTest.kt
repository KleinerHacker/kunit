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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.KHeatFluxDensityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Both typed decompositions of a heat transfer coefficient, their inverses, and their equivalence. */
class KHeatTransferCoefficientOperatorTest {

    /** Decomposition 1: `heatFluxDensity / temperatureDifference = U-value`. */
    @Test
    fun `heat flux density over temperature difference is u value`() {
        val u = (26 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(20)
        assertIs<KHeatTransferCoefficientUnitInstance>(u)
        assertEquals(1.3, u into wattsPerSquareMeterKelvin, 1e-9)
    }

    /** Decomposition 2: `thermalConductivity / length = U-value`. */
    @Test
    fun `thermal conductivity over thickness is u value`() {
        val u = (0.04 of wattsPerMeterKelvin) / (0.2 of meters)
        assertIs<KHeatTransferCoefficientUnitInstance>(u)
        assertEquals(0.2, u into wattsPerSquareMeterKelvin, 1e-9)
    }

    /** `U-value * temperatureDifference = heatFluxDensity` and its commutative counterpart. */
    @Test
    fun `u value times temperature difference is heat flux density`() {
        val u = 1.3 of wattsPerSquareMeterKelvin
        val d = KTemperatureDifference.ofKelvin(20)
        val q1 = u * d
        val q2 = d * u
        assertIs<KHeatFluxDensityUnitInstance>(q1)
        assertIs<KHeatFluxDensityUnitInstance>(q2)
        assertEquals(26.0, q1 into wattsPerSquareMeter, 1e-9)
        assertEquals(26.0, q2 into wattsPerSquareMeter, 1e-9)
    }

    /** `heatFluxDensity / U-value = temperatureDifference`. */
    @Test
    fun `heat flux density over u value is temperature difference`() {
        val d = (26 of wattsPerSquareMeter) / (1.3 of wattsPerSquareMeterKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(20.0, d into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** `U-value * length = thermalConductivity` and its commutative counterpart. */
    @Test
    fun `u value times thickness is thermal conductivity`() {
        val u = 0.2 of wattsPerSquareMeterKelvin
        val t = 0.2 of meters
        val k1 = u * t
        val k2 = t * u
        assertIs<KThermalConductivityUnitInstance>(k1)
        assertIs<KThermalConductivityUnitInstance>(k2)
        assertEquals(0.04, k1 into wattsPerMeterKelvin, 1e-12)
        assertEquals(0.04, k2 into wattsPerMeterKelvin, 1e-12)
    }

    /** `thermalConductivity / U-value = length`. */
    @Test
    fun `thermal conductivity over u value is thickness`() {
        val t = (0.04 of wattsPerMeterKelvin) / (0.2 of wattsPerSquareMeterKelvin)
        assertIs<KLengthUnitInstance>(t)
        assertEquals(0.2, t into meters, 1e-12)
    }

    /** All three decompositions yield the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaFlux = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
        val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
        val native = (
                (1000 of grams).toUnit() /
                        ((1 of seconds).toUnit() pow 3) /
                        KTemperatureDifference.ofKelvin(1).toUnit()
                ).toHeatTransferCoefficient()

        assertIs<KHeatTransferCoefficientUnitInstance>(viaFlux)
        assertIs<KHeatTransferCoefficientUnitInstance>(viaConductivity)
        assertIs<KHeatTransferCoefficientUnitInstance>(native)
        assertEquals(viaFlux, viaConductivity)
        assertEquals(viaFlux, native)
        assertEquals(1.0, native into wattsPerSquareMeterKelvin, 1e-9)
    }
}
