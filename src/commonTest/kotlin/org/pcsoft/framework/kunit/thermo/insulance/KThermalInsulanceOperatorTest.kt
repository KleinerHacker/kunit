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

package org.pcsoft.framework.kunit.thermo.insulance

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
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.KHeatTransferCoefficientUnitInstance
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Both typed decompositions of a thermal insulance, their inverses, and the reciprocal U-value relation. */
class KThermalInsulanceOperatorTest {

    /** Decomposition 1: `temperatureDifference / heatFluxDensity = R-value`. */
    @Test
    fun `temperature difference over heat flux density is r value`() {
        val r = KTemperatureDifference.ofKelvin(20) / (4 of wattsPerSquareMeter)
        assertIs<KThermalInsulanceUnitInstance>(r)
        assertEquals(5.0, r into squareMeterKelvinPerWatt, 1e-9)
    }

    /** Decomposition 2: `length / thermalConductivity = R-value`. */
    @Test
    fun `thickness over thermal conductivity is r value`() {
        val r = (0.2 of meters) / (0.04 of wattsPerMeterKelvin)
        assertIs<KThermalInsulanceUnitInstance>(r)
        assertEquals(5.0, r into squareMeterKelvinPerWatt, 1e-9)
    }

    /** `R-value * heatFluxDensity = temperatureDifference` and its commutative counterpart. */
    @Test
    fun `r value times heat flux density is temperature difference`() {
        val r = 5 of squareMeterKelvinPerWatt
        val q = 4 of wattsPerSquareMeter
        val d1 = r * q
        val d2 = q * r
        assertIs<KTemperatureDifferenceUnitInstance>(d1)
        assertIs<KTemperatureDifferenceUnitInstance>(d2)
        assertEquals(20.0, d1 into KTemperatureDifference.ofKelvin(1), 1e-9)
        assertEquals(20.0, d2 into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** `temperatureDifference / R-value = heatFluxDensity`. */
    @Test
    fun `temperature difference over r value is heat flux density`() {
        val q = KTemperatureDifference.ofKelvin(20) / (5 of squareMeterKelvinPerWatt)
        assertIs<KHeatFluxDensityUnitInstance>(q)
        assertEquals(4.0, q into wattsPerSquareMeter, 1e-9)
    }

    /** `R-value * thermalConductivity = length` and its commutative counterpart. */
    @Test
    fun `r value times thermal conductivity is thickness`() {
        val r = 5 of squareMeterKelvinPerWatt
        val k = 0.04 of wattsPerMeterKelvin
        val t1 = r * k
        val t2 = k * r
        assertIs<KLengthUnitInstance>(t1)
        assertIs<KLengthUnitInstance>(t2)
        assertEquals(0.2, t1 into meters, 1e-12)
        assertEquals(0.2, t2 into meters, 1e-12)
    }

    /** `length / R-value = thermalConductivity`. */
    @Test
    fun `thickness over r value is thermal conductivity`() {
        val k = (0.2 of meters) / (5 of squareMeterKelvinPerWatt)
        assertIs<KThermalConductivityUnitInstance>(k)
        assertEquals(0.04, k into wattsPerMeterKelvin, 1e-12)
    }

    /** The R-value and the U-value are exact reciprocals, in both directions and with typed results. */
    @Test
    fun `r value and u value are reciprocal`() {
        val u = 1.3 of wattsPerSquareMeterKelvin
        val r = 1 / u
        assertIs<KThermalInsulanceUnitInstance>(r)
        assertEquals(1.0 / 1.3, r into squareMeterKelvinPerWatt, 1e-12)

        val backToU = 1 / r
        assertIs<KHeatTransferCoefficientUnitInstance>(backToU)
        assertEquals(1.3, backToU into wattsPerSquareMeterKelvin, 1e-12)
    }

    /** All three decompositions yield the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaFlux = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
        val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
        val native = (
                ((1 of seconds).toUnit() pow 3) *
                        KTemperatureDifference.ofKelvin(1).toUnit() /
                        (1000 of grams).toUnit()
                ).toThermalInsulance()

        assertIs<KThermalInsulanceUnitInstance>(viaFlux)
        assertIs<KThermalInsulanceUnitInstance>(viaThickness)
        assertIs<KThermalInsulanceUnitInstance>(native)
        assertEquals(viaFlux, viaThickness)
        assertEquals(viaFlux, native)
        assertEquals(1.0, native into squareMeterKelvinPerWatt, 1e-9)
    }
}
