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

package org.pcsoft.framework.kunit.inductance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KInductanceUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KInductanceUnitSystemTest {

    private val tokens: List<Pair<KInductanceUnitInstance, Double>> = listOf(
        henries to KInductanceUnit.HENRY.baseValue,
        webersPerAmpere to KInductanceUnit.WEBER_PER_AMPERE.baseValue,
        abhenries to KInductanceUnit.ABHENRY.baseValue,
        stathenries to KInductanceUnit.STATHENRY.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to henries and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KInductanceUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized henry value (`1 H == 1000 mH`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of henries, 1000 of milli.henries)
        assertEquals((1 of henries).hashCode(), (1000 of milli.henries).hashCode())
        assertEquals(1 of henries, 1 of webersPerAmpere)
        assertFalse((1 of henries) == (2 of henries))
        assertFalse((1 of henries).equals(1.0)) // not a KInductanceUnitInstance
    }

    /** `toString` renders the normalized henry value. */
    @Test
    fun `toString base unit`() {
        assertEquals("2.0 H", (2 of henries).toString())
        assertEquals("1.0 H", (1000 of milli.henries).toString())
    }

    /** A mixed unit that is not a canonical inductance normal form cannot be converted to an inductance. */
    @Test
    fun `toInductance on non-inductance fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toInductance() }
        assertFailsWith<IllegalStateException> { ((1 of henries).toUnit() * (1 of henries).toUnit()).toInductance() }
    }
}
