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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.KMolarEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Both typed decompositions of a molar heat capacity, their inverses, and their equivalence. */
class KMolarHeatCapacityOperatorTest {

    /** Decomposition 1: `heatCapacity / amountOfSubstance = molar heat capacity`. */
    @Test
    fun `heat capacity over amount of substance is molar heat capacity`() {
        val cm = (58.2 of joulesPerKelvin) / (2 of moles)
        assertIs<KMolarHeatCapacityUnitInstance>(cm)
        assertEquals(29.1, cm into joulesPerMoleKelvin, 1e-9)
    }

    /** Decomposition 2: `molarEnergy / temperatureDifference = molar heat capacity`. */
    @Test
    fun `molar energy over temperature difference is molar heat capacity`() {
        val cm = (58.2 of joulesPerMole) / KTemperatureDifference.ofKelvin(2)
        assertIs<KMolarHeatCapacityUnitInstance>(cm)
        assertEquals(29.1, cm into joulesPerMoleKelvin, 1e-9)
    }

    /** `molar heat capacity * amountOfSubstance = heatCapacity` and its commutative counterpart. */
    @Test
    fun `molar heat capacity times amount of substance is heat capacity`() {
        val cm = 29.1 of joulesPerMoleKelvin
        val n = 2 of moles
        val c1 = cm * n
        val c2 = n * cm
        assertIs<KHeatCapacityUnitInstance>(c1)
        assertIs<KHeatCapacityUnitInstance>(c2)
        assertEquals(58.2, c1 into joulesPerKelvin, 1e-9)
        assertEquals(58.2, c2 into joulesPerKelvin, 1e-9)
    }

    /** `heatCapacity / molar heat capacity = amountOfSubstance`. */
    @Test
    fun `heat capacity over molar heat capacity is amount of substance`() {
        val n = (58.2 of joulesPerKelvin) / (29.1 of joulesPerMoleKelvin)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(2.0, n into moles, 1e-9)
    }

    /** `molar heat capacity * temperatureDifference = molarEnergy` and its commutative counterpart. */
    @Test
    fun `molar heat capacity times temperature difference is molar energy`() {
        val cm = 29.1 of joulesPerMoleKelvin
        val d = KTemperatureDifference.ofKelvin(2)
        val e1 = cm * d
        val e2 = d * cm
        assertIs<KMolarEnergyUnitInstance>(e1)
        assertIs<KMolarEnergyUnitInstance>(e2)
        assertEquals(58.2, e1 into joulesPerMole, 1e-9)
        assertEquals(58.2, e2 into joulesPerMole, 1e-9)
    }

    /** `molarEnergy / molar heat capacity = temperatureDifference`. */
    @Test
    fun `molar energy over molar heat capacity is temperature difference`() {
        val d = (58.2 of joulesPerMole) / (29.1 of joulesPerMoleKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(2.0, d into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** All three decompositions yield the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
        val viaMolarEnergy = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
        val native = (
            (1000 of grams).toUnit() *
                ((1 of meters).toUnit() pow 2) /
                ((1 of seconds).toUnit() pow 2) /
                (1 of moles).toUnit() /
                KTemperatureDifference.ofKelvin(1).toUnit()
            ).toMolarHeatCapacity()

        assertIs<KMolarHeatCapacityUnitInstance>(viaHeatCapacity)
        assertIs<KMolarHeatCapacityUnitInstance>(viaMolarEnergy)
        assertIs<KMolarHeatCapacityUnitInstance>(native)
        assertEquals(viaHeatCapacity, viaMolarEnergy)
        assertEquals(viaHeatCapacity, native)
        assertEquals(1.0, native into joulesPerMoleKelvin, 1e-9)
    }
}
