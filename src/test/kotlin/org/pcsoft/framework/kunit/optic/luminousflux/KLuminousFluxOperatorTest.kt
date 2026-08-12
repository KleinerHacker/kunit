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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group luminous flux operators and the equivalence of the typed and native decomposition. */
class KLuminousFluxOperatorTest {

    /** `luminousIntensity * solidAngle = luminous flux` and its commutative counterpart. */
    @Test
    fun `luminous intensity times solid angle is luminous flux`() {
        val i = 100 of candelas
        val omega = 2 of steradians
        val phi1 = i * omega
        val phi2 = omega * i
        assertIs<KLuminousFluxUnitInstance>(phi1)
        assertIs<KLuminousFluxUnitInstance>(phi2)
        assertEquals(200.0, phi1 into lumens, 1e-9)
        assertEquals(200.0, phi2 into lumens, 1e-9)
    }

    /** `luminous flux / solidAngle = luminousIntensity`. */
    @Test
    fun `luminous flux over solid angle is luminous intensity`() {
        val i = (200 of lumens) / (2 of steradians)
        assertIs<KLuminousIntensityUnitInstance>(i)
        assertEquals(100.0, i into candelas, 1e-9)
    }

    /** `luminous flux / luminousIntensity = solidAngle`. */
    @Test
    fun `luminous flux over luminous intensity is solid angle`() {
        val omega = (200 of lumens) / (100 of candelas)
        assertIs<KSolidAngleUnitInstance>(omega)
        assertEquals(2.0, omega into steradians, 1e-9)
    }

    /** The typed operator and the native `cd·sr` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (100 of candelas) * (2 of steradians)
        val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()
        assertIs<KLuminousFluxUnitInstance>(typed)
        assertIs<KLuminousFluxUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into lumens, native into lumens, 1e-12)
    }

    /** An isotropic point source: the full sphere is 4π sr. */
    @Test
    fun `isotropic lamp total flux`() {
        val phi = (100 of candelas) * ((4.0 * Math.PI) of steradians)
        assertEquals(400.0 * Math.PI, phi into lumens, 1e-9)
    }
}
