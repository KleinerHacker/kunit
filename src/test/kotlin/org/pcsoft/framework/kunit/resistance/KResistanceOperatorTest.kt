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

package org.pcsoft.framework.kunit.resistance

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.voltage.volts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The resistance operators: same-type arithmetic/comparison, the escape of `resistance*resistance`/
 * `resistance/resistance` to a mixed unit, both resistance decompositions (Ohm's law `voltage/current` and
 * the canonical native `mass·length²·time⁻³·current⁻²` form via `toResistance`), the inverse Ohm's-law
 * operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistanceOperatorTest {

    /** Same-type resistance operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `resistance same-type operators`() {
        val r1 = 100 of ohms
        val r2 = 40 of ohms
        assertEquals(140.0, (r1 + r2).value, 1e-9)
        assertEquals(60.0, (r1 - r2).value, 1e-9)
        assertTrue(r1 > r2)
        assertIs<KMixedUnitInstance>(r1 * r2)
        assertIs<KMixedUnitInstance>(r1 / r2)
    }

    /** Both resistance decompositions agree: Ohm's law `U/I` and the native `kg·m²·s⁻³·A⁻²` expression. */
    @Test
    fun `both resistance decompositions agree`() {
        // Decomposition A: Ohm's law solved for resistance
        val fromOhmsLaw = (230 of volts) / (2 of amperes)
        assertEquals(115 of ohms, fromOhmsLaw)

        // Decomposition B: native canonical expression
        val native = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
        assertEquals(115 of ohms, native.toResistance())
        assertEquals(115.0, native.toResistance().value, 1e-9)

        // Both funnel into resistanceInstanceOf and are value-equal
        assertEquals(fromOhmsLaw, native.toResistance())
        assertEquals(fromOhmsLaw.value, native.toResistance().value, 1e-9)
    }

    /** Inverse Ohm's-law operators: `R·I = U`, `I·R = U`, `U/R = I`. */
    @Test
    fun `inverse ohms law operators`() {
        val r = 115 of ohms
        val i = 2 of amperes
        assertEquals(230 of volts, r * i)
        assertEquals(230 of volts, i * r)
        assertEquals(2 of amperes, (230 of volts) / r)
    }

    /** A non-canonical shape cannot be read back as a resistance. */
    @Test
    fun `invalid resistance decomposition fails`() {
        // wrong current exponent (-1 instead of -2)
        val wrongCurrent = (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
        assertFailsWith<IllegalStateException> { wrongCurrent.toResistance() }
        // wrong distance exponent (+1 instead of +2)
        val wrongDistance = (kilo.grams * (meters pow 1)) / ((amperes pow 2) * (seconds pow 3))
        assertFailsWith<IllegalStateException> { wrongDistance.toResistance() }
        // wrong time exponent (-2 instead of -3)
        val wrongTime = (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toResistance() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toResistance() }
    }
}
