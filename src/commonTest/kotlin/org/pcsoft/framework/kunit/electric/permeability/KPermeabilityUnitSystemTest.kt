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

package org.pcsoft.framework.kunit.electric.permeability

import kotlin.math.abs
import kotlin.test.*
import kotlin.test.Test
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of

/**
 * `KPermeabilityUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
class KPermeabilityUnitSystemTest {

    private val tokens: List<Pair<KPermeabilityUnitInstance, Double>> = listOf(
        henriesPerMeter to KPermeabilityUnit.HENRY_PER_METER.baseValue,
        henriesPerCentimeter to KPermeabilityUnit.HENRY_PER_CENTIMETER.baseValue,
        vacuumPermeability to KPermeabilityUnit.VACUUM_PERMEABILITY,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to henries per meter and round-trips through `into`. */
    @Test
    fun `construction and round-trip`() = forEachCase(tokenArgs()) { case ->
        `construction and round-trip`(case[0] as KPermeabilityUnitInstance, case[1] as Double)
    }

    private fun `construction and round-trip`(token: KPermeabilityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(101.0, ((1 of henriesPerMeter) + (1 of henriesPerCentimeter)).value, 1e-9)
        assertEquals(99.0, ((1 of henriesPerCentimeter) - (1 of henriesPerMeter)).value, 1e-9)
        assertTrue((1 of henriesPerCentimeter) > (1 of henriesPerMeter))
        assertEquals(0, (1 of henriesPerCentimeter).compareTo(100 of henriesPerMeter))
    }

    /** Multiplying/dividing two permeabilities leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of henriesPerMeter) * (3 of henriesPerMeter)).value, 1e-9)
        assertEquals(2.0, ((6 of henriesPerMeter) / (3 of henriesPerMeter)).value, 1e-9)
    }

    /** Equality/hash by normalized henry-per-meter value (`1 H/cm == 100 H/m`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of henriesPerCentimeter, 100 of henriesPerMeter)
        assertEquals((1 of henriesPerCentimeter).hashCode(), (100 of henriesPerMeter).hashCode())
        assertFalse((1 of henriesPerMeter) == (2 of henriesPerMeter))
        assertFalse((1 of henriesPerMeter).equals(1.0)) // not a KPermeabilityUnitInstance
    }

    /** `toString` renders the normalized henry-per-meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("100.0 H/m", (1 of henriesPerCentimeter).toString())
    }

    /** The real-world reading: the vacuum permeability is 1.257 µH/m. */
    @Test
    fun `vacuum permeability real world`() {
        assertEquals(1.25663706127e-6, (1 of vacuumPermeability).value, 1e-18)
        assertEquals(1.25663706127, (1 of vacuumPermeability) into micro.henriesPerMeter, 1e-9)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a permeability. */
    @Test
    fun `toPermeability on non-permeability fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toPermeability() }
        assertFailsWith<IllegalStateException> {
            ((1 of henriesPerMeter).toUnit() * (1 of henriesPerMeter).toUnit()).toPermeability()
        }
    }
}
