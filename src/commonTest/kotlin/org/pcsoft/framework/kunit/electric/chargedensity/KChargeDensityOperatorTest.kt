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

package org.pcsoft.framework.kunit.electric.chargedensity

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group charge density operators: `charge / volume = charge density` and its inverses. */
class KChargeDensityOperatorTest {

    /** `charge / volume = charge density` (`ρ = Q / V`). */
    @Test
    fun `charge over volume is charge density`() {
        val rho = (6 of coulombs) / (2 of liters) // 3 C/L = 3000 C/m³
        assertIs<KChargeDensityUnitInstance>(rho)
        assertEquals(3000.0, rho into (coulombs / (meters pow 3)), 1e-9)
    }

    /** `charge density * volume = charge` and the commutative `volume * charge density = charge`. */
    @Test
    fun `charge density times volume is charge`() {
        val rho = (2 of coulombs) / (1 of liters) // 2000 C/m³
        val q1 = rho * (3 of liters)
        val q2 = (3 of liters) * rho
        assertIs<KChargeUnitInstance>(q1)
        assertIs<KChargeUnitInstance>(q2)
        assertEquals(6.0, q1 into coulombs, 1e-9)
        assertEquals(6.0, q2 into coulombs, 1e-9)
        assertEquals(q1, q2)
    }

    /** `charge / charge density = volume` (`V = Q / ρ`). */
    @Test
    fun `charge over charge density is volume`() {
        val rho = (2 of coulombs) / (1 of liters) // 2000 C/m³
        val v: KVolumeUnitInstance = (6 of coulombs) / rho
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(3.0, v into liters, 1e-9)
    }

    /**
     * All decompositions are value-equal: the typed `charge / volume` operator and the native
     * `current¹·time¹·distance⁻³` expression narrowed with `toChargeDensity()` yield the same typed value.
     *
     * Real-world example: 12 mC of space charge dissolved in 4 L of electrolyte.
     */
    @Test
    fun `all decompositions are value-equal`() {
        val typed = (0.012 of coulombs) / (4 of liters) // 3 C/m³
        val native = ((0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))).toChargeDensity()
        assertIs<KChargeDensityUnitInstance>(typed)
        assertIs<KChargeDensityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(3.0, typed into (coulombs / (meters pow 3)), 1e-9)
        assertEquals(3.0, native into (coulombs / (meters pow 3)), 1e-9)
    }
}
