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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularvelocity.KAngularVelocityUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.KInertiaUnitInstance
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.momentum.KMomentumUnitInstance
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Typed angular-momentum operators: both decompositions (`inertia * angularvelocity`,
 * `momentum * length`) and the inverses.
 */
class KAngularMomentumOperatorTest {

    private val omega = (3 of radians) / (1 of seconds) // 3 rad/s

    /** Decomposition A: `inertia * angularvelocity = angularmomentum`, plus its commutative form. */
    @Test
    fun `inertia times angular velocity is angular momentum`() {
        val l1 = (2 of kilogramMetersSquared) * omega
        val l2 = omega * (2 of kilogramMetersSquared)
        assertIs<KAngularMomentumUnitInstance>(l1)
        assertIs<KAngularMomentumUnitInstance>(l2)
        assertEquals(6.0, l1 into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(6.0, l2 into kilogramMetersSquaredPerSecond, 1e-9)
    }

    /** Decomposition B: `momentum * length = angularmomentum`, plus its commutative form. */
    @Test
    fun `momentum times lever arm is angular momentum`() {
        val l1 = (3 of kilogramMetersPerSecond) * (2 of meters)
        val l2 = (2 of meters) * (3 of kilogramMetersPerSecond)
        assertIs<KAngularMomentumUnitInstance>(l1)
        assertIs<KAngularMomentumUnitInstance>(l2)
        assertEquals(6.0, l1 into kilogramMetersSquaredPerSecond, 1e-9)
        assertEquals(6.0, l2 into kilogramMetersSquaredPerSecond, 1e-9)
    }

    /** All three decompositions produce the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaInertia = (2 of kilogramMetersSquared) * omega
        val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
        val viaNative = (6 of kilogramMetersSquaredPerSecond)
        assertEquals(viaInertia, viaMomentum)
        assertEquals(viaInertia, viaNative)
    }

    /** `angularmomentum / inertia = angularvelocity` and `angularmomentum / angularvelocity = inertia`. */
    @Test
    fun `rotational inverses`() {
        val l = 6 of kilogramMetersSquaredPerSecond
        val w: KAngularVelocityUnitInstance = l / (2 of kilogramMetersSquared)
        val j: KInertiaUnitInstance = l / omega
        assertIs<KAngularVelocityUnitInstance>(w)
        assertIs<KInertiaUnitInstance>(j)
        assertEquals(3.0, w.value, 1e-9)
        assertEquals(2.0, j into kilogramMetersSquared, 1e-9)
    }

    /** `angularmomentum / length = momentum` and `angularmomentum / momentum = length`. */
    @Test
    fun `linear inverses`() {
        val l = 6 of kilogramMetersSquaredPerSecond
        val p: KMomentumUnitInstance = l / (2 of meters)
        val r: KLengthUnitInstance = l / (3 of kilogramMetersPerSecond)
        assertIs<KMomentumUnitInstance>(p)
        assertIs<KLengthUnitInstance>(r)
        assertEquals(3.0, p into kilogramMetersPerSecond, 1e-9)
        assertEquals(2.0, r into meters, 1e-9)
    }
}
