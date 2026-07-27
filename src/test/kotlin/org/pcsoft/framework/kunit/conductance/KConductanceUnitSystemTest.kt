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

package org.pcsoft.framework.kunit.conductance

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

/** `KConductanceUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KConductanceUnitSystemTest {

    private val tokens: List<Pair<KConductanceUnitInstance, Double>> = listOf(
        siemens to KConductanceUnit.SIEMENS.baseValue,
        mhos to KConductanceUnit.MHO.baseValue,
        abmhos to KConductanceUnit.ABMHO.baseValue,
        statmhos to KConductanceUnit.STATMHO.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to siemens and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KConductanceUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized siemens value (`1 kS == 1000 S`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.siemens, 1000 of siemens)
        assertEquals((1 of kilo.siemens).hashCode(), (1000 of siemens).hashCode())
        assertFalse((1 of siemens) == (2 of siemens))
        assertFalse((1 of siemens).equals(1.0)) // not a KConductanceUnitInstance
    }

    /** `toString` renders the normalized siemens value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 S", (1 of kilo.siemens).toString())
    }

    /** A mixed unit that is not a canonical conductance normal form cannot be converted to a conductance. */
    @Test
    fun `toConductance on non-conductance fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toConductance() }
        assertFailsWith<IllegalStateException> { ((1 of siemens).toUnit() * (1 of siemens).toUnit()).toConductance() }
    }
}
