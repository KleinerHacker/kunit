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

package org.pcsoft.framework.kunit.electric.currentdensity

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group current density operators: `current / area = current density` and its inverses. */
class KCurrentDensityOperatorTest {

    /** `current / area = current density` (`J = I / A`). */
    @Test
    fun `current over area is current density`() {
        val j = (10 of amperes) / ((1 of meters) * (2 of meters)) // 5 A/m²
        assertIs<KCurrentDensityUnitInstance>(j)
        assertEquals(5.0, j into (amperes / (meters pow 2)), 1e-9)
    }

    /** `current density * area = current` and the commutative `area * current density = current`. */
    @Test
    fun `current density times area is current`() {
        val j = (2 of amperes) / ((1 of meters) * (1 of meters)) // 2 A/m²
        val area = (2 of meters) * (3 of meters) // 6 m²
        val i1 = j * area
        val i2 = area * j
        assertIs<KElectricCurrentUnitInstance>(i1)
        assertIs<KElectricCurrentUnitInstance>(i2)
        assertEquals(12.0, i1 into amperes, 1e-9)
        assertEquals(12.0, i2 into amperes, 1e-9)
        assertEquals(i1, i2)
    }

    /** `current / current density = area` (`A = I / J`). */
    @Test
    fun `current over current density is area`() {
        val j = (2 of amperes) / ((1 of meters) * (1 of meters)) // 2 A/m²
        val area: KAreaUnitInstance = (12 of amperes) / j
        assertIs<KAreaUnitInstance>(area)
        assertEquals(6.0, area.value, 1e-9)
    }

    /**
     * All decompositions are value-equal: the typed `current / area` operator and the native
     * `current¹·distance⁻²` expression narrowed with `toCurrentDensity()` yield the same typed value.
     *
     * Real-world example: 16 A through a 2.5 mm² copper wire is a current density of 6.4 A/mm².
     */
    @Test
    fun `all decompositions are value-equal`() {
        val crossSection = (2.5 of milli.meters) * (1 of milli.meters) // 2.5 mm² = 2.5e-6 m²
        val typed = (16 of amperes) / crossSection
        val native = ((16 of amperes).toUnit() / (2.5e-6 of ((meters pow 2)))).toCurrentDensity()
        assertIs<KCurrentDensityUnitInstance>(typed)
        assertIs<KCurrentDensityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(6.4e6, typed into (amperes / (meters pow 2)), 1e-3)
        assertEquals(6.4, native into (amperes / (milli.meters pow 2)), 1e-9)
    }
}
