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

package org.pcsoft.framework.kunit.electric.magneticfieldstrength

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The magnetic field strength operators: same-type arithmetic/comparison, the escape of `H*H`/`H/H` to a
 * mixed unit, both decompositions (the typed `current/length` form and the canonical native `A·m⁻¹`
 * expression via `toMagneticFieldStrength`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFieldStrengthOperatorTest {

    /** Same-type operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `magnetic field strength same-type operators`() {
        val h1 = 100 of amperesPerMeter
        val h2 = 40 of amperesPerMeter
        assertEquals(140.0, (h1 + h2).value, 1e-9)
        assertEquals(60.0, (h1 - h2).value, 1e-9)
        assertTrue(h1 > h2)
        assertIs<KMixedUnitInstance>(h1 * h2)
        assertIs<KMixedUnitInstance>(h1 / h2)
    }

    /** Both decompositions agree: the typed `I / l` form and the native `A·m⁻¹` expression. */
    @Test
    fun `both magnetic field strength decompositions agree`() {
        // Decomposition A: typed operands - a 500-turn coil of 0.25 m carrying 2 A (1000 ampere-turns)
        val fromOperator = (1000 of amperes) / (0.25 of meters)
        assertIs<KMagneticFieldStrengthUnitInstance>(fromOperator)
        assertEquals(4000 of amperesPerMeter, fromOperator)

        // Decomposition B: native canonical expression
        val native = 4000 of (amperes pow 1) / (meters pow 1)
        assertIs<KMagneticFieldStrengthUnitInstance>(native.toMagneticFieldStrength())
        assertEquals(4000 of amperesPerMeter, native.toMagneticFieldStrength())
        assertEquals(4000.0, native.toMagneticFieldStrength().value, 1e-9)

        // Both funnel into magneticFieldStrengthInstanceOf and are value-equal
        assertEquals(fromOperator, native.toMagneticFieldStrength())
        assertEquals(fromOperator.value, native.toMagneticFieldStrength().value, 1e-9)
    }

    /** Inverse operators: `H·l = I` and `l·H = I`. */
    @Test
    fun `inverse magnetic field strength operators`() {
        val h = 4000 of amperesPerMeter
        val l = 0.25 of meters
        assertEquals(1000 of amperes, h * l)
        assertEquals(1000 of amperes, l * h)
    }

    /** A non-canonical shape cannot be read back as a magnetic field strength. */
    @Test
    fun `invalid magnetic field strength decomposition fails`() {
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = (amperes pow 2) / (meters pow 1)
        assertFailsWith<IllegalStateException> { wrongCurrent.toMagneticFieldStrength() }
        // wrong distance exponent (-2 instead of -1)
        val wrongDistance = (amperes pow 1) / (meters pow 2)
        assertFailsWith<IllegalStateException> { wrongDistance.toMagneticFieldStrength() }
        // too many terms
        val tooMany = (amperes pow 1) / (meters pow 1) * (seconds pow 1)
        assertFailsWith<IllegalStateException> { tooMany.toMagneticFieldStrength() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of amperes).toUnit().toMagneticFieldStrength() }
    }
}
