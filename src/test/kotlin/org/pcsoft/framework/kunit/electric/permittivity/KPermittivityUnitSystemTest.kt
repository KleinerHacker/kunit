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

package org.pcsoft.framework.kunit.electric.permittivity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pico
import kotlin.math.abs
import kotlin.test.*

/**
 * `KPermittivityUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KPermittivityUnitSystemTest {

    private val tokens: List<Pair<KPermittivityUnitInstance, Double>> = listOf(
        faradsPerMeter to KPermittivityUnit.FARAD_PER_METER.baseValue,
        faradsPerCentimeter to KPermittivityUnit.FARAD_PER_CENTIMETER.baseValue,
        vacuumPermittivity to KPermittivityUnit.VACUUM_PERMITTIVITY,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to farads per meter and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KPermittivityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(101.0, ((1 of faradsPerMeter) + (1 of faradsPerCentimeter)).value, 1e-9)
        assertEquals(99.0, ((1 of faradsPerCentimeter) - (1 of faradsPerMeter)).value, 1e-9)
        assertTrue((1 of faradsPerCentimeter) > (1 of faradsPerMeter))
        assertEquals(0, (1 of faradsPerCentimeter).compareTo(100 of faradsPerMeter))
    }

    /** Multiplying/dividing two permittivities leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of faradsPerMeter) * (3 of faradsPerMeter)).value, 1e-9)
        assertEquals(2.0, ((6 of faradsPerMeter) / (3 of faradsPerMeter)).value, 1e-9)
    }

    /** Equality/hash by normalized farad-per-meter value (`1 F/cm == 100 F/m`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of faradsPerCentimeter, 100 of faradsPerMeter)
        assertEquals((1 of faradsPerCentimeter).hashCode(), (100 of faradsPerMeter).hashCode())
        assertFalse((1 of faradsPerMeter) == (2 of faradsPerMeter))
        assertFalse((1 of faradsPerMeter).equals(1.0)) // not a KPermittivityUnitInstance
    }

    /** `toString` renders the normalized farad-per-meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("100.0 F/m", (1 of faradsPerCentimeter).toString())
    }

    /** The real-world reading: the vacuum permittivity is 8.854 pF/m. */
    @Test
    fun `vacuum permittivity real world`() {
        assertEquals(8.8541878188e-12, (1 of vacuumPermittivity).value, 1e-24)
        assertEquals(8.8541878188, (1 of vacuumPermittivity) into pico.faradsPerMeter, 1e-9)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a permittivity. */
    @Test
    fun `toPermittivity on non-permittivity fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toPermittivity() }
        assertFailsWith<IllegalStateException> {
            ((1 of faradsPerMeter).toUnit() * (1 of faradsPerMeter).toUnit()).toPermittivity()
        }
    }
}
