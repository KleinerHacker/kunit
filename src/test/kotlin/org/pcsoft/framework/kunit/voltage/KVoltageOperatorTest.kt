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

package org.pcsoft.framework.kunit.voltage

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The voltage operators: same-type arithmetic/comparison, the escape of `voltage*voltage`/`voltage/voltage`
 * to a mixed unit, the canonical `mass·length²·time⁻³·current⁻¹` decomposition via `toVoltage`, and the
 * invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KVoltageOperatorTest {

    /** The canonical native expression `kg·m²·s⁻³·A⁻¹` decomposes to a typed voltage in volts. */
    @Test
    fun `native expression is voltage`() {
        val u = 10 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
        assertEquals(10 of volts, u.toVoltage())
        assertEquals(10.0, u.toVoltage().value, 1e-9)
    }

    /** Same-type voltage operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `voltage same-type operators`() {
        val v1 = 100 of volts
        val v2 = 40 of volts
        assertEquals(140.0, (v1 + v2).value, 1e-9)
        assertEquals(60.0, (v1 - v2).value, 1e-9)
        assertTrue(v1 > v2)
        assertIs<KMixedUnitInstance>(v1 * v2)
        assertIs<KMixedUnitInstance>(v1 / v2)
    }

    /** A non-canonical shape cannot be read back as a voltage. */
    @Test
    fun `invalid voltage decomposition fails`() {
        // wrong distance exponent (+1 instead of +2)
        val wrongDistance = (kilo.grams * (meters pow 1)) / (amperes * (seconds pow 3))
        assertFailsWith<IllegalStateException> { wrongDistance.toVoltage() }
        // wrong time exponent (-2 instead of -3)
        val wrongTime = (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toVoltage() }
        // wrong current exponent (-2 instead of -1)
        val wrongCurrent = (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
        assertFailsWith<IllegalStateException> { wrongCurrent.toVoltage() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toVoltage() }
    }
}
