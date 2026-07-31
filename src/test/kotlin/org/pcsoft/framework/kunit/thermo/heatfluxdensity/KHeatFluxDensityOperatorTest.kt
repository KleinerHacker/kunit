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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group heat flux density operators and the equivalence of all decompositions. */
class KHeatFluxDensityOperatorTest {

    private val area = (2 of meters) * (1 of meters) // 2 m²

    /** `power / area = heat flux density`. */
    @Test
    fun `power over area is heat flux density`() {
        val q = (1000 of watts) / area
        assertIs<KHeatFluxDensityUnitInstance>(q)
        assertEquals(500.0, q into wattsPerSquareMeter, 1e-9)
    }

    /** `heat flux density * area = power` and its commutative counterpart. */
    @Test
    fun `heat flux density times area is power`() {
        val q = 500 of wattsPerSquareMeter
        val p1 = q * area
        val p2 = area * q
        assertIs<KPowerUnitInstance>(p1)
        assertIs<KPowerUnitInstance>(p2)
        assertEquals(1000.0, p1 into watts, 1e-9)
        assertEquals(1000.0, p2 into watts, 1e-9)
    }

    /** `power / heat flux density = area`. */
    @Test
    fun `power over heat flux density is area`() {
        val a = (1000 of watts) / (500 of wattsPerSquareMeter)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, a into ((1 of meters) * (1 of meters)), 1e-9)
    }

    /**
     * Both decompositions - the typed `power / area` operator and the native `mass·time⁻³` expression
     * through `toHeatFluxDensity()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (1 of watts) / ((1 of meters) * (1 of meters))
        val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()
        assertIs<KHeatFluxDensityUnitInstance>(typed)
        assertIs<KHeatFluxDensityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into wattsPerSquareMeter, 1e-9)
    }
}
