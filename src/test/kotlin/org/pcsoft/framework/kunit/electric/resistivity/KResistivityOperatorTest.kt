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

package org.pcsoft.framework.kunit.electric.resistivity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.*

/**
 * The resistivity operators: same-type arithmetic/comparison, the escape of `resistivity*resistivity`/
 * `resistivity/resistivity` to a mixed unit, both resistivity decompositions (`resistance·length` and the
 * canonical native `mass·length³·time⁻³·current⁻²` form via `toResistivity`), the inverse operators, and the
 * invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistivityOperatorTest {

    /** Same-type resistivity operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `resistivity same-type operators`() {
        val r1 = 100 of ohmMeters
        val r2 = 40 of ohmMeters
        assertEquals(140.0, (r1 + r2).value, 1e-9)
        assertEquals(60.0, (r1 - r2).value, 1e-9)
        assertTrue(r1 > r2)
        assertIs<KMixedUnitInstance>(r1 * r2)
        assertIs<KMixedUnitInstance>(r1 / r2)
    }

    /** Both resistivity decompositions agree: `R·l` and the native `kg·m³·s⁻³·A⁻²` expression. */
    @Test
    fun `both resistivity decompositions agree`() {
        // Decomposition A: resistance times the geometry factor
        val fromResistance = (5 of ohms) * (0.4 of meters)
        assertEquals(2 of ohmMeters, fromResistance)
        assertEquals(2 of ohmMeters, (0.4 of meters) * (5 of ohms))

        // Decomposition B: native canonical expression
        val native = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
        assertEquals(2 of ohmMeters, native.toResistivity())
        assertEquals(2.0, native.toResistivity().value, 1e-9)

        // Both funnel into resistivityInstanceOf and are value-equal
        assertEquals(fromResistance, native.toResistivity())
        assertEquals(fromResistance.value, native.toResistivity().value, 1e-9)
    }

    /** Inverse operators: `ρ/l = R`, `ρ/R = l`. */
    @Test
    fun `inverse resistivity operators`() {
        val rho = 2 of ohmMeters
        assertIs<KResistanceUnitInstance>(rho / (0.4 of meters))
        assertEquals(5 of ohms, rho / (0.4 of meters))
        assertIs<KLengthUnitInstance>(rho / (5 of ohms))
        assertEquals(0.4, rho / (5 of ohms) into meters, 1e-12)
    }

    /** Real-world example: 17 nΩ·m copper over a 1 mm geometry factor yields 17 µΩ. */
    @Test
    fun `copper wire resistance real world`() {
        val r = (17 of org.pcsoft.framework.kunit.nano.ohmMeters) / (1 of milli.meters)
        assertEquals(1.7e-5, r into ohms, 1e-15)
    }

    /** A non-canonical shape cannot be read back as a resistivity. */
    @Test
    fun `invalid resistivity decomposition fails`() {
        // wrong distance exponent (+2 instead of +3) - that is a resistance
        val wrongDistance = (kilo.grams * (meters pow 2)) / ((seconds pow 3) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongDistance.toResistivity() }
        // wrong time exponent (-2 instead of -3)
        val wrongTime = (kilo.grams * (meters pow 3)) / ((seconds pow 2) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toResistivity() }
        // wrong current exponent (-1 instead of -2)
        val wrongCurrent = (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongCurrent.toResistivity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toResistivity() }
    }
}
