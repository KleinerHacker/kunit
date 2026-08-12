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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group radiant intensity operators and the typed/native decomposition equivalence. */
class KRadiantIntensityOperatorTest {

    /** `power / solidAngle = radiant intensity`. */
    @Test
    fun `power over solid angle is radiant intensity`() {
        val i = (20 of watts) / (4 of steradians)
        assertIs<KRadiantIntensityUnitInstance>(i)
        assertEquals(5.0, i into wattsPerSteradian, 1e-9)
    }

    /** `radiant intensity * solidAngle = power` and its commutative counterpart. */
    @Test
    fun `radiant intensity times solid angle is power`() {
        val i = 5 of wattsPerSteradian
        val omega = 4 of steradians
        val p1 = i * omega
        val p2 = omega * i
        assertIs<KPowerUnitInstance>(p1)
        assertIs<KPowerUnitInstance>(p2)
        assertEquals(20.0, p1 into watts, 1e-9)
        assertEquals(20.0, p2 into watts, 1e-9)
    }

    /** `power / radiant intensity = solidAngle`. */
    @Test
    fun `power over radiant intensity is solid angle`() {
        val omega = (20 of watts) / (5 of wattsPerSteradian)
        assertIs<KSolidAngleUnitInstance>(omega)
        assertEquals(4.0, omega into steradians, 1e-9)
    }

    /** The typed operator and the native `kg·m²·s⁻³·sr⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (20 of watts) / (4 of steradians)
        val native = (
                5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
                ).toRadiantIntensity()
        assertIs<KRadiantIntensityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into wattsPerSteradian, native into wattsPerSteradian, 1e-9)
    }
}
