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

package org.pcsoft.framework.kunit.capacitance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KCapacitanceUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KCapacitanceUnitSystemTest {

    private val tokens: List<Pair<KCapacitanceUnitInstance, Double>> = listOf(
        farads to KCapacitanceUnit.FARAD.baseValue,
        abfarads to KCapacitanceUnit.ABFARAD.baseValue,
        statfarads to KCapacitanceUnit.STATFARAD.baseValue,
        jars to KCapacitanceUnit.JAR.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to farads and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KCapacitanceUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized farad value (`1 mF == 1000 µF`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of milli.farads, 1000 of micro.farads)
        assertEquals((1 of milli.farads).hashCode(), (1000 of micro.farads).hashCode())
        assertFalse((1 of farads) == (2 of farads))
        assertFalse((1 of farads).equals(1.0)) // not a KCapacitanceUnitInstance
    }

    /** `toString` renders the normalized farad value. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.001 F", (1 of milli.farads).toString())
    }

    /** A mixed unit that is not a canonical capacitance normal form cannot be converted to a capacitance. */
    @Test
    fun `toCapacitance on non-capacitance fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toCapacitance() }
        assertFailsWith<IllegalStateException> { ((1 of farads).toUnit() * (1 of farads).toUnit()).toCapacitance() }
    }
}
