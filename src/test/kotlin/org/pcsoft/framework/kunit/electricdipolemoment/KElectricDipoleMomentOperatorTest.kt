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

package org.pcsoft.framework.kunit.electricdipolemoment

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The electric dipole moment operators: both decompositions (`charge*length` and the canonical native
 * `current·time·distance` form via `toElectricDipoleMoment`), the inverse operators, and the invalid-shape
 * guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricDipoleMomentOperatorTest {

    /** Both electric dipole moment decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all electric dipole moment decompositions agree`() {
        // Decomposition A: charge times separation
        val fromCharge = (2 of coulombs) * (3 of meters)
        assertEquals(6 of coulombMeters, fromCharge)
        assertEquals(6 of coulombMeters, (3 of meters) * (2 of coulombs))

        // Decomposition B: native canonical expression
        val native = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
        assertEquals(6 of coulombMeters, native.toElectricDipoleMoment())
        assertEquals(6.0, native.toElectricDipoleMoment().value, 1e-9)

        // Both funnel into electricDipoleMomentInstanceOf and are value-equal
        assertEquals(fromCharge, native.toElectricDipoleMoment())
        assertEquals(fromCharge.value, native.toElectricDipoleMoment().value, 1e-9)
    }

    /** Inverse operators: `p/Q = d`, `p/d = Q`. */
    @Test
    fun `inverse electric dipole moment operators`() {
        val p = 6 of coulombMeters
        assertIs<KLengthUnitInstance>(p / (2 of coulombs))
        assertEquals(3.0, p / (2 of coulombs) into meters, 1e-12)
        assertIs<KChargeUnitInstance>(p / (3 of meters))
        assertEquals(2.0, p / (3 of meters) into coulombs, 1e-12)
    }

    /** Real-world example: 1 pC separated by 1 nm gives 1e-21 C·m, about 3.0e8 debyes. */
    @Test
    fun `separated charge real world`() {
        val p = (1 of pico.coulombs) * (1 of nano.meters)
        assertEquals(1e-21, p.value, 1e-33)
        assertEquals(2.997924579983392e8, p into debyes, 1e-1)
    }

    /** A non-canonical shape cannot be read back as an electric dipole moment. */
    @Test
    fun `invalid electric dipole moment decomposition fails`() {
        // wrong distance exponent (+2 instead of +1)
        val wrongDistance = (amperes pow 1) * (seconds pow 1) * (meters pow 2)
        assertFailsWith<IllegalStateException> { wrongDistance.toElectricDipoleMoment() }
        // wrong time exponent (+2 instead of +1)
        val wrongTime = (amperes pow 1) * (seconds pow 2) * (meters pow 1)
        assertFailsWith<IllegalStateException> { wrongTime.toElectricDipoleMoment() }
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = (amperes pow 2) * (seconds pow 1) * (meters pow 1)
        assertFailsWith<IllegalStateException> { wrongCurrent.toElectricDipoleMoment() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toElectricDipoleMoment() }
    }
}
