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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/**
 * `KMagneticFluxDensityUnitInstance` surface: `of`/`into` construction and round-trip, equality,
 * `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxDensityUnitSystemTest {

    private val tokens: List<Pair<KMagneticFluxDensityUnitInstance, Double>> = listOf(
        teslas to KMagneticFluxDensityUnit.TESLA.baseValue,
        webersPerSquareMeter to KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER.baseValue,
        gauss to KMagneticFluxDensityUnit.GAUSS.baseValue,
        gammas to KMagneticFluxDensityUnit.GAMMA.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to teslas and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KMagneticFluxDensityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized tesla value (`1 T == 1 Wb/m²`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of teslas, 1 of webersPerSquareMeter)
        assertEquals((1 of teslas).hashCode(), (1 of webersPerSquareMeter).hashCode())
        assertEquals(1.0, (10000 of gauss).value, 1e-12)
        assertEquals(1.0, (1e9 of gammas).value, 1e-12)
        assertFalse((1 of teslas) == (2 of teslas))
        assertFalse((1 of teslas).equals(1.0)) // not a KMagneticFluxDensityUnitInstance
    }

    /** Reading one unit against another (`1 T == 10000 G == 1e9 γ`). */
    @Test
    fun `cross unit reading`() {
        assertEquals(10000.0, (1 of teslas) into gauss, 1e-3)
        assertEquals(1e9, (1 of teslas) into gammas, 1e3)
        assertEquals(1.0, (1 of teslas) into webersPerSquareMeter, 1e-12)
    }

    /** `toString` renders the normalized tesla value. */
    @Test
    fun `toString base unit`() {
        assertEquals("3.0 T", (3 of teslas).toString())
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a flux density. */
    @Test
    fun `toMagneticFluxDensity on non-flux-density fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toMagneticFluxDensity() }
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toMagneticFluxDensity() }
        // wrong time exponent (-3 instead of -2)
        assertFailsWith<IllegalStateException> {
            (kilo.grams / ((seconds pow 3) * (amperes pow 1))).toMagneticFluxDensity()
        }
        // wrong current exponent (-2 instead of -1)
        assertFailsWith<IllegalStateException> {
            (kilo.grams / ((seconds pow 2) * (amperes pow 2))).toMagneticFluxDensity()
        }
    }
}
