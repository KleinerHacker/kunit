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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group luminous efficacy operators and the typed/native decomposition equivalence. */
class KLuminousEfficacyOperatorTest {

    /** `luminousFlux / power = luminous efficacy`. */
    @Test
    fun `luminous flux over power is luminous efficacy`() {
        val eta = (1200 of lumens) / (10 of watts)
        assertIs<KLuminousEfficacyUnitInstance>(eta)
        assertEquals(120.0, eta into lumensPerWatt, 1e-9)
    }

    /** `luminous efficacy * power = luminousFlux` and its commutative counterpart. */
    @Test
    fun `luminous efficacy times power is luminous flux`() {
        val eta = 120 of lumensPerWatt
        val p = 10 of watts
        val phi1 = eta * p
        val phi2 = p * eta
        assertIs<KLuminousFluxUnitInstance>(phi1)
        assertIs<KLuminousFluxUnitInstance>(phi2)
        assertEquals(1200.0, phi1 into lumens, 1e-9)
        assertEquals(1200.0, phi2 into lumens, 1e-9)
    }

    /** `luminousFlux / luminous efficacy = power`. */
    @Test
    fun `luminous flux over luminous efficacy is power`() {
        val p = (1200 of lumens) / (120 of lumensPerWatt)
        assertIs<KPowerUnitInstance>(p)
        assertEquals(10.0, p into watts, 1e-9)
    }

    /**
     * The typed operator and the native `cd·sr·kg⁻¹·m⁻²·s³` expression agree.
     *
     * The native form is assembled from *unit templates*, not from typed instances: for a group carrying a
     * mass term the raw mixed value is the gram-based product, while a typed instance stores its value in
     * the named unit (watts). Building from templates keeps both readings consistent.
     */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (1200 of lumens) / (10 of watts)
        val native = (
                120 of (candelas.toUnit() * steradians.toUnit()) /
                        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
                ).toLuminousEfficacy()
        assertIs<KLuminousEfficacyUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into lumensPerWatt, native into lumensPerWatt, 1e-9)
    }
}
