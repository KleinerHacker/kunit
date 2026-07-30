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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group volumetric flow operators and the equivalence of all decompositions. */
class KVolumeFlowOperatorTest {

    /** `volume / time = volumetric flow`. */
    @Test
    fun `volume over time is volumetric flow`() {
        val q = (600 of liters) / (2 of minutes)
        assertIs<KVolumeFlowUnitInstance>(q)
        assertEquals(0.005, q into cubicMetersPerSecond, 1e-12) // 0.6 m³ / 120 s
    }

    /** `volumetric flow * time = volume` and the commutative `time * volumetric flow = volume`. */
    @Test
    fun `volumetric flow times time is volume`() {
        val q = 5 of litersPerSecond
        val t = 2 of minutes
        val v1 = q * t
        val v2 = t * q
        assertIs<KVolumeUnitInstance>(v1)
        assertIs<KVolumeUnitInstance>(v2)
        assertEquals(600.0, v1 into liters, 1e-9)
        assertEquals(600.0, v2 into liters, 1e-9)
    }

    /** `volume / volumetric flow = time`. */
    @Test
    fun `volume over volumetric flow is time`() {
        val t = (600 of liters) / (5 of litersPerSecond)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(120.0, t into seconds, 1e-6)
    }

    /**
     * Both decompositions - the typed `volume / time` operator and the native `distance³·time⁻¹`
     * expression funneled through `toVolumeFlow()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (8000 of liters) / (4 of seconds) // 8 m³ in 4 s
        val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()
        assertIs<KVolumeFlowUnitInstance>(typed)
        assertIs<KVolumeFlowUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(2.0, typed into cubicMetersPerSecond, 1e-9)
    }
}
