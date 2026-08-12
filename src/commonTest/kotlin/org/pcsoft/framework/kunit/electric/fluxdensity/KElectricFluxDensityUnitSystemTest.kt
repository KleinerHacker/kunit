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

package org.pcsoft.framework.kunit.electric.fluxdensity

import kotlin.math.abs
import kotlin.test.*
import kotlin.test.Test
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of

/**
 * `KElectricFluxDensityUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
class KElectricFluxDensityUnitSystemTest {

    private val tokens: List<Pair<KElectricFluxDensityUnitInstance, Double>> = listOf(
        coulombsPerSquareMeter to KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER.baseValue,
        coulombsPerSquareCentimeter to KElectricFluxDensityUnit.COULOMB_PER_SQUARE_CENTIMETER.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to C/m² and round-trips through `into`. */
    @Test
    fun `construction and round-trip`() = forEachCase(tokenArgs()) { case ->
        `construction and round-trip`(case[0] as KElectricFluxDensityUnitInstance, case[1] as Double)
    }

    private fun `construction and round-trip`(token: KElectricFluxDensityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(10001.0, ((1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)).value, 1e-6)
        assertEquals(9999.0, ((1 of coulombsPerSquareCentimeter) - (1 of coulombsPerSquareMeter)).value, 1e-6)
        assertTrue((1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter))
        assertEquals(0, (1 of coulombsPerSquareCentimeter).compareTo(10000 of coulombsPerSquareMeter))
    }

    /** Multiplying/dividing two densities leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)).value, 1e-9)
        assertEquals(2.0, ((6 of coulombsPerSquareMeter) / (3 of coulombsPerSquareMeter)).value, 1e-9)
    }

    /** Equality/hash by normalized C/m² value (`1 C/cm² == 10000 C/m²`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of coulombsPerSquareCentimeter, 10000 of coulombsPerSquareMeter)
        assertEquals(
            (1 of coulombsPerSquareCentimeter).hashCode(),
            (10000 of coulombsPerSquareMeter).hashCode(),
        )
        assertFalse((1 of coulombsPerSquareMeter) == (2 of coulombsPerSquareMeter))
        assertFalse((1 of coulombsPerSquareMeter).equals(1.0)) // not a KElectricFluxDensityUnitInstance
    }

    /** `toString` renders the normalized C/m² value. */
    @Test
    fun `toString base unit`() {
        assertEquals("10000.0 C/m²", (1 of coulombsPerSquareCentimeter).toString())
    }

    /** The real-world reading: a charged capacitor plate carries about 5 µC/m². */
    @Test
    fun `capacitor plate real world`() {
        assertEquals(5e-6, (5 of micro.coulombsPerSquareMeter).value, 1e-18)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a flux density. */
    @Test
    fun `toElectricFluxDensity on non-density fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toElectricFluxDensity() }
        assertFailsWith<IllegalStateException> {
            ((1 of coulombsPerSquareMeter).toUnit() * (1 of coulombsPerSquareMeter).toUnit())
                .toElectricFluxDensity()
        }
    }
}
