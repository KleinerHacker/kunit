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

package org.pcsoft.framework.kunit.electric.linearchargedensity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * `KLinearChargeDensityUnitInstance` surface: construction from an expression, `scaledBy`, same-type
 * arithmetic, comparison, equality, `toString`, decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLinearChargeDensityUnitSystemTest {

    private fun lambda(coulombsPerMeter: Double) = (coulombsPerMeter of coulombs) / (1 of meters)

    /** Construction from `charge / length` normalizes to coulombs per meter. */
    @Test
    fun `construction from expression`() {
        assertEquals(2.5e-6, ((5 of micro.coulombs) / (2 of meters)).value, 1e-20)
        assertEquals(2.0, lambda(2.0).value, 1e-12)
    }

    /** `scaledBy` backs number-times-unit construction on an existing density. */
    @Test
    fun `scaled by`() {
        assertEquals(6.0, lambda(2.0).scaledBy(3.0).value, 1e-12)
    }

    /** `+`/`-` operate on the normalized value. */
    @Test
    fun `same-type operators`() {
        assertEquals(5.0, (lambda(2.0) + lambda(3.0)).value, 1e-12)
        assertEquals(1.0, (lambda(3.0) - lambda(2.0)).value, 1e-12)
        assertTrue(lambda(3.0) > lambda(2.0))
        assertEquals(0, lambda(2.0).compareTo(lambda(2.0)))
    }

    /** Multiplying/dividing two densities leaves the group and yields a generic mixed unit. */
    @Test
    fun `mixed unit operators`() {
        assertEquals(6.0, (lambda(2.0) * lambda(3.0)).value, 1e-12)
        assertEquals(2.0, (lambda(6.0) / lambda(3.0)).value, 1e-12)
    }

    /** Equality/hash by normalized coulomb-per-meter value, independent of the source units. */
    @Test
    fun `equals and hashCode`() {
        assertEquals((1 of coulombs) / (1 of meters), (1000 of milli.coulombs) / (1 of meters))
        assertEquals(
            ((1 of coulombs) / (1 of meters)).hashCode(),
            ((1000 of milli.coulombs) / (1 of meters)).hashCode(),
        )
        assertFalse(lambda(1.0) == lambda(2.0))
        assertFalse(lambda(1.0).equals(1.0)) // not a KLinearChargeDensityUnitInstance
    }

    /** `toString` renders the normalized coulomb-per-meter value. */
    @Test
    fun `toString base unit`() {
        assertEquals("2.0 C/m", lambda(2.0).toString())
    }

    /** The real-world reading: 5 µC spread along a 2 m filament gives 2.5 µC/m. */
    @Test
    fun `charged filament real world`() {
        assertEquals(2.5e-6, ((5 of micro.coulombs) / (2 of meters)).value, 1e-20)
    }

    /** A mixed unit that is not a canonical normal form cannot be converted to a linear charge density. */
    @Test
    fun `toLinearChargeDensity on non-density fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toLinearChargeDensity() }
        assertFailsWith<IllegalStateException> { (lambda(1.0).toUnit() * lambda(1.0).toUnit()).toLinearChargeDensity() }
    }
}
