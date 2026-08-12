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

package org.pcsoft.framework.kunit.electric.conductance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.*

/**
 * The conductance operators: same-type arithmetic/comparison, the escape of `conductance*conductance`/
 * `conductance/conductance` to a mixed unit, both conductance decompositions (`current/voltage` and the
 * canonical native `mass⁻¹·length⁻²·time³·current²` form via `toConductance`), the reciprocal relation to
 * resistance, the inverse Ohm's-law operators, and the invalid-shape guard.
 */
class KConductanceOperatorTest {

    /** Same-type conductance operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `conductance same-type operators`() {
        val g1 = 100 of siemens
        val g2 = 40 of siemens
        assertEquals(140.0, (g1 + g2).value, 1e-9)
        assertEquals(60.0, (g1 - g2).value, 1e-9)
        assertTrue(g1 > g2)
        assertIs<KMixedUnitInstance>(g1 * g2)
        assertIs<KMixedUnitInstance>(g1 / g2)
    }

    /** Both conductance decompositions agree: `I/U` and the native `kg⁻¹·m⁻²·s³·A²` expression. */
    @Test
    fun `both conductance decompositions agree`() {
        // Decomposition A: Ohm's law solved for conductance
        val fromOhmsLaw = (2 of amperes) / (1 of volts)
        assertEquals(2 of siemens, fromOhmsLaw)

        // Decomposition B: native canonical expression
        val native = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
        assertEquals(2 of siemens, native.toConductance())
        assertEquals(2.0, native.toConductance().value, 1e-9)

        // Both funnel into conductanceInstanceOf and are value-equal
        assertEquals(fromOhmsLaw, native.toConductance())
        assertEquals(fromOhmsLaw.value, native.toConductance().value, 1e-9)
    }

    /** The gram/kilogram mass bridge: 1 S built natively from gram-based terms stays 1 S. */
    @Test
    fun `mass reference bridge`() {
        val native = 1 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
        assertEquals(1.0, native.toConductance().value, 1e-9)
        // the same expression stated in grams is 1000x smaller
        val inGrams = 1 of ((seconds pow 3) * (amperes pow 2)) / (grams * (meters pow 2))
        assertEquals(1000.0, inGrams.toConductance().value, 1e-6)
    }

    /** Conductance and resistance are reciprocal: `G = 1/R` and `R = 1/G`. */
    @Test
    fun `reciprocal of resistance`() {
        val g = 1 / (1 of ohms)
        assertIs<KConductanceUnitInstance>(g)
        assertEquals(1 of siemens, g)

        val r = 1 / (1 of siemens)
        assertIs<KResistanceUnitInstance>(r)
        assertEquals(1 of ohms, r)

        // round trip: 1 / (1 / R) == R
        assertEquals(4 of ohms, 1 / (1 / (4 of ohms)))
        assertEquals(0.25, (1 / (4 of ohms)).value, 1e-9)
    }

    /** Inverse Ohm's-law operators on conductance: `G·U = I`, `U·G = I`, `I/G = U`. */
    @Test
    fun `inverse ohms law operators`() {
        val g = 2 of siemens
        val u = 115 of volts
        assertEquals(230 of amperes, g * u)
        assertEquals(230 of amperes, u * g)
        assertEquals(115 of volts, (230 of amperes) / g)
    }

    /** A non-canonical shape cannot be read back as a conductance. */
    @Test
    fun `invalid conductance decomposition fails`() {
        // wrong current exponent (+1 instead of +2)
        val wrongCurrent = (amperes * (seconds pow 3)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toConductance() }
        // wrong distance exponent (-1 instead of -2)
        val wrongDistance = ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 1))
        assertFailsWith<IllegalStateException> { wrongDistance.toConductance() }
        // wrong time exponent (+2 instead of +3)
        val wrongTime = ((amperes pow 2) * (seconds pow 2)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toConductance() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toConductance() }
    }
}
