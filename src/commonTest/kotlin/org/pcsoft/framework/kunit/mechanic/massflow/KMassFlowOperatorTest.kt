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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.KVolumeFlowUnitInstance
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Typed mass-flow operators: both decompositions (`mass / time`, `density * volumeflow`) and the
 * inverses.
 */
class KMassFlowOperatorTest {

    private val water: KDensityUnitInstance = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

    /** Decomposition A: `mass / time = massflow`. */
    @Test
    fun `mass over time is mass flow`() {
        val m = (2 of kilo.grams) / (1 of seconds)
        assertIs<KMassFlowUnitInstance>(m)
        assertEquals(2.0, m into kilogramsPerSecond, 1e-9)
    }

    /** Decomposition B: `density * volumeflow = massflow`, plus its commutative form. */
    @Test
    fun `density times volume flow is mass flow`() {
        val q = 2 of cubicMetersPerSecond
        val m1 = water * q
        val m2 = q * water
        assertIs<KMassFlowUnitInstance>(m1)
        assertIs<KMassFlowUnitInstance>(m2)
        assertEquals(2000.0, m1 into kilogramsPerSecond, 1e-6)
        assertEquals(2000.0, m2 into kilogramsPerSecond, 1e-6)
    }

    /** Both decompositions and the native form produce the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
        val viaDensityFlow = water * (2 of cubicMetersPerSecond)
        val viaNative = ((2_000_000 of grams).toUnit() / (1 of seconds).toUnit()).toMassFlow()
        assertEquals(viaMassTime, viaDensityFlow)
        assertEquals(viaMassTime, viaNative)
    }

    /** `massflow * time = mass`, the commutative form, and `mass / massflow = time`. */
    @Test
    fun `mass and time inverses`() {
        val m = 2 of kilogramsPerSecond
        val mass1: KMassUnitInstance = m * (3 of seconds)
        val mass2: KMassUnitInstance = (3 of seconds) * m
        val t: KTimeUnitInstance = (6 of kilo.grams) / m
        assertIs<KMassUnitInstance>(mass1)
        assertIs<KMassUnitInstance>(mass2)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(6.0, mass1 into kilo.grams, 1e-9)
        assertEquals(6.0, mass2 into kilo.grams, 1e-9)
        assertEquals(3.0, t into seconds, 1e-9)
    }

    /** `massflow / density = volumeflow` and `massflow / volumeflow = density`. */
    @Test
    fun `density and volume flow inverses`() {
        val m = 2000 of kilogramsPerSecond
        val q: KVolumeFlowUnitInstance = m / water
        val rho: KDensityUnitInstance = m / (2 of cubicMetersPerSecond)
        assertIs<KVolumeFlowUnitInstance>(q)
        assertIs<KDensityUnitInstance>(rho)
        assertEquals(2.0, q into cubicMetersPerSecond, 1e-9)
        assertEquals(water.value, rho.value, 1e-6)
    }
}
