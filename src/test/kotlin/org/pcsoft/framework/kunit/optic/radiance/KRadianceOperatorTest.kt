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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.KRadiantIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group radiance operators and the typed/native decomposition equivalence. */
class KRadianceOperatorTest {

    private val squareMeter: KAreaUnitInstance = (1 of meters) * (1 of meters)

    /** `radiantIntensity / area = radiance`. */
    @Test
    fun `radiant intensity over area is radiance`() {
        val l = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
        assertIs<KRadianceUnitInstance>(l)
        assertEquals(5.0, l into wattsPerSteradianSquareMeter, 1e-9)
    }

    /** `radiance * area = radiantIntensity` and its commutative counterpart. */
    @Test
    fun `radiance times area is radiant intensity`() {
        val l = 5 of wattsPerSteradianSquareMeter
        val a = (2 of meters) * (1 of meters)
        val i1 = l * a
        val i2 = a * l
        assertIs<KRadiantIntensityUnitInstance>(i1)
        assertIs<KRadiantIntensityUnitInstance>(i2)
        assertEquals(10.0, i1 into wattsPerSteradian, 1e-9)
        assertEquals(10.0, i2 into wattsPerSteradian, 1e-9)
    }

    /** `radiantIntensity / radiance = area`. */
    @Test
    fun `radiant intensity over radiance is area`() {
        val a = (10 of wattsPerSteradian) / (5 of wattsPerSteradianSquareMeter)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, a into squareMeter, 1e-9)
    }

    /** The typed operator and the native `kg·s⁻³·sr⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
        val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()
        assertIs<KRadianceUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(
            typed into wattsPerSteradianSquareMeter,
            native into wattsPerSteradianSquareMeter,
            1e-9,
        )
    }
}
