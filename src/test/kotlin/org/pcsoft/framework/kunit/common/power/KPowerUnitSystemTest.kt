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

package org.pcsoft.framework.kunit.common.power

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KPowerUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KPowerUnitSystemTest {

    private val tokens: List<Pair<KPowerUnitInstance, Double>> = listOf(
        watts to KPowerUnit.WATT.baseValue,
        metricHorsePowers to KPowerUnit.METRIC_HORSE_POWER.baseValue,
        mechanicalHorsePowers to KPowerUnit.MECHANICAL_HORSE_POWER.baseValue,
        ergsPerSecond to KPowerUnit.ERG_PER_SECOND.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to watts and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KPowerUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized watt value (`1 kW == 1000 W`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.watts, 1000 of watts)
        assertEquals((1 of kilo.watts).hashCode(), (1000 of watts).hashCode())
        assertFalse((1 of watts) == (2 of watts))
        assertFalse((1 of watts).equals(1.0)) // not a KPowerUnitInstance
    }

    /** `toString` renders the normalized watt value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 W", (1 of kilo.watts).toString())
    }

    /** A real-world reading: a 100 PS engine is 73.5 kW. */
    @Test
    fun `engine power in kilo watts`() {
        assertEquals(73.549875, (100 of metricHorsePowers) into kilo.watts, 1e-9)
    }

    /** A mixed unit that is not a canonical power normal form cannot be converted to a power. */
    @Test
    fun `toPower on non-power fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toPower() }
        assertFailsWith<IllegalStateException> { ((1 of watts).toUnit() * (1 of watts).toUnit()).toPower() }
    }
}
