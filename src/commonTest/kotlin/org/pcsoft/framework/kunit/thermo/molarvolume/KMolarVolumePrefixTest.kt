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

package org.pcsoft.framework.kunit.thermo.molarvolume

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.*

/** All 24 SI prefix builders on every named molar volume unit, each with its own assertion. */
class KMolarVolumePrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-40)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    @Test
    fun `si prefix on cubic meters per mole`() = forEachCase(prefixArgs()) { case ->
        `si prefix on cubic meters per mole`(case[0] as KPrefixBuilder, case[1] as Double)
    }

    private fun `si prefix on cubic meters per mole`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.cubicMetersPerMole).value, rel(factor))
    }

    @Test
    fun `si prefix on liters per mole`() = forEachCase(prefixArgs()) { case ->
        `si prefix on liters per mole`(case[0] as KPrefixBuilder, case[1] as Double)
    }

    private fun `si prefix on liters per mole`(builder: KPrefixBuilder, factor: Double) {
        val expected = factor * 1.0e-3
        assertEquals(expected, (1 of builder.litersPerMole).value, rel(expected))
    }

    @Test
    fun `si prefix on cubic centimeters per mole`() = forEachCase(prefixArgs()) { case ->
        `si prefix on cubic centimeters per mole`(case[0] as KPrefixBuilder, case[1] as Double)
    }

    private fun `si prefix on cubic centimeters per mole`(builder: KPrefixBuilder, factor: Double) {
        val expected = factor * 1.0e-6
        assertEquals(expected, (1 of builder.cubicCentimetersPerMole).value, rel(expected))
    }
}
