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

package org.pcsoft.framework.kunit.electric.magneticflux

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.volts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The magnetic flux operators: same-type arithmetic/comparison, the escape of `flux*flux`/`flux/flux` to a
 * mixed unit, both flux decompositions (the induction law `voltage·time` and the canonical native
 * `mass·length²·time⁻²·current⁻¹` form via `toMagneticFlux`), the inverse operators, and the invalid-shape
 * guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxOperatorTest {

    /** Same-type flux operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `magnetic flux same-type operators`() {
        val p1 = 100 of webers
        val p2 = 40 of webers
        assertEquals(140.0, (p1 + p2).value, 1e-9)
        assertEquals(60.0, (p1 - p2).value, 1e-9)
        assertTrue(p1 > p2)
        assertIs<KMixedUnitInstance>(p1 * p2)
        assertIs<KMixedUnitInstance>(p1 / p2)
    }

    /** All flux decompositions agree: `U·t`, `t·U`, `U/f` and the native `kg·m²·s⁻²·A⁻¹` expression. */
    @Test
    fun `all magnetic flux decompositions agree`() {
        // Decomposition A: the induction law (V·s = Wb)
        val fromInduction = (10 of volts) * (0.2 of seconds)
        assertEquals(2 of webers, fromInduction)
        assertEquals(2 of webers, (0.2 of seconds) * (10 of volts))

        // Decomposition B: the inverse-time form (V/Hz = V·s)
        val fromFrequency = (10 of volts) / (5 of hertz)
        assertEquals(2 of webers, fromFrequency)

        // Decomposition C: native canonical expression
        val native = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
        assertEquals(2 of webers, native.toMagneticFlux())
        assertEquals(2.0, native.toMagneticFlux().value, 1e-9)

        // All funnel into magneticFluxInstanceOf and are value-equal
        assertEquals(fromInduction, fromFrequency)
        assertEquals(fromInduction, native.toMagneticFlux())
        assertEquals(fromInduction.value, native.toMagneticFlux().value, 1e-9)
    }

    /** Inverse operators: `Φ/t = U`, `Φ·f = U`, `Φ/U = t`. */
    @Test
    fun `inverse magnetic flux operators`() {
        val phi = 2 of webers
        assertIs<KVoltageUnitInstance>(phi / (0.2 of seconds))
        assertEquals(10 of volts, phi / (0.2 of seconds))
        assertEquals(10 of volts, phi * (5 of hertz))
        assertIs<KTimeUnitInstance>(phi / (10 of volts))
        assertEquals(0.2, phi / (10 of volts) into seconds, 1e-12)
    }

    /** Real-world example: a coil whose 20 mWb core flux collapses in 4 ms induces 5 V. */
    @Test
    fun `induced voltage real world`() {
        val u = (20 of milli.webers) / (4 of milli.seconds)
        assertEquals(5.0, u into volts, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a magnetic flux. */
    @Test
    fun `invalid magnetic flux decomposition fails`() {
        // wrong current exponent (-2 instead of -1)
        val wrongCurrent = (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toMagneticFlux() }
        // wrong time exponent (-3 instead of -2)
        val wrongTime = (kilo.grams * (meters pow 2)) / ((seconds pow 3) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongTime.toMagneticFlux() }
        // wrong distance exponent (+1 instead of +2)
        val wrongDistance = (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongDistance.toMagneticFlux() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toMagneticFlux() }
    }
}
