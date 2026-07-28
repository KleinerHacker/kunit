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

package org.pcsoft.framework.kunit.electric.inductance

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The inductance operators: same-type arithmetic/comparison, the escape of `inductance*inductance`/
 * `inductance/inductance` to a mixed unit, all inductance decompositions (`flux / current`, the reactance
 * form `resistance / frequency` and the canonical native `mass·length²·time⁻²·current⁻²` form via
 * `toInductance`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KInductanceOperatorTest {

    /** Same-type inductance operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `inductance same-type operators`() {
        val l1 = 100 of henries
        val l2 = 40 of henries
        assertEquals(140.0, (l1 + l2).value, 1e-9)
        assertEquals(60.0, (l1 - l2).value, 1e-9)
        assertTrue(l1 > l2)
        assertIs<KMixedUnitInstance>(l1 * l2)
        assertIs<KMixedUnitInstance>(l1 / l2)
    }

    /** All inductance decompositions agree: `Φ/I`, `X/ω` and the native `kg·m²·s⁻²·A⁻²` expression. */
    @Test
    fun `all inductance decompositions agree`() {
        // Decomposition A: definition of inductance
        val fromFluxCurrent = (4 of webers) / (2 of amperes)
        assertEquals(2 of henries, fromFluxCurrent)

        // Decomposition B: reactance form (Ω/Hz = Ω·s = H)
        val fromReactance = (4 of ohms) / (2 of hertz)
        assertEquals(2 of henries, fromReactance)

        // Decomposition C: native canonical expression
        val native = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
        assertEquals(2 of henries, native.toInductance())
        assertEquals(2.0, native.toInductance().value, 1e-9)

        // All funnel into inductanceInstanceOf and are value-equal
        assertEquals(fromFluxCurrent, fromReactance)
        assertEquals(fromFluxCurrent, native.toInductance())
        assertEquals(fromFluxCurrent.value, fromReactance.value, 1e-9)
        assertEquals(fromFluxCurrent.value, native.toInductance().value, 1e-9)
    }

    /** Inverse operators: `L·I = Φ`, `I·L = Φ`, `Φ/L = I`, `L·ω = X`. */
    @Test
    fun `inverse inductance operators`() {
        val l = 2 of henries
        val i = 2 of amperes
        assertIs<KMagneticFluxUnitInstance>(l * i)
        assertEquals(4 of webers, l * i)
        assertEquals(4 of webers, i * l)
        assertIs<KElectricCurrentUnitInstance>((4 of webers) / l)
        assertEquals(2 of amperes, (4 of webers) / l)
        assertIs<KResistanceUnitInstance>(l * (2 of hertz))
        assertEquals(4 of ohms, l * (2 of hertz))
    }

    /** A non-canonical shape cannot be read back as an inductance. */
    @Test
    fun `invalid inductance decomposition fails`() {
        // wrong current exponent (-1 instead of -2)
        val wrongCurrent = (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toInductance() }
        // wrong distance exponent (+1 instead of +2)
        val wrongDistance = (kilo.grams * (meters pow 1)) / ((amperes pow 2) * (seconds pow 2))
        assertFailsWith<IllegalStateException> { wrongDistance.toInductance() }
        // wrong time exponent (-3 instead of -2)
        val wrongTime = (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
        assertFailsWith<IllegalStateException> { wrongTime.toInductance() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toInductance() }
    }

    /** Real-world example: a 470 µH choke at an angular frequency of 100 kHz has a reactance of 47 Ω. */
    @Test
    fun `choke reactance`() {
        val l = 470 of micro.henries
        val x = l * (100_000 of hertz)
        assertEquals(47.0, x.value, 1e-9)
    }
}
