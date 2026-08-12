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

package org.pcsoft.framework.kunit.common.energy

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.KAngularAccelerationUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.KAngularVelocityUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.KInertiaUnitInstance
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.of

/**
 * Torque decomposition of the energy group (`N·m` has the dimension of a joule): the lever-arm form
 * `force * length` and the rotational-power form `P = M · ω`.
 */
class KTorqueOperatorTest {

    /** `force * length = torque` (the same operator as mechanical work), read in N·m. */
    @Test
    fun `force times lever arm is torque`() {
        val m = (100 of newtons) * (2 of meters)
        assertIs<KEnergyUnitInstance>(m)
        assertEquals(200.0, m into joules, 1e-9)
    }

    /** `torque * angularvelocity = power`, plus its commutative form. */
    @Test
    fun `torque times angular velocity is power`() {
        val torque = 200 of joules
        val omega = 1 of revolutionsPerSecond
        val p1 = torque * omega
        val p2 = omega * torque
        assertIs<KPowerUnitInstance>(p1)
        assertIs<KPowerUnitInstance>(p2)
        assertEquals(200.0 * 2.0 * PI, p1 into watts, 1e-6)
        assertEquals(200.0 * 2.0 * PI, p2 into watts, 1e-6)
    }

    /** `power / angularvelocity = torque` - the everyday drivetrain formula. */
    @Test
    fun `power over angular velocity is torque`() {
        val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
        assertIs<KEnergyUnitInstance>(torque)
        assertEquals(200.0, torque into joules, 0.1)
    }

    /** `power / torque = angularvelocity`. */
    @Test
    fun `power over torque is angular velocity`() {
        val omega: KAngularVelocityUnitInstance = (400.0 * PI of watts) / (200 of joules)
        assertIs<KAngularVelocityUnitInstance>(omega)
        assertEquals(60.0, omega into revolutionsPerMinute, 1e-6)
    }

    /** `inertia * angularacceleration = torque` (`M = J · α`), plus its commutative form. */
    @Test
    fun `inertia times angular acceleration is torque`() {
        val j = 1.8 of kilogramMetersSquared
        val alpha = 100 of radiansPerSecondSquared
        val m1 = j * alpha
        val m2 = alpha * j
        assertIs<KEnergyUnitInstance>(m1)
        assertIs<KEnergyUnitInstance>(m2)
        assertEquals(180.0, m1 into joules, 1e-9)
        assertEquals(180.0, m2 into joules, 1e-9)
    }

    /** `torque / inertia = angularacceleration` and `torque / angularacceleration = inertia`. */
    @Test
    fun `rotational newton inverses`() {
        val torque = 180 of joules
        val alpha: KAngularAccelerationUnitInstance = torque / (1.8 of kilogramMetersSquared)
        val j: KInertiaUnitInstance = torque / (100 of radiansPerSecondSquared)
        assertIs<KAngularAccelerationUnitInstance>(alpha)
        assertIs<KInertiaUnitInstance>(j)
        assertEquals(100.0, alpha into radiansPerSecondSquared, 1e-9)
        assertEquals(1.8, j into kilogramMetersSquared, 1e-9)
    }

    /** The lever-arm form and the power form describe the same torque value. */
    @Test
    fun `both torque readings agree`() {
        val viaLever = (100 of newtons) * (2 of meters)
        val viaPower = (200.0 * 2.0 * PI of watts) / (1 of revolutionsPerSecond)
        assertEquals(viaLever into joules, viaPower into joules, 1e-6)
    }
}
