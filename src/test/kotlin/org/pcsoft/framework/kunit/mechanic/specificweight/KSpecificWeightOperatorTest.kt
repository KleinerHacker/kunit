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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group specific weight operators and the equivalence of both decompositions. */
class KSpecificWeightOperatorTest {

    private val cubicMeter: KVolumeUnitInstance = (1 of meters) * (1 of meters) * (1 of meters)

    /** Water: 1000 kg/m³. */
    private val water: KDensityUnitInstance = (1000 of kilo.grams) / cubicMeter

    /** `force / volume = specific weight`. */
    @Test
    fun `force over volume is specific weight`() {
        val gamma = (9806.65 of newtons) / cubicMeter
        assertIs<KSpecificWeightUnitInstance>(gamma)
        assertEquals(9806.65, gamma into newtonsPerCubicMeter, 1e-6)
    }

    /** `density * acceleration = specific weight` - the second decomposition (`γ = ρ · g`). */
    @Test
    fun `density times acceleration is specific weight`() {
        val g: KAccelerationUnitInstance = 1 of standardGravities
        val gamma1 = water * g
        val gamma2 = g * water
        assertIs<KSpecificWeightUnitInstance>(gamma1)
        assertIs<KSpecificWeightUnitInstance>(gamma2)
        assertEquals(9806.65, gamma1 into newtonsPerCubicMeter, 1e-6)
        assertEquals(9806.65, gamma2 into newtonsPerCubicMeter, 1e-6)
    }

    /** Both decompositions yield the same typed, value-equal result. */
    @Test
    fun `both decompositions agree`() {
        val viaForce = (9806.65 of newtons) / cubicMeter
        val viaDensity = water * (1 of standardGravities)
        assertEquals(viaForce, viaDensity)
        assertEquals(
            viaForce into newtonsPerCubicMeter,
            viaDensity into newtonsPerCubicMeter,
            1e-6,
        )
    }

    /** `specific weight * volume = force` and its commutative counterpart. */
    @Test
    fun `specific weight times volume is force`() {
        val gamma = 9806.65 of newtonsPerCubicMeter
        val f1 = gamma * cubicMeter
        val f2 = cubicMeter * gamma
        assertIs<KForceUnitInstance>(f1)
        assertIs<KForceUnitInstance>(f2)
        assertEquals(9806.65, f1 into newtons, 1e-6)
        assertEquals(9806.65, f2 into newtons, 1e-6)
    }

    /** `force / specific weight = volume`. */
    @Test
    fun `force over specific weight is volume`() {
        val v = (9806.65 of newtons) / (9806.65 of newtonsPerCubicMeter)
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(1.0, v into cubicMeter, 1e-9)
    }

    /** `specific weight / acceleration = density` and `/ density = acceleration`. */
    @Test
    fun `inverse decompositions`() {
        val gamma = 9806.65 of newtonsPerCubicMeter
        val d = gamma / (1 of standardGravities)
        assertIs<KDensityUnitInstance>(d)
        assertEquals(1000.0, d into ((1 of kilo.grams) / cubicMeter), 1e-9)

        val a = gamma / water
        assertIs<KAccelerationUnitInstance>(a)
        assertEquals(1.0, a into standardGravities, 1e-9)
    }
}
