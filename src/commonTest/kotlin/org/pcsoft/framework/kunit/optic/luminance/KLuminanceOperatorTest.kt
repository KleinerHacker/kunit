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

package org.pcsoft.framework.kunit.optic.luminance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.optic.illuminance.KIlluminanceUnitInstance
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group luminance operators and the equivalence of all decompositions. */
class KLuminanceOperatorTest {

    private val squareMeter: KAreaUnitInstance = (1 of meters) * (1 of meters)

    /** `luminousIntensity / area = luminance`. */
    @Test
    fun `luminous intensity over area is luminance`() {
        val l = (250 of candelas) / squareMeter
        assertIs<KLuminanceUnitInstance>(l)
        assertEquals(250.0, l into candelasPerSquareMeter, 1e-9)
    }

    /** `illuminance / solidAngle = luminance` - the second decomposition. */
    @Test
    fun `illuminance over solid angle is luminance`() {
        val l = (500 of lux) / (2 of steradians)
        assertIs<KLuminanceUnitInstance>(l)
        assertEquals(250.0, l into candelasPerSquareMeter, 1e-9)
    }

    /** Both decompositions yield the same typed, value-equal result. */
    @Test
    fun `both decompositions agree`() {
        val viaIntensity = (250 of candelas) / squareMeter
        val viaIlluminance = (500 of lux) / (2 of steradians)
        assertEquals(viaIntensity, viaIlluminance)
        assertEquals(
            viaIntensity into candelasPerSquareMeter,
            viaIlluminance into candelasPerSquareMeter,
            1e-12,
        )
    }

    /** `luminance * area = luminousIntensity` and its commutative counterpart. */
    @Test
    fun `luminance times area is luminous intensity`() {
        val l = 250 of candelasPerSquareMeter
        val i1 = l * squareMeter
        val i2 = squareMeter * l
        assertIs<KLuminousIntensityUnitInstance>(i1)
        assertIs<KLuminousIntensityUnitInstance>(i2)
        assertEquals(250.0, i1 into candelas, 1e-9)
        assertEquals(250.0, i2 into candelas, 1e-9)
    }

    /** `luminousIntensity / luminance = area`. */
    @Test
    fun `luminous intensity over luminance is area`() {
        val a = (500 of candelas) / (250 of candelasPerSquareMeter)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, a into squareMeter, 1e-9)
    }

    /** `luminance * solidAngle = illuminance` and its commutative counterpart. */
    @Test
    fun `luminance times solid angle is illuminance`() {
        val l = 250 of candelasPerSquareMeter
        val omega = 2 of steradians
        val e1 = l * omega
        val e2 = omega * l
        assertIs<KIlluminanceUnitInstance>(e1)
        assertIs<KIlluminanceUnitInstance>(e2)
        assertEquals(500.0, e1 into lux, 1e-9)
        assertEquals(500.0, e2 into lux, 1e-9)
    }

    /** `illuminance / luminance = solidAngle`. */
    @Test
    fun `illuminance over luminance is solid angle`() {
        val omega = (500 of lux) / (250 of candelasPerSquareMeter)
        assertIs<KSolidAngleUnitInstance>(omega)
        assertEquals(2.0, omega into steradians, 1e-9)
    }

    /** The typed operator and the native `cd·m⁻²` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (250 of candelas) / squareMeter
        val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()
        assertIs<KLuminanceUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into candelasPerSquareMeter, native into candelasPerSquareMeter, 1e-12)
    }
}
