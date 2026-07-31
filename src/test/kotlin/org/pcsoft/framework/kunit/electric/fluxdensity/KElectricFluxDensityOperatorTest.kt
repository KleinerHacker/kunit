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

package org.pcsoft.framework.kunit.electric.fluxdensity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.ares
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The electric flux density operators: both decompositions (`charge/area` and the canonical native
 * `current·time·distance⁻²` form via `toElectricFluxDensity`), the inverse operators, and the invalid-shape
 * guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricFluxDensityOperatorTest {

    /** 3 m², built through the area group (1 are = 100 m²). */
    private val threeSquareMeters: KAreaUnitInstance = 0.03 of ares

    /** Both electric flux density decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all electric flux density decompositions agree`() {
        // Decomposition A: charge over an area
        val fromCharge = (6 of coulombs) / threeSquareMeters
        assertEquals(2.0, fromCharge.value, 1e-9)

        // Decomposition B: native canonical expression
        val native = 2 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
        assertEquals(2 of coulombsPerSquareMeter, native.toElectricFluxDensity())
        assertEquals(2.0, native.toElectricFluxDensity().value, 1e-9)

        // Both funnel into electricFluxDensityInstanceOf and are value-equal
        assertEquals(fromCharge, native.toElectricFluxDensity())
        assertEquals(fromCharge.value, native.toElectricFluxDensity().value, 1e-9)
    }

    /** Inverse and commutative operators: `D·A = Q`, `Q/D = A`. */
    @Test
    fun `inverse electric flux density operators`() {
        val d = 2 of coulombsPerSquareMeter
        assertIs<KChargeUnitInstance>(d * threeSquareMeters)
        assertEquals(6 of coulombs, d * threeSquareMeters)
        assertEquals(6 of coulombs, threeSquareMeters * d)
        assertIs<KAreaUnitInstance>((6 of coulombs) / d)
        assertEquals(0.03, (6 of coulombs) / d into ares, 1e-12)
    }

    /** Real-world example: 20 µC spread over a 4 m² capacitor plate gives 5 µC/m². */
    @Test
    fun `capacitor plate density real world`() {
        val plate: KAreaUnitInstance = 0.04 of ares
        val d = (20 of micro.coulombs) / plate
        assertEquals(5e-6, d.value, 1e-18)
        assertEquals(5.0, d into micro.coulombsPerSquareMeter, 1e-9)
    }

    /** A non-canonical shape cannot be read back as an electric flux density. */
    @Test
    fun `invalid electric flux density decomposition fails`() {
        // wrong distance exponent (-3 instead of -2) - that is a charge density
        val wrongDistance = ((amperes pow 1) * (seconds pow 1)) / (meters pow 3)
        assertFailsWith<IllegalStateException> { wrongDistance.toElectricFluxDensity() }
        // wrong time exponent (+2 instead of +1)
        val wrongTime = ((amperes pow 1) * (seconds pow 2)) / (meters pow 2)
        assertFailsWith<IllegalStateException> { wrongTime.toElectricFluxDensity() }
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = ((amperes pow 2) * (seconds pow 1)) / (meters pow 2)
        assertFailsWith<IllegalStateException> { wrongCurrent.toElectricFluxDensity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toElectricFluxDensity() }
    }
}
