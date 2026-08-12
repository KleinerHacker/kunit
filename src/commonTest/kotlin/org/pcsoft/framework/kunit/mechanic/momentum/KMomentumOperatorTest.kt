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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed momentum operators: both decompositions (`mass * speed`, `force * time`) and the inverses. */
class KMomentumOperatorTest {

    private val speed: KSpeedUnitInstance = (3 of meters) / (1 of seconds)

    /** Decomposition A: `mass * speed = momentum`, plus its commutative form. */
    @Test
    fun `mass times speed is momentum`() {
        val p1 = (2 of kilo.grams) * speed
        val p2 = speed * (2 of kilo.grams)
        assertIs<KMomentumUnitInstance>(p1)
        assertIs<KMomentumUnitInstance>(p2)
        assertEquals(6.0, p1 into kilogramMetersPerSecond, 1e-9)
        assertEquals(6.0, p2 into kilogramMetersPerSecond, 1e-9)
    }

    /** Decomposition B (impulse): `force * time = momentum`, plus its commutative form. */
    @Test
    fun `force times time is impulse`() {
        val p1 = (10 of newtons) * (3 of seconds)
        val p2 = (3 of seconds) * (10 of newtons)
        assertIs<KMomentumUnitInstance>(p1)
        assertIs<KMomentumUnitInstance>(p2)
        assertEquals(30.0, p1 into newtonSeconds, 1e-9)
        assertEquals(30.0, p2 into newtonSeconds, 1e-9)
    }

    /** Both decompositions and the native form produce the same typed, value-equal result. */
    @Test
    fun `all decompositions agree`() {
        val viaMassSpeed = (2 of kilo.grams) * speed
        val viaForceTime = (6 of newtons) * (1 of seconds)
        val viaNative =
            ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()
        assertEquals(viaMassSpeed, viaForceTime)
        assertEquals(viaMassSpeed, viaNative)
    }

    /** `momentum / mass = speed` and `momentum / speed = mass`. */
    @Test
    fun `momentum inverses for mass and speed`() {
        val p = 6 of kilogramMetersPerSecond
        val v: KSpeedUnitInstance = p / (2 of kilo.grams)
        val m: KMassUnitInstance = p / speed
        assertIs<KSpeedUnitInstance>(v)
        assertIs<KMassUnitInstance>(m)
        assertEquals(3.0, v.value, 1e-9)
        assertEquals(2.0, m into kilo.grams, 1e-9)
    }

    /** `momentum / time = force` and `momentum / force = time`. */
    @Test
    fun `impulse inverses for force and time`() {
        val p = 30 of newtonSeconds
        val f: KForceUnitInstance = p / (3 of seconds)
        val t: KTimeUnitInstance = p / (10 of newtons)
        assertIs<KForceUnitInstance>(f)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(10.0, f into newtons, 1e-9)
        assertEquals(3.0, t into seconds, 1e-9)
    }
}
