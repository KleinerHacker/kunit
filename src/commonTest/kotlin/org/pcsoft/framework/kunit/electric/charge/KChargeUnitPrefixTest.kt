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

import org.pcsoft.framework.kunit.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the charge units. Charge accepts *any* magnitude, so all 24 SI prefixes are
 * available on every unit (`milli.coulombs` = mC, `milli.ampereHours` = mAh). Covers the prefixed extension property.
 */
class KChargeUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a coulombs template by its factor (`kilo.coulombs == 1000 C`). */
    @Test
    fun `si prefix on coulombs`() = forEachCase(prefixArgs()) { case ->
        `si prefix on coulombs`(case[0] as KPrefixBuilder, case[1] as Double)
    }

    private fun `si prefix on coulombs`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.coulombs).value, rel(factor))
    }

    /** Every named charge unit exposes its own prefixed extension property (`milli.ampereHours`, …). */
    @Test
    fun `every charge unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(f * KChargeUnit.COULOMB.baseValue, (1 of kilo.coulombs).value, rel(f))
        assertEquals(
            f * KChargeUnit.AMPERE_SECOND.baseValue,
            (1 of kilo.ampereSeconds).value,
            rel(f * KChargeUnit.AMPERE_SECOND.baseValue)
        )
        assertEquals(
            f * KChargeUnit.AMPERE_HOUR.baseValue,
            (1 of kilo.ampereHours).value,
            rel(f * KChargeUnit.AMPERE_HOUR.baseValue)
        )
        assertEquals(
            f * KChargeUnit.ABCOULOMB.baseValue,
            (1 of kilo.abcoulombs).value,
            rel(f * KChargeUnit.ABCOULOMB.baseValue)
        )
        assertEquals(
            f * KChargeUnit.STATCOULOMB.baseValue,
            (1 of kilo.statcoulombs).value,
            rel(f * KChargeUnit.STATCOULOMB.baseValue)
        )
        assertEquals(
            f * KChargeUnit.FARADAY.baseValue,
            (1 of kilo.faradays).value,
            rel(f * KChargeUnit.FARADAY.baseValue)
        )
        assertEquals(
            f * KChargeUnit.ELEMENTARY_CHARGE.baseValue,
            (1 of kilo.elementaryCharges).value,
            rel(f * KChargeUnit.ELEMENTARY_CHARGE.baseValue)
        )
    }

    /** The real-world battery notation `2000 mAh` equals 7200 C. */
    @Test
    fun `battery capacity in milli ampere hours`() {
        assertEquals(7200.0, (2000 of milli.ampereHours).value, 1e-6)
    }
}
