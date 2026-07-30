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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.KSpecificEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Both typed decompositions of a specific heat capacity, their inverses, and their equivalence. */
class KSpecificHeatCapacityOperatorTest {

    /** Decomposition 1: `heatCapacity / mass = specific heat capacity`. */
    @Test
    fun `heat capacity over mass is specific heat capacity`() {
        val c = (4184 of joulesPerKelvin) / (1 of kilo.grams)
        assertIs<KSpecificHeatCapacityUnitInstance>(c)
        assertEquals(4184.0, c into joulesPerKilogramKelvin, 1e-9)
    }

    /** Decomposition 2: `specificEnergy / temperatureDifference = specific heat capacity`. */
    @Test
    fun `specific energy over temperature difference is specific heat capacity`() {
        val c = (8368 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(2)
        assertIs<KSpecificHeatCapacityUnitInstance>(c)
        assertEquals(4184.0, c into joulesPerKilogramKelvin, 1e-9)
    }

    /** `specific heat capacity * mass = heatCapacity` and its commutative counterpart. */
    @Test
    fun `specific heat capacity times mass is heat capacity`() {
        val c = 4184 of joulesPerKilogramKelvin
        val m = 2 of kilo.grams
        val hc1 = c * m
        val hc2 = m * c
        assertIs<KHeatCapacityUnitInstance>(hc1)
        assertIs<KHeatCapacityUnitInstance>(hc2)
        assertEquals(8368.0, hc1 into joulesPerKelvin, 1e-9)
        assertEquals(8368.0, hc2 into joulesPerKelvin, 1e-9)
    }

    /** `heatCapacity / specific heat capacity = mass`. */
    @Test
    fun `heat capacity over specific heat capacity is mass`() {
        val m = (8368 of joulesPerKelvin) / (4184 of joulesPerKilogramKelvin)
        assertIs<KMassUnitInstance>(m)
        assertEquals(2000.0, m into grams, 1e-6)
    }

    /** `specific heat capacity * temperatureDifference = specificEnergy` and its commutative counterpart. */
    @Test
    fun `specific heat capacity times temperature difference is specific energy`() {
        val c = 4184 of joulesPerKilogramKelvin
        val d = KTemperatureDifference.ofKelvin(2)
        val q1 = c * d
        val q2 = d * c
        assertIs<KSpecificEnergyUnitInstance>(q1)
        assertIs<KSpecificEnergyUnitInstance>(q2)
        assertEquals(8368.0, q1 into joulesPerKilogram, 1e-9)
        assertEquals(8368.0, q2 into joulesPerKilogram, 1e-9)
    }

    /** `specificEnergy / specific heat capacity = temperatureDifference`. */
    @Test
    fun `specific energy over specific heat capacity is temperature difference`() {
        val d = (8368 of joulesPerKilogram) / (4184 of joulesPerKilogramKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(2.0, d into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** All three decompositions yield the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of kilo.grams)
        val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
        val native = (
            ((1 of meters).toUnit() pow 2) /
                ((1 of seconds).toUnit() pow 2) /
                KTemperatureDifference.ofKelvin(1).toUnit()
            ).toSpecificHeatCapacity()

        assertIs<KSpecificHeatCapacityUnitInstance>(viaHeatCapacity)
        assertIs<KSpecificHeatCapacityUnitInstance>(viaSpecificEnergy)
        assertIs<KSpecificHeatCapacityUnitInstance>(native)
        assertEquals(viaHeatCapacity, viaSpecificEnergy)
        assertEquals(viaHeatCapacity, native)
        assertEquals(1.0, native into joulesPerKilogramKelvin, 1e-9)
    }
}
