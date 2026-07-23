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

package org.pcsoft.framework.kunit.voltage

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KVoltageUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KVoltageUnitSystemTest {

    private val tokens: List<Pair<KVoltageUnitInstance, Double>> = listOf(
        volts to KVoltageUnit.VOLT.baseValue,
        statvolts to KVoltageUnit.STATVOLT.baseValue,
        abvolts to KVoltageUnit.ABVOLT.baseValue,
        westonCells to KVoltageUnit.WESTON_CELL.baseValue,
        daniells to KVoltageUnit.DANIELL.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to volts and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KVoltageUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized volt value (`1 kV == 1000 V`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.volts, 1000 of volts)
        assertEquals((1 of kilo.volts).hashCode(), (1000 of volts).hashCode())
        assertFalse((1 of volts) == (2 of volts))
        assertFalse((1 of volts).equals(1.0)) // not a KVoltageUnitInstance
    }

    /** `toString` renders the normalized volt value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 V", (1 of kilo.volts).toString())
    }

    /** A mixed unit that is not a canonical voltage normal form cannot be converted to a voltage. */
    @Test
    fun `toVoltage on non-voltage fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toVoltage() }
        assertFailsWith<IllegalStateException> { ((1 of volts).toUnit() * (1 of volts).toUnit()).toVoltage() }
    }

    /** `format` a voltage into its base dimensions g*m^2*s^-3*A^-1. */
    @Test
    fun `format compositions`() {
        assertEquals("1.0 g*m^2*s^-3*A^-1", (1 of volts) format (grams * (meters pow 2) / (seconds pow 3) / amperes.toUnit()))
    }
}
