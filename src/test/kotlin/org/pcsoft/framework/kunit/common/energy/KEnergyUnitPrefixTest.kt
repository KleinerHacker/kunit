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

package org.pcsoft.framework.kunit.common.energy

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the energy units. Energy accepts *any* magnitude, so all 24 SI prefixes are
 * available on every unit (`kilo.joules` = kJ, `mega.electronVolts` = MeV). Covers the prefixed extension
 * property.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KEnergyUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-40)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a joules template by its factor (`kilo.joules == 1000 J`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `si prefix on joules`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.joules).value, rel(factor))
    }

    /** Every named energy unit exposes its own prefixed extension property (`kilo.joules`, …). */
    @Test
    fun `every energy unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(f * KEnergyUnit.JOULE.baseValue, (1 of kilo.joules).value, rel(f))
        assertEquals(f * KEnergyUnit.ERG.baseValue, (1 of kilo.ergs).value, rel(f * KEnergyUnit.ERG.baseValue))
        assertEquals(
            f * KEnergyUnit.CALORIE.baseValue,
            (1 of kilo.calories).value,
            rel(f * KEnergyUnit.CALORIE.baseValue),
        )
        assertEquals(
            f * KEnergyUnit.ELECTRON_VOLT.baseValue,
            (1 of kilo.electronVolts).value,
            rel(f * KEnergyUnit.ELECTRON_VOLT.baseValue),
        )
        assertEquals(
            f * KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue,
            (1 of kilo.britishThermalUnits).value,
            rel(f * KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue),
        )
    }

    /** The real-world nutrition notation `2000 kcal` of a daily diet. */
    @Test
    fun `daily diet in kilo calories`() {
        assertEquals(8.368e6, (2000 of kilo.calories).value, 1.0)
    }
}
