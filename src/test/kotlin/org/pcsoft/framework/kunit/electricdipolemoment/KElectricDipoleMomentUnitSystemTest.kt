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

package org.pcsoft.framework.kunit.electricdipolemoment

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * `KElectricDipoleMomentUnitInstance` surface: `of`/`into` construction and round-trip, same-type arithmetic,
 * comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricDipoleMomentUnitSystemTest {

    private val tokens: List<Pair<KElectricDipoleMomentUnitInstance, Double>> = listOf(
        coulombMeters to KElectricDipoleMomentUnit.COULOMB_METER.baseValue,
        debyes to KElectricDipoleMomentUnit.DEBYE.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-45)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to coulomb meters and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KElectricDipoleMomentUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** `+`/`-` operate on the normalized value and convert between units automatically. */
    @Test
    fun `same-type operators`() {
        assertEquals(5.0, ((2 of coulombMeters) + (3 of coulombMeters)).value, 1e-9)
        assertEquals(1.0, ((3 of coulombMeters) - (2 of coulombMeters)).value, 1e-9)
        assertEquals(6.671281904e-30, ((1 of debyes) + (1 of debyes)).value, 1e-42)
        assertTrue((1 of coulombMeters) > (1 of debyes))
        assertEquals(0, (1 of coulombMeters).compareTo(1 of coulombMeters))
    }

    /** Multiplying/dividing two dipole moments leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, ((2 of coulombMeters) * (3 of coulombMeters)).value, 1e-9)
        assertEquals(2.0, ((6 of coulombMeters) / (3 of coulombMeters)).value, 1e-9)
    }

    /** Equality/hash by normalized coulomb-meter value. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of debyes, 3.335640952e-30 of coulombMeters)
        assertEquals((1 of debyes).hashCode(), (3.335640952e-30 of coulombMeters).hashCode())
        assertFalse((1 of coulombMeters) == (2 of coulombMeters))
        assertFalse((1 of coulombMeters).equals(1.0)) // not a KElectricDipoleMomentUnitInstance
    }

    /** `toString` renders the normalized coulomb-meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("2.0 C·m", (2 of coulombMeters).toString())
    }

    /** The real-world reading: the water molecule has a dipole moment of 1.85 D. */
    @Test
    fun `water molecule real world`() {
        assertEquals(6.1709357612e-30, (1.85 of debyes).value, 1e-42)
        assertEquals(1.85, (6.1709357612e-30 of coulombMeters) into debyes, 1e-9)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a dipole moment. */
    @Test
    fun `toElectricDipoleMoment on non-moment fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toElectricDipoleMoment() }
        assertFailsWith<IllegalStateException> {
            ((1 of coulombMeters).toUnit() * (1 of coulombMeters).toUnit()).toElectricDipoleMoment()
        }
    }
}
