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

package org.pcsoft.framework.kunit.frequency

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KFrequencyUnitInstance` surface: `of`/`into` construction and round-trip, equality, `pow`, conversions. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KFrequencyUnitSystemTest {

    private val tokens: List<Pair<KFrequencyUnitInstance, Double>> = listOf(
        hertz to KFrequencyUnit.HERTZ.baseValue,
        rps to KFrequencyUnit.RPS.baseValue,
        rpm to KFrequencyUnit.RPM.baseValue,
        bpm to KFrequencyUnit.BPM.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to hertz and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KFrequencyUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Reading frequency in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of hertz) into ((1 of hertz).toUnit() * (1 of hertz).toUnit()) }
    }

    /** Equality/hash by normalized hertz value (`1 kHz == 1000 Hz`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.hertz, 1000 of hertz)
        assertEquals((1 of kilo.hertz).hashCode(), (1000 of hertz).hashCode())
        assertFalse((1 of hertz) == (2 of hertz))
        assertFalse((1 of hertz).equals(1.0)) // not a KFrequencyUnitInstance
    }

    /** `toString` renders the normalized hertz value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 Hz", (1 of kilo.hertz).toString())
    }

    /** `pow` on a frequency value yields a generic mixed unit (no dimensioned power type). */
    @Test
    fun `pow yields mixed`() {
        assertEquals(4.0, ((2 of hertz) pow 2).value, 1e-9)
    }

    /** A single frequency term (with any exponent) converts back to a pure frequency value. */
    @Test
    fun `toFrequency round-trip`() {
        assertEquals(1000.0, (1 of kilo.hertz).toUnit().toFrequency().value, 1e-9)
        val squared = (2 of hertz).toUnit() pow 2 // [HERTZ^2], value 4
        assertEquals(4.0, squared.toFrequency().value, 1e-9)
    }

    /** A mixed unit that is not a single frequency term cannot be converted to a frequency value. */
    @Test
    fun `toFrequency on non-frequency fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toFrequency() }
        assertFailsWith<IllegalStateException> { ((1 of hertz).toUnit() / (1 of hertz).toUnit()).toFrequency() }
    }

    /** `format` into kilohertz. */
    @Test
    fun `format compositions`() {
        assertEquals("1.0 kHz", (1000 of hertz) format kilo.hertz)
    }
}
