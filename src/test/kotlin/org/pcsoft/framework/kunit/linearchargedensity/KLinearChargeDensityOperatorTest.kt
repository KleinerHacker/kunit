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

package org.pcsoft.framework.kunit.linearchargedensity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The linear charge density operators: both decompositions (`charge/length` and the canonical native
 * `current·time·distance⁻¹` form via `toLinearChargeDensity`), the inverse operators, and the invalid-shape
 * guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLinearChargeDensityOperatorTest {

    /** Both linear charge density decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all linear charge density decompositions agree`() {
        // Decomposition A: charge along a length
        val fromCharge = (6 of coulombs) / (3 of meters)
        assertEquals(2.0, fromCharge.value, 1e-9)

        // Decomposition B: native canonical expression
        val native = 2 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
        assertEquals(2.0, native.toLinearChargeDensity().value, 1e-9)

        // Both funnel into linearChargeDensityInstanceOf and are value-equal
        assertEquals(fromCharge, native.toLinearChargeDensity())
        assertEquals(fromCharge.value, native.toLinearChargeDensity().value, 1e-9)
    }

    /** Inverse and commutative operators: `λ·l = Q`, `Q/λ = l`. */
    @Test
    fun `inverse linear charge density operators`() {
        val lambda = (6 of coulombs) / (3 of meters)
        assertIs<KChargeUnitInstance>(lambda * (3 of meters))
        assertEquals(6 of coulombs, lambda * (3 of meters))
        assertEquals(6 of coulombs, (3 of meters) * lambda)
        assertIs<KLengthUnitInstance>((6 of coulombs) / lambda)
        assertEquals(3.0, (6 of coulombs) / lambda into meters, 1e-12)
    }

    /** Real-world example: a filament carrying 5 µC over 2 m has a linear charge density of 2.5 µC/m. */
    @Test
    fun `charged filament real world`() {
        val lambda = (5 of micro.coulombs) / (2 of meters)
        assertEquals(2.5e-6, lambda.value, 1e-20)
        assertEquals(5.0, (lambda * (2 of meters)) into micro.coulombs, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a linear charge density. */
    @Test
    fun `invalid linear charge density decomposition fails`() {
        // wrong distance exponent (-2 instead of -1) - that is an electric flux density
        val wrongDistance = ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
        assertFailsWith<IllegalStateException> { wrongDistance.toLinearChargeDensity() }
        // wrong time exponent (+2 instead of +1)
        val wrongTime = ((amperes pow 1) * (seconds pow 2)) / (meters pow 1)
        assertFailsWith<IllegalStateException> { wrongTime.toLinearChargeDensity() }
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = ((amperes pow 2) * (seconds pow 1)) / (meters pow 1)
        assertFailsWith<IllegalStateException> { wrongCurrent.toLinearChargeDensity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toLinearChargeDensity() }
    }
}
