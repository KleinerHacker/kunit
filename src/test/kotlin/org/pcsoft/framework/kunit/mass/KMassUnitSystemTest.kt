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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
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

/** `KMassUnitInstance` surface: `of`/`into` construction and round-trip, equality, `pow`, conversions. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMassUnitSystemTest {

    private val tokens: List<Pair<KMassUnitInstance, Double>> = listOf(
        grams to KMassUnit.GRAM.baseValue,
        tonnes to KMassUnit.TONNE.baseValue,
        pounds to KMassUnit.POUND.baseValue,
        daltons to KMassUnit.DALTON.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to grams and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KMassUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Reading mass in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of grams) into ((1 of grams).toUnit() * (1 of grams).toUnit()) }
    }

    /** Equality/hash by normalized gram value (`1 kg == 1000 g`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.grams, 1000 of grams)
        assertEquals((1 of kilo.grams).hashCode(), (1000 of grams).hashCode())
        assertFalse((1 of grams) == (2 of grams))
        assertFalse((1 of grams).equals(1.0)) // not a KMassUnitInstance
    }

    /** `toString` renders the normalized gram value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 g", (1 of kilo.grams).toString())
    }

    /** `pow` on a mass value yields a generic mixed unit (no dimensioned power type). */
    @Test
    fun `pow yields mixed`() {
        assertEquals(4.0, ((2 of grams) pow 2).value, 1e-9)
    }

    /** A single mass term (with any exponent) converts back to a pure mass value. */
    @Test
    fun `toMass round-trip`() {
        assertEquals(1000.0, (1 of kilo.grams).toUnit().toMass().value, 1e-9)
        val squared = (2 of grams).toUnit() pow 2 // [GRAM^2], value 4
        assertEquals(4.0, squared.toMass().value, 1e-9)
    }

    /** A mixed unit that is not a single mass term cannot be converted to a mass value. */
    @Test
    fun `toMass on non-mass fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toMass() }
        assertFailsWith<IllegalStateException> { ((1 of grams).toUnit() / (1 of grams).toUnit()).toMass() }
    }
}
