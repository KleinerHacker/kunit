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

package org.pcsoft.framework.kunit.charge

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.frequency.hertz
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The charge operators: same-type arithmetic/comparison, the escape of `charge*charge`/`charge/charge` to a
 * mixed unit, all charge decompositions (`current · time`, `time · current`, `current / frequency` and the
 * canonical native `current·time` form via `toCharge`), the inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KChargeOperatorTest {

    /** Same-type charge operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `charge same-type operators`() {
        val q1 = 100 of coulombs
        val q2 = 40 of coulombs
        assertEquals(140.0, (q1 + q2).value, 1e-9)
        assertEquals(60.0, (q1 - q2).value, 1e-9)
        assertTrue(q1 > q2)
        assertIs<KMixedUnitInstance>(q1 * q2)
        assertIs<KMixedUnitInstance>(q1 / q2)
    }

    /** All charge decompositions agree: `I·t`, `t·I`, `I/f` and the native `A·s` expression. */
    @Test
    fun `all charge decompositions agree`() {
        // Decomposition A: definition of charge
        val fromCurrentTime = (2 of amperes) * (3600 of seconds)
        assertEquals(2 of ampereHours, fromCurrentTime)

        // Decomposition A (commutative)
        val fromTimeCurrent = (3600 of seconds) * (2 of amperes)
        assertEquals(2 of ampereHours, fromTimeCurrent)

        // Decomposition B: inverse-time form (1/Hz = s)
        val fromFrequency = (2 of amperes) / ((1.0 / 3600.0) of hertz)
        assertEquals(7200.0, fromFrequency.value, 1e-6)

        // Decomposition C: native canonical expression (mixed units are normalized)
        val native = 2 of (amperes * hours)
        assertEquals(7200.0, native.value, 1e-9)
        val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()
        assertEquals(2 of ampereHours, raw.toCharge())
        assertEquals(7200.0, raw.toCharge().value, 1e-9)

        // All funnel into chargeInstanceOf and are value-equal
        assertEquals(fromCurrentTime, fromTimeCurrent)
        assertEquals(fromCurrentTime, raw.toCharge())
        assertEquals(fromCurrentTime.value, fromTimeCurrent.value, 1e-9)
        assertEquals(fromCurrentTime.value, fromFrequency.value, 1e-6)
    }

    /** `toCharge` is order independent (time term first). */
    @Test
    fun `toCharge is order independent`() {
        val raw = (1 of hours).toUnit() * (2 of amperes).toUnit()
        assertEquals(2 of ampereHours, raw.toCharge())
    }

    /** Inverse operators: `Q/t = I`, `Q/I = t`, `Q·f = I`. */
    @Test
    fun `inverse charge operators`() {
        val q = 2 of ampereHours // 7200 C
        assertIs<KElectricCurrentUnitInstance>(q / (3600 of seconds))
        assertEquals(2.0, (q / (3600 of seconds)).value, 1e-9)
        assertIs<KTimeUnitInstance>(q / (2 of amperes))
        assertEquals(3600.0, (q / (2 of amperes)).value, 1e-9)
        assertIs<KElectricCurrentUnitInstance>(q * ((1.0 / 3600.0) of hertz))
        assertEquals(2.0, (q * ((1.0 / 3600.0) of hertz)).value, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a charge. */
    @Test
    fun `invalid charge decomposition fails`() {
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = (amperes pow 2) * (seconds pow 1)
        assertFailsWith<IllegalStateException> { wrongCurrent.toCharge() }
        // wrong time exponent (+2 instead of +1)
        val wrongTime = amperes * (seconds pow 2)
        assertFailsWith<IllegalStateException> { wrongTime.toCharge() }
        // too few terms
        assertFailsWith<IllegalStateException> { (2 of amperes).toUnit().toCharge() }
    }
}
