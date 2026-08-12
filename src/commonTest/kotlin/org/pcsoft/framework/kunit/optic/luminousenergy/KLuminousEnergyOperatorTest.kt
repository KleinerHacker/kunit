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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group luminous energy operators and the equivalence of the typed and native decomposition. */
class KLuminousEnergyOperatorTest {

    /** `luminousFlux * time = luminous energy` and its commutative counterpart. */
    @Test
    fun `luminous flux times time is luminous energy`() {
        val phi = 800 of lumens
        val t = 2 of hours
        val q1 = phi * t
        val q2 = t * phi
        assertIs<KLuminousEnergyUnitInstance>(q1)
        assertIs<KLuminousEnergyUnitInstance>(q2)
        assertEquals(1600.0, q1 into lumenHours, 1e-9)
        assertEquals(1600.0, q2 into lumenHours, 1e-9)
    }

    /** `luminous energy / time = luminousFlux`. */
    @Test
    fun `luminous energy over time is luminous flux`() {
        val phi = (1600 of lumenHours) / (2 of hours)
        assertIs<KLuminousFluxUnitInstance>(phi)
        assertEquals(800.0, phi into lumens, 1e-9)
    }

    /** `luminous energy / luminousFlux = time`. */
    @Test
    fun `luminous energy over luminous flux is time`() {
        val t = (1600 of lumenHours) / (800 of lumens)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(2.0, t into hours, 1e-9)
    }

    /** The typed operator and the native `cd·sr·s` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (800 of lumens) * (5 of seconds)
        val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()
        assertIs<KLuminousEnergyUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into lumenSeconds, native into lumenSeconds, 1e-12)
    }
}
