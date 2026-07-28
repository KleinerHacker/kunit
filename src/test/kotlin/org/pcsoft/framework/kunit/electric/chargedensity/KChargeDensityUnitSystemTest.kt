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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * `KChargeDensityUnitInstance` surface: construction via expressions, `into`, equality, `toString`,
 * same-type operators and the `toChargeDensity()` normal-form hook.
 */
class KChargeDensityUnitSystemTest {

    /** A charge density built as `charge / volume` reads back through an expression `into` target. */
    @Test
    fun `construction and round-trip`() {
        val rho = (6 of coulombs) / (2 of liters) // 3 C/L = 3000 C/m³
        assertEquals(3000.0, rho into (coulombs / (meters pow 3)), 1e-9)
        assertEquals(0.003, rho into (coulombs / (centi.meters pow 3)), 1e-12)
        assertEquals(3.0e6, rho into (milli.coulombs / (meters pow 3)), 1e-3)
    }

    /** `of` on a charge density template scales it (covers scaledBy). */
    @Test
    fun `of scales charge density`() {
        val rho = (2 of coulombs) / (1 of liters) // 2000 C/m³
        val scaled = 3 of rho
        assertEquals(6000.0, scaled into (coulombs / (meters pow 3)), 1e-9)
    }

    /** Equality/hash by normalized value in C/m³. */
    @Test
    fun `equals and hashCode`() {
        val a = (2 of coulombs) / (1 of liters)
        val b = (4 of coulombs) / (2 of liters)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertFalse(a == (1 of coulombs) / (1 of liters))
        assertFalse(a.equals(1.0))
    }

    /** `toString` renders the value in C/m³. */
    @Test
    fun `toString base unit`() {
        val rho = (1 of coulombs) / (1 of liters) // 1000 C/m³
        assertEquals("1000.0 C/m³", rho.toString())
    }

    /** Same-type `+`/`-`/comparison, and `*`/`/` escaping to a mixed unit. */
    @Test
    fun `same-type operators`() {
        val a = (3 of coulombs) / (1 of liters) // 3000 C/m³
        val b = (1 of coulombs) / (1 of liters) // 1000 C/m³
        assertEquals(4000.0, (a + b) into (coulombs / (meters pow 3)), 1e-9)
        assertEquals(2000.0, (a - b) into (coulombs / (meters pow 3)), 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A `current¹·time¹·distance⁻³` mixed unit converts back to a pure charge density; wrong shapes fail. */
    @Test
    fun `toChargeDensity round-trip and failure`() {
        val raw = (6 of coulombs).toUnit() / ((1 of meters).toUnit() pow 3) // [A^1, s^1, m^-3], 6 C/m³
        assertEquals(6.0, raw.toChargeDensity() into (coulombs / (meters pow 3)), 1e-9)
        assertEquals((6 of coulombs) / (1000 of liters), raw.toChargeDensity())

        assertFailsWith<IllegalStateException> { (6 of coulombs).toUnit().toChargeDensity() } // 2 terms
        assertFailsWith<IllegalStateException> {
            ((6 of coulombs).toUnit() / ((1 of meters).toUnit() pow 2)).toChargeDensity() // distance^-2
        }
        assertFailsWith<IllegalStateException> {
            (((6 of coulombs).toUnit() pow 2) / ((1 of meters).toUnit() pow 3)).toChargeDensity() // current^2
        }
        assertFailsWith<IllegalStateException> {
            ((6 of coulombs).toUnit() * (1 of seconds).toUnit() / ((1 of meters).toUnit() pow 3))
                .toChargeDensity() // time^2
        }
    }
}
