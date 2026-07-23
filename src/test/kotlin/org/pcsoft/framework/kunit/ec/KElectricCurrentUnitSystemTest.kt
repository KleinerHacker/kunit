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

package org.pcsoft.framework.kunit.ec

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KElectricCurrentUnitInstance` surface: `of`/`into` construction and round-trip, equality, `pow`, conversions. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricCurrentUnitSystemTest {

    private val tokens: List<Pair<KElectricCurrentUnitInstance, Double>> = listOf(
        amperes to KElectricCurrentUnit.AMPERE.baseValue,
        biot to KElectricCurrentUnit.BIOT.baseValue,
        statamperes to KElectricCurrentUnit.STATAMPERE.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to amperes and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KElectricCurrentUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Reading a current in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of amperes) into ((1 of amperes).toUnit() * (1 of amperes).toUnit()) }
    }

    /** Equality/hash by normalized ampere value (`1 Bi == 10 A`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of biot, 10 of amperes)
        assertEquals((1 of biot).hashCode(), (10 of amperes).hashCode())
        assertFalse((1 of amperes) == (2 of amperes))
        assertFalse((1 of amperes).equals(1.0)) // not a KElectricCurrentUnitInstance
    }

    /** `toString` renders the normalized ampere value. */
    @Test
    fun `toString base unit`() {
        assertEquals("10.0 A", (1 of biot).toString())
    }

    /** `pow` on a current value yields a generic mixed unit (no dimensioned power type). */
    @Test
    fun `pow yields mixed`() {
        assertEquals(4.0, ((2 of amperes) pow 2).value, 1e-9)
    }

    /** A single current term (with any exponent) converts back to a pure current value. */
    @Test
    fun `toElectricCurrent round-trip`() {
        assertEquals(10.0, (1 of biot).toUnit().toElectricCurrent().value, 1e-9)
        val squared = (2 of amperes).toUnit() pow 2 // [AMPERE^2], value 4
        assertEquals(4.0, squared.toElectricCurrent().value, 1e-9)
    }

    /** A mixed unit that is not a single current term cannot be converted to a current value. */
    @Test
    fun `toElectricCurrent on non-current fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toElectricCurrent() }
        assertFailsWith<IllegalStateException> { ((1 of amperes).toUnit() / (1 of amperes).toUnit()).toElectricCurrent() }
    }

    /** `format` into kiloamperes and amperes. */
    @Test
    fun `format compositions`() {
        assertEquals("2.0 kA", (2000 of amperes) format kilo.amperes)
        assertEquals("10.0 A", (1 of biot) format amperes)
    }
}
