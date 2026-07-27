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

package org.pcsoft.framework.kunit.energy

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/** `KEnergyUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`, decomposition guard. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KEnergyUnitSystemTest {

    private val tokens: List<Pair<KEnergyUnitInstance, Double>> = listOf(
        joules to KEnergyUnit.JOULE.baseValue,
        ergs to KEnergyUnit.ERG.baseValue,
        calories to KEnergyUnit.CALORIE.baseValue,
        electronVolts to KEnergyUnit.ELECTRON_VOLT.baseValue,
        britishThermalUnits to KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to joules and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KEnergyUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized joule value (`1 kJ == 1000 J`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joules, 1000 of joules)
        assertEquals((1 of kilo.joules).hashCode(), (1000 of joules).hashCode())
        assertFalse((1 of joules) == (2 of joules))
        assertFalse((1 of joules).equals(1.0)) // not a KEnergyUnitInstance
    }

    /** `toString` renders the normalized joule value. */
    @Test
    fun `toString base unit`() {
        assertEquals("1000.0 J", (1 of kilo.joules).toString())
    }

    /** The kilowatt hour has no token and is built as `kilo.watts * hours`: 1 kWh = 3.6 MJ. */
    @Test
    fun `kilowatt hour without a token`() {
        val kwh = (1 of kilo.watts) * (1 of hours)
        assertEquals(3.6e6, kwh.value, 1e-3)
        assertEquals(3600.0, kwh into kilo.joules, 1e-6)
    }

    /** A mixed unit that is not a canonical energy normal form cannot be converted to an energy. */
    @Test
    fun `toEnergy on non-energy fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toEnergy() }
        assertFailsWith<IllegalStateException> { ((1 of joules).toUnit() * (1 of joules).toUnit()).toEnergy() }
    }
}
