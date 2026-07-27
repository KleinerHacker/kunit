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

package org.pcsoft.framework.kunit.conductivity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.conductance.KConductanceUnitInstance
import org.pcsoft.framework.kunit.conductance.siemens
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.resistivity.KResistivityUnitInstance
import org.pcsoft.framework.kunit.resistivity.ohmMeters
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The conductivity operators: same-type arithmetic/comparison, the escape of `conductivity*conductivity`/
 * `conductivity/conductivity` to a mixed unit, all conductivity decompositions (the reciprocal `1/resistivity`,
 * the `conductance/length` form and the canonical native `mass⁻¹·length⁻³·time³·current²` form via
 * `toConductivity`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KConductivityOperatorTest {

    /** Same-type conductivity operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `conductivity same-type operators`() {
        val s1 = 100 of siemensPerMeter
        val s2 = 40 of siemensPerMeter
        assertEquals(140.0, (s1 + s2).value, 1e-9)
        assertEquals(60.0, (s1 - s2).value, 1e-9)
        assertTrue(s1 > s2)
        assertIs<KMixedUnitInstance>(s1 * s2)
        assertIs<KMixedUnitInstance>(s1 / s2)
    }

    /** All conductivity decompositions agree: `1/ρ`, `G/l` and the native `kg⁻¹·m⁻³·s³·A²` expression. */
    @Test
    fun `all conductivity decompositions agree`() {
        // Decomposition A: the reciprocal of a resistivity (1 / 0.5 Ω·m = 2 S/m)
        val fromResistivity = 1 / (0.5 of ohmMeters)
        assertEquals(2 of siemensPerMeter, fromResistivity)

        // Decomposition B: conductance over the geometry factor (10 S / 5 m = 2 S/m)
        val fromConductance = (10 of siemens) / (5 of meters)
        assertEquals(2 of siemensPerMeter, fromConductance)

        // Decomposition C: native canonical expression
        val native = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
        assertEquals(2 of siemensPerMeter, native.toConductivity())
        assertEquals(2.0, native.toConductivity().value, 1e-9)

        // All funnel into conductivityInstanceOf and are value-equal
        assertEquals(fromResistivity, fromConductance)
        assertEquals(fromResistivity, native.toConductivity())
        assertEquals(fromResistivity.value, native.toConductivity().value, 1e-9)
    }

    /** The reciprocal pair is symmetric: `1/σ = ρ` mirrors `1/ρ = σ`. */
    @Test
    fun `reciprocal pair is symmetric`() {
        val rho: KResistivityUnitInstance = 1 / (2 of siemensPerMeter)
        assertIs<KResistivityUnitInstance>(rho)
        assertEquals(0.5, rho into ohmMeters, 1e-12)
        assertEquals(2 of siemensPerMeter, 1 / rho)
    }

    /** Inverse operators: `σ·l = G`, `l·σ = G`, `G/σ = l`. */
    @Test
    fun `inverse conductivity operators`() {
        val sigma = 2 of siemensPerMeter
        val g1 = sigma * (5 of meters)
        val g2 = (5 of meters) * sigma
        assertIs<KConductanceUnitInstance>(g1)
        assertEquals(10 of siemens, g1)
        assertEquals(10 of siemens, g2)
        val l: KLengthUnitInstance = (10 of siemens) / sigma
        assertIs<KLengthUnitInstance>(l)
        assertEquals(5.0, l into meters, 1e-12)
    }

    /** Real-world example: copper's 17 nΩ·m resistivity is a conductivity of about 58.8 MS/m. */
    @Test
    fun `copper conductivity real world`() {
        val sigma = 1 / (17 of nano.ohmMeters)
        assertEquals(58.82352941176471, sigma into mega.siemensPerMeter, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a conductivity. */
    @Test
    fun `invalid conductivity decomposition fails`() {
        // wrong distance exponent (-2 instead of -3) - that is a conductance
        val wrongDistance = ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongDistance.toConductivity() }
        // wrong time exponent (+4 instead of +3)
        val wrongTime = ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 3))
        assertFailsWith<IllegalStateException> { wrongTime.toConductivity() }
        // wrong current exponent (+1 instead of +2)
        val wrongCurrent = ((amperes pow 1) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
        assertFailsWith<IllegalStateException> { wrongCurrent.toConductivity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toConductivity() }
    }
}
