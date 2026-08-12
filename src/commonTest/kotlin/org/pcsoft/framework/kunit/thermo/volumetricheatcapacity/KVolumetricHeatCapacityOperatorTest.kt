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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group volumetric heat capacity operators and the equivalence of all decompositions. */
class KVolumetricHeatCapacityOperatorTest {

    /** Water: 1000 kg/m³. */
    private val water: KDensityUnitInstance = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

    /** `heatCapacity / volume = volumetric heat capacity`. */
    @Test
    fun `heat capacity over volume is volumetric heat capacity`() {
        val cv = (4184 of joulesPerKelvin) / (1 of liters)
        assertIs<KVolumetricHeatCapacityUnitInstance>(cv)
        assertEquals(4.184, cv into mega.joulesPerCubicMeterKelvin, 1e-9)
    }

    /** `specificHeatCapacity * density = volumetric heat capacity` - the second decomposition. */
    @Test
    fun `specific heat capacity times density is volumetric heat capacity`() {
        val cv1 = (4184 of joulesPerKilogramKelvin) * water
        val cv2 = water * (4184 of joulesPerKilogramKelvin)
        assertIs<KVolumetricHeatCapacityUnitInstance>(cv1)
        assertIs<KVolumetricHeatCapacityUnitInstance>(cv2)
        assertEquals(4.184, cv1 into mega.joulesPerCubicMeterKelvin, 1e-9)
        assertEquals(4.184, cv2 into mega.joulesPerCubicMeterKelvin, 1e-9)
    }

    /** Both decompositions yield the same typed, value-equal result. */
    @Test
    fun `both decompositions agree`() {
        val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)
        val viaDensity = (4184 of joulesPerKilogramKelvin) * water
        assertEquals(viaHeatCapacity, viaDensity)
        assertEquals(
            viaHeatCapacity into joulesPerCubicMeterKelvin,
            viaDensity into joulesPerCubicMeterKelvin,
            1e-6,
        )
    }

    /** `volumetric heat capacity * volume = heatCapacity` and its commutative counterpart. */
    @Test
    fun `volumetric heat capacity times volume is heat capacity`() {
        val cv = 4.184 of mega.joulesPerCubicMeterKelvin
        val v = 1 of liters
        val c1 = cv * v
        val c2 = v * cv
        assertIs<KHeatCapacityUnitInstance>(c1)
        assertIs<KHeatCapacityUnitInstance>(c2)
        assertEquals(4184.0, c1 into joulesPerKelvin, 1e-6)
        assertEquals(4184.0, c2 into joulesPerKelvin, 1e-6)
    }

    /** `heatCapacity / volumetric heat capacity = volume`. */
    @Test
    fun `heat capacity over volumetric heat capacity is volume`() {
        val v = (4184 of joulesPerKelvin) / (4.184 of mega.joulesPerCubicMeterKelvin)
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(1.0, v into liters, 1e-9)
    }

    /** `volumetric heat capacity / density = specificHeatCapacity` - the inverse decomposition. */
    @Test
    fun `volumetric heat capacity over density is specific heat capacity`() {
        val c = (4.184 of mega.joulesPerCubicMeterKelvin) / water
        assertIs<KSpecificHeatCapacityUnitInstance>(c)
        assertEquals(4184.0, c into joulesPerKilogramKelvin, 1e-6)
    }

    /** `volumetric heat capacity / specificHeatCapacity = density`. */
    @Test
    fun `volumetric heat capacity over specific heat capacity is density`() {
        val d = (4.184 of mega.joulesPerCubicMeterKelvin) / (4184 of joulesPerKilogramKelvin)
        assertIs<KDensityUnitInstance>(d)
        assertEquals(1000.0, d into ((1 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))), 1e-9)
    }
}
