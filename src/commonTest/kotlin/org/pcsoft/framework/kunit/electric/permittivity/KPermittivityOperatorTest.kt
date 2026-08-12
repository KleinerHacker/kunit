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

package org.pcsoft.framework.kunit.electric.permittivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.KCapacitanceUnitInstance
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.fluxdensity.KElectricFluxDensityUnitInstance
import org.pcsoft.framework.kunit.electric.fluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The permittivity operators: all three decompositions (`capacitance/length`,
 * `electricFluxDensity/electricFieldStrength` and the canonical native `mass⁻¹·length⁻³·time⁴·current²` form
 * via `toPermittivity`), the inverse operators, and the invalid-shape guard.
 */
class KPermittivityOperatorTest {

    /** All permittivity decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all permittivity decompositions agree`() {
        // Decomposition A: capacitance over the plate geometry
        val fromCapacitance = (10 of farads) / (5 of meters)
        assertEquals(2 of faradsPerMeter, fromCapacitance)

        // Decomposition B: flux density over field strength
        val fromFields = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)
        assertEquals(2 of faradsPerMeter, fromFields)

        // Decomposition C: native canonical expression
        val native = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
        assertEquals(2 of faradsPerMeter, native.toPermittivity())
        assertEquals(2.0, native.toPermittivity().value, 1e-9)

        // All three funnel into permittivityInstanceOf and are value-equal
        assertEquals(fromCapacitance, fromFields)
        assertEquals(fromCapacitance, native.toPermittivity())
        assertEquals(fromFields.value, native.toPermittivity().value, 1e-9)
    }

    /** Inverse and commutative operators of the capacitance decomposition: `ε·l = C`, `C/ε = l`. */
    @Test
    fun `inverse capacitance operators`() {
        val eps = 2 of faradsPerMeter
        assertIs<KCapacitanceUnitInstance>(eps * (5 of meters))
        assertEquals(10 of farads, eps * (5 of meters))
        assertEquals(10 of farads, (5 of meters) * eps)
        assertIs<KLengthUnitInstance>((10 of farads) / eps)
        assertEquals(5.0, (10 of farads) / eps into meters, 1e-12)
    }

    /** Inverse and commutative operators of the field decomposition: `ε·E = D`, `D/ε = E`. */
    @Test
    fun `inverse field operators`() {
        val eps = 2 of faradsPerMeter
        assertIs<KElectricFluxDensityUnitInstance>(eps * (3 of voltsPerMeter))
        assertEquals(6 of coulombsPerSquareMeter, eps * (3 of voltsPerMeter))
        assertEquals(6 of coulombsPerSquareMeter, (3 of voltsPerMeter) * eps)
        assertIs<KElectricFieldStrengthUnitInstance>((6 of coulombsPerSquareMeter) / eps)
        assertEquals(3.0, (6 of coulombsPerSquareMeter) / eps into voltsPerMeter, 1e-12)
    }

    /** Real-world example: in vacuum a field of 1 MV/m produces a flux density of 8.854 µC/m². */
    @Test
    fun `vacuum flux density real world`() {
        val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)
        assertEquals(8.8541878188e-6, d.value, 1e-18)
        assertEquals(8.8541878188e-12, (1 of vacuumPermittivity) into faradsPerMeter, 1e-24)
        assertEquals(8.8541878188, (1 of vacuumPermittivity) into pico.faradsPerMeter, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a permittivity. */
    @Test
    fun `invalid permittivity decomposition fails`() {
        // wrong distance exponent (-2 instead of -3) - that is a capacitance
        val wrongDistance = ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongDistance.toPermittivity() }
        // wrong time exponent (+3 instead of +4)
        val wrongTime = ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
        assertFailsWith<IllegalStateException> { wrongTime.toPermittivity() }
        // wrong current exponent (+1 instead of +2)
        val wrongCurrent = ((seconds pow 4) * (amperes pow 1)) / (kilo.grams * (meters pow 3))
        assertFailsWith<IllegalStateException> { wrongCurrent.toPermittivity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toPermittivity() }
    }
}
