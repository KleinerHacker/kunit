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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Typed force-per-length operators: both decompositions (`force / length` as stiffness,
 * `energy / area` as surface tension) and the inverses.
 */
class KLineForceOperatorTest {

    private val squareMeter: KAreaUnitInstance = (1 of meters) * (1 of meters)

    /** Decomposition A (stiffness): `force / length = lineforce`. */
    @Test
    fun `force over length is stiffness`() {
        val k = (200 of newtons) / (0.05 of meters)
        assertIs<KLineForceUnitInstance>(k)
        assertEquals(4000.0, k into newtonsPerMeter, 1e-6)
    }

    /** Decomposition B (surface tension): `energy / area = lineforce`. */
    @Test
    fun `energy over area is surface tension`() {
        val sigma = (72 of milli.joules) / squareMeter
        assertIs<KLineForceUnitInstance>(sigma)
        assertEquals(72.0, sigma into milli.newtonsPerMeter, 1e-9)
        assertEquals(72.0, sigma into dynesPerCentimeter, 1e-9)
    }

    /** Both decompositions produce the same typed, value-equal result. */
    @Test
    fun `both decompositions agree`() {
        val viaForce = (2 of newtons) / (1 of meters)
        val viaEnergy = (2 of joules) / squareMeter
        assertEquals(viaForce, viaEnergy)
    }

    /** `lineforce * length = force`, the commutative form, and `force / lineforce = length`. */
    @Test
    fun `force and length inverses`() {
        val k = 4000 of newtonsPerMeter
        val f1: KForceUnitInstance = k * (0.05 of meters)
        val f2: KForceUnitInstance = (0.05 of meters) * k
        val l: KLengthUnitInstance = (200 of newtons) / k
        assertIs<KForceUnitInstance>(f1)
        assertIs<KForceUnitInstance>(f2)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(200.0, f1 into newtons, 1e-6)
        assertEquals(200.0, f2 into newtons, 1e-6)
        assertEquals(0.05, l into meters, 1e-9)
    }

    /** `lineforce * area = energy`, the commutative form, and `energy / lineforce = area`. */
    @Test
    fun `energy and area inverses`() {
        val sigma = 2 of newtonsPerMeter
        val e1: KEnergyUnitInstance = sigma * squareMeter
        val e2: KEnergyUnitInstance = squareMeter * sigma
        val a: KAreaUnitInstance = (2 of joules) / sigma
        assertIs<KEnergyUnitInstance>(e1)
        assertIs<KEnergyUnitInstance>(e2)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, e1 into joules, 1e-9)
        assertEquals(2.0, e2 into joules, 1e-9)
        assertEquals(1.0, a into squareMeter, 1e-9)
    }
}
