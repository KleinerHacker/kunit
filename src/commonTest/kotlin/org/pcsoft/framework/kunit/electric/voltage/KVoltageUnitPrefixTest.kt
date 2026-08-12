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

package org.pcsoft.framework.kunit.electric.voltage

import org.pcsoft.framework.kunit.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the voltage units. Voltage accepts *any* magnitude, so all 24 SI prefixes are
 * available on every unit (`milli.volts` = mV, `kilo.volts` = kV). Covers the prefixed extension property.
 */
class KVoltageUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a volts template by its factor (`kilo.volts == 1000 V`). */
    @Test
    fun `si prefix on volts`() = forEachCase(prefixArgs()) { case ->
        `si prefix on volts`(case[0] as KPrefixBuilder, case[1] as Double)
    }

    private fun `si prefix on volts`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.volts).value, rel(factor))
    }

    /** Every named voltage unit exposes its own prefixed extension property (`kilo.statvolts`, …). */
    @Test
    fun `every voltage unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(f * KVoltageUnit.VOLT.baseValue, (1 of kilo.volts).value, rel(f))
        assertEquals(
            f * KVoltageUnit.STATVOLT.baseValue,
            (1 of kilo.statvolts).value,
            rel(f * KVoltageUnit.STATVOLT.baseValue)
        )
        assertEquals(
            f * KVoltageUnit.ABVOLT.baseValue,
            (1 of kilo.abvolts).value,
            rel(f * KVoltageUnit.ABVOLT.baseValue)
        )
        assertEquals(
            f * KVoltageUnit.WESTON_CELL.baseValue,
            (1 of kilo.westonCells).value,
            rel(f * KVoltageUnit.WESTON_CELL.baseValue)
        )
        assertEquals(
            f * KVoltageUnit.DANIELL.baseValue,
            (1 of kilo.daniells).value,
            rel(f * KVoltageUnit.DANIELL.baseValue)
        )
    }
}
