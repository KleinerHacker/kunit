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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/**
 * `KMagneticFieldStrengthUnitInstance` surface: `of`/`into` construction and round-trip, equality,
 * `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFieldStrengthUnitSystemTest {

    private val tokens: List<Pair<KMagneticFieldStrengthUnitInstance, Double>> = listOf(
        amperesPerMeter to KMagneticFieldStrengthUnit.AMPERE_PER_METER.baseValue,
        oersteds to KMagneticFieldStrengthUnit.OERSTED.baseValue,
        gilbertsPerCentimeter to KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER.baseValue,
        ampereTurnsPerInch to KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to A/m and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KMagneticFieldStrengthUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized A/m value (`1 kA/m == 1000 A/m`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.amperesPerMeter, 1000 of amperesPerMeter)
        assertEquals((1 of kilo.amperesPerMeter).hashCode(), (1000 of amperesPerMeter).hashCode())
        assertFalse((1 of amperesPerMeter) == (2 of amperesPerMeter))
        assertFalse((1 of amperesPerMeter).equals(1.0)) // not a KMagneticFieldStrengthUnitInstance
    }

    /** `toString` renders the normalized A/m value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 A/m", (1 of kilo.amperesPerMeter).toString())
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a field strength. */
    @Test
    fun `toMagneticFieldStrength on non-field-strength fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toMagneticFieldStrength() }
        assertFailsWith<IllegalStateException> {
            ((1 of amperesPerMeter).toUnit() * (1 of amperesPerMeter).toUnit()).toMagneticFieldStrength()
        }
    }
}
