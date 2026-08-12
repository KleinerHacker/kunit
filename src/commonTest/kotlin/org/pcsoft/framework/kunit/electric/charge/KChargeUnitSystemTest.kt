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

package org.pcsoft.framework.kunit.electric.charge

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of

/** `KChargeUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
class KChargeUnitSystemTest {

    private val tokens: List<Pair<KChargeUnitInstance, Double>> = listOf(
        coulombs to KChargeUnit.COULOMB.baseValue,
        ampereSeconds to KChargeUnit.AMPERE_SECOND.baseValue,
        ampereHours to KChargeUnit.AMPERE_HOUR.baseValue,
        abcoulombs to KChargeUnit.ABCOULOMB.baseValue,
        statcoulombs to KChargeUnit.STATCOULOMB.baseValue,
        faradays to KChargeUnit.FARADAY.baseValue,
        elementaryCharges to KChargeUnit.ELEMENTARY_CHARGE.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to coulombs and round-trips through `into`. */
    @Test
    fun `construction and round-trip`() = forEachCase(tokenArgs()) { case ->
        `construction and round-trip`(case[0] as KChargeUnitInstance, case[1] as Double)
    }

    private fun `construction and round-trip`(token: KChargeUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized coulomb value (`1 Ah == 3600 C`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of ampereHours, 3600 of coulombs)
        assertEquals((1 of ampereHours).hashCode(), (3600 of coulombs).hashCode())
        assertFalse((1 of coulombs) == (2 of coulombs))
        assertFalse((1 of coulombs).equals(1.0)) // not a KChargeUnitInstance
    }

    /** `toString` renders the normalized coulomb value. */
    @Test
    fun `toString base unit`() {
        assertEquals("3600.0 C", (1 of ampereHours).toString())
    }

    /** A mixed unit that is not a canonical charge normal form cannot be converted to a charge. */
    @Test
    fun `toCharge on non-charge fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toCharge() }
        assertFailsWith<IllegalStateException> { ((1 of coulombs).toUnit() * (1 of coulombs).toUnit()).toCharge() }
    }

    /** `format` a charge into its base dimensions A*s. */
    @Test
    fun `format compositions`() {
        assertEquals("1.0 A*s", (1 of coulombs) format (amperes * seconds))
    }
}
