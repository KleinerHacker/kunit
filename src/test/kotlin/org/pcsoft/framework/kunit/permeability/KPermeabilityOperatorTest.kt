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

package org.pcsoft.framework.kunit.permeability

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.inductance.KInductanceUnitInstance
import org.pcsoft.framework.kunit.inductance.henries
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.magneticfieldstrength.KMagneticFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.magneticfluxdensity.KMagneticFluxDensityUnitInstance
import org.pcsoft.framework.kunit.magneticfluxdensity.teslas
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
 * The permeability operators: all three decompositions (`inductance/length`,
 * `magneticFluxDensity/magneticFieldStrength` and the canonical native `mass·length·time⁻²·current⁻²` form
 * via `toPermeability`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KPermeabilityOperatorTest {

    /** All permeability decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all permeability decompositions agree`() {
        // Decomposition A: inductance over the coil geometry
        val fromInductance = (10 of henries) / (5 of meters)
        assertEquals(2 of henriesPerMeter, fromInductance)

        // Decomposition B: flux density over field strength
        val fromFields = (6 of teslas) / (3 of amperesPerMeter)
        assertEquals(2 of henriesPerMeter, fromFields)

        // Decomposition C: native canonical expression
        val native = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
        assertEquals(2 of henriesPerMeter, native.toPermeability())
        assertEquals(2.0, native.toPermeability().value, 1e-9)

        // All three funnel into permeabilityInstanceOf and are value-equal
        assertEquals(fromInductance, fromFields)
        assertEquals(fromInductance, native.toPermeability())
        assertEquals(fromFields.value, native.toPermeability().value, 1e-9)
    }

    /** Inverse and commutative operators of the inductance decomposition: `μ·l = L`, `L/μ = l`. */
    @Test
    fun `inverse inductance operators`() {
        val mu = 2 of henriesPerMeter
        assertIs<KInductanceUnitInstance>(mu * (5 of meters))
        assertEquals(10 of henries, mu * (5 of meters))
        assertEquals(10 of henries, (5 of meters) * mu)
        assertIs<KLengthUnitInstance>((10 of henries) / mu)
        assertEquals(5.0, (10 of henries) / mu into meters, 1e-12)
    }

    /** Inverse and commutative operators of the field decomposition: `μ·H = B`, `B/μ = H`. */
    @Test
    fun `inverse field operators`() {
        val mu = 2 of henriesPerMeter
        assertIs<KMagneticFluxDensityUnitInstance>(mu * (3 of amperesPerMeter))
        assertEquals(6 of teslas, mu * (3 of amperesPerMeter))
        assertEquals(6 of teslas, (3 of amperesPerMeter) * mu)
        assertIs<KMagneticFieldStrengthUnitInstance>((6 of teslas) / mu)
        assertEquals(3.0, (6 of teslas) / mu into amperesPerMeter, 1e-12)
    }

    /** Real-world example: in vacuum a field of 1000 A/m produces a flux density of 1.257 mT. */
    @Test
    fun `vacuum flux density real world`() {
        val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)
        assertEquals(1.25663706127e-3, b.value, 1e-15)
        assertEquals(1.25663706127, (1 of vacuumPermeability) into micro.henriesPerMeter, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a permeability. */
    @Test
    fun `invalid permeability decomposition fails`() {
        // wrong distance exponent (+2 instead of +1) - that is an inductance
        val wrongDistance = (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongDistance.toPermeability() }
        // wrong time exponent (-3 instead of -2)
        val wrongTime = (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toPermeability() }
        // wrong current exponent (-1 instead of -2)
        val wrongCurrent = (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongCurrent.toPermeability() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toPermeability() }
    }
}
