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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousflux.times
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group illuminance operators and the equivalence of the typed and native decomposition. */
class KIlluminanceOperatorTest {

    private val squareMeter: KAreaUnitInstance = (1 of meters) * (1 of meters)

    /** `luminousFlux / area = illuminance`. */
    @Test
    fun `luminous flux over area is illuminance`() {
        val e = (1000 of lumens) / ((2 of meters) * (1 of meters))
        assertIs<KIlluminanceUnitInstance>(e)
        assertEquals(500.0, e into lux, 1e-9)
    }

    /** `illuminance * area = luminousFlux` and its commutative counterpart. */
    @Test
    fun `illuminance times area is luminous flux`() {
        val e = 500 of lux
        val a = (2 of meters) * (1 of meters)
        val phi1 = e * a
        val phi2 = a * e
        assertIs<KLuminousFluxUnitInstance>(phi1)
        assertIs<KLuminousFluxUnitInstance>(phi2)
        assertEquals(1000.0, phi1 into lumens, 1e-9)
        assertEquals(1000.0, phi2 into lumens, 1e-9)
    }

    /** `luminousFlux / illuminance = area`. */
    @Test
    fun `luminous flux over illuminance is area`() {
        val a = (1000 of lumens) / (500 of lux)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, a into squareMeter, 1e-9)
    }

    /** The typed operator and the native `cd·sr·m⁻²` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (1000 of lumens) / ((2 of meters) * (1 of meters))
        val native = (
                (1000 of candelas).toUnit() * (1 of steradians).toUnit() /
                        (((2 of meters) * (1 of meters)).toUnit())
                ).toIlluminance()
        assertIs<KIlluminanceUnitInstance>(typed)
        assertIs<KIlluminanceUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into lux, native into lux, 1e-12)
    }

    /** A 1 m² surface 1 m away from a 1 cd isotropic source receives 1 lx. */
    @Test
    fun `inverse square law at one meter`() {
        val phi = (1 of candelas) * (1 of steradians)
        val e = phi / squareMeter
        assertEquals(1.0, e into lux, 1e-9)
    }
}
