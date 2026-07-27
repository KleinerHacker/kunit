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

package org.pcsoft.framework.kunit.magneticflux

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

/** `KMagneticFluxUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxUnitSystemTest {

    private val tokens: List<Pair<KMagneticFluxUnitInstance, Double>> = listOf(
        webers to KMagneticFluxUnit.WEBER.baseValue,
        maxwells to KMagneticFluxUnit.MAXWELL.baseValue,
        unitPoles to KMagneticFluxUnit.UNIT_POLE.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to webers and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KMagneticFluxUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized weber value (`1 Wb == 1e8 Mx`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of webers, 1e8 of maxwells)
        assertEquals((1 of webers).hashCode(), (1e8 of maxwells).hashCode())
        assertFalse((1 of webers) == (2 of webers))
        assertFalse((1 of webers).equals(1.0)) // not a KMagneticFluxUnitInstance
    }

    /** `toString` renders the normalized weber value. */
    @Test
    fun `toString base unit`() {
        assertEquals("0.001 Wb", (1 of milli.webers).toString())
    }

    /** A mixed unit that is not a canonical magnetic flux normal form cannot be converted to a flux. */
    @Test
    fun `toMagneticFlux on non-flux fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toMagneticFlux() }
        assertFailsWith<IllegalStateException> { ((1 of webers).toUnit() * (1 of webers).toUnit()).toMagneticFlux() }
    }
}
