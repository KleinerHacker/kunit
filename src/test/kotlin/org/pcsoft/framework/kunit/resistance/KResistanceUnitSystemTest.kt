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

package org.pcsoft.framework.kunit.resistance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KResistanceUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistanceUnitSystemTest {

    private val tokens: List<Pair<KResistanceUnitInstance, Double>> = listOf(
        ohms to KResistanceUnit.OHM.baseValue,
        statohms to KResistanceUnit.STATOHM.baseValue,
        abohms to KResistanceUnit.ABOHM.baseValue,
        internationalOhms to KResistanceUnit.INTERNATIONAL_OHM.baseValue,
        legalOhms to KResistanceUnit.LEGAL_OHM.baseValue,
        siemensUnits to KResistanceUnit.SIEMENS_UNIT.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to ohms and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KResistanceUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized ohm value (`1 kΩ == 1000 Ω`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.ohms, 1000 of ohms)
        assertEquals((1 of kilo.ohms).hashCode(), (1000 of ohms).hashCode())
        assertFalse((1 of ohms) == (2 of ohms))
        assertFalse((1 of ohms).equals(1.0)) // not a KResistanceUnitInstance
    }

    /** `toString` renders the normalized ohm value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 Ω", (1 of kilo.ohms).toString())
    }

    /** A mixed unit that is not a canonical resistance normal form cannot be converted to a resistance. */
    @Test
    fun `toResistance on non-resistance fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toResistance() }
        assertFailsWith<IllegalStateException> { ((1 of ohms).toUnit() * (1 of ohms).toUnit()).toResistance() }
    }
}
