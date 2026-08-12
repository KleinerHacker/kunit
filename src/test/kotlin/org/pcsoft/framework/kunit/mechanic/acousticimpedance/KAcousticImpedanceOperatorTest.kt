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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group acoustic impedance operators and the equivalence of both decompositions. */
class KAcousticImpedanceOperatorTest {

    /** Air at 20 °C: 1.204 kg/m³. */
    private val air: KDensityUnitInstance =
        (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

    /** Speed of sound in air: 343 m/s. */
    private val soundInAir: KSpeedUnitInstance = (343 of meters) / (1 of seconds)

    /** `density * speed = acoustic impedance` (`Z = ρ · c`) and its commutative counterpart. */
    @Test
    fun `density times speed is acoustic impedance`() {
        val z1 = air * soundInAir
        val z2 = soundInAir * air
        assertIs<KAcousticImpedanceUnitInstance>(z1)
        assertIs<KAcousticImpedanceUnitInstance>(z2)
        assertEquals(412.972, z1 into rayls, 1e-3)
        assertEquals(412.972, z2 into rayls, 1e-3)
    }

    /** `pressure / speed = acoustic impedance`. */
    @Test
    fun `pressure over speed is acoustic impedance`() {
        val z = (413 of pascals) / ((1 of meters) / (1 of seconds))
        assertIs<KAcousticImpedanceUnitInstance>(z)
        assertEquals(413.0, z into rayls, 1e-9)
    }

    /** Both decompositions agree for the same physical situation. */
    @Test
    fun `both decompositions agree`() {
        val viaDensity = air * soundInAir
        val pressure = (412.972 of pascals)
        val viaPressure = pressure / ((1 of meters) / (1 of seconds))
        assertEquals(viaDensity into rayls, viaPressure into rayls, 1e-3)
    }

    /** `acoustic impedance * speed = pressure` and its commutative counterpart. */
    @Test
    fun `acoustic impedance times speed is pressure`() {
        val z = 413 of rayls
        val v = (1 of meters) / (1 of seconds)
        val p1 = z * v
        val p2 = v * z
        assertIs<KPressureUnitInstance>(p1)
        assertIs<KPressureUnitInstance>(p2)
        assertEquals(413.0, p1 into pascals, 1e-9)
        assertEquals(413.0, p2 into pascals, 1e-9)
    }

    /** `pressure / acoustic impedance = speed`. */
    @Test
    fun `pressure over acoustic impedance is speed`() {
        val v = (413 of pascals) / (413 of rayls)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(1.0, v into ((1 of meters) / (1 of seconds)), 1e-9)
    }

    /** `acoustic impedance / speed = density` and `/ density = speed`. */
    @Test
    fun `inverse decompositions`() {
        val z = air * soundInAir
        val d = z / soundInAir
        assertIs<KDensityUnitInstance>(d)
        assertEquals(
            1.204,
            d into ((1 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))),
            1e-9,
        )

        val c = z / air
        assertIs<KSpeedUnitInstance>(c)
        assertEquals(343.0, c into ((1 of meters) / (1 of seconds)), 1e-9)
    }
}
