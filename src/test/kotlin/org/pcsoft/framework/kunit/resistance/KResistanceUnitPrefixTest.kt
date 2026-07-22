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

package org.pcsoft.framework.kunit.resistance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.atto
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.deca
import org.pcsoft.framework.kunit.deci
import org.pcsoft.framework.kunit.exa
import org.pcsoft.framework.kunit.femto
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.peta
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.quecto
import org.pcsoft.framework.kunit.quetta
import org.pcsoft.framework.kunit.ronna
import org.pcsoft.framework.kunit.ronto
import org.pcsoft.framework.kunit.tera
import org.pcsoft.framework.kunit.yocto
import org.pcsoft.framework.kunit.yotta
import org.pcsoft.framework.kunit.zepto
import org.pcsoft.framework.kunit.zetta
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the resistance units. Resistance accepts *any* magnitude, so all 24 SI prefixes
 * are available on every unit (`milli.ohms` = mΩ, `kilo.ohms` = kΩ). Covers the prefixed extension property.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KResistanceUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales an ohms template by its factor (`kilo.ohms == 1000 Ω`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `si prefix on ohms`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.ohms).value, rel(factor))
    }

    /** Every named resistance unit exposes its own prefixed extension property (`kilo.statohms`, …). */
    @Test
    fun `every resistance unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(f * KResistanceUnit.OHM.baseValue, (1 of kilo.ohms).value, rel(f))
        assertEquals(f * KResistanceUnit.STATOHM.baseValue, (1 of kilo.statohms).value, rel(f * KResistanceUnit.STATOHM.baseValue))
        assertEquals(f * KResistanceUnit.ABOHM.baseValue, (1 of kilo.abohms).value, rel(f * KResistanceUnit.ABOHM.baseValue))
        assertEquals(f * KResistanceUnit.INTERNATIONAL_OHM.baseValue, (1 of kilo.internationalOhms).value, rel(f * KResistanceUnit.INTERNATIONAL_OHM.baseValue))
        assertEquals(f * KResistanceUnit.LEGAL_OHM.baseValue, (1 of kilo.legalOhms).value, rel(f * KResistanceUnit.LEGAL_OHM.baseValue))
        assertEquals(f * KResistanceUnit.SIEMENS_UNIT.baseValue, (1 of kilo.siemensUnits).value, rel(f * KResistanceUnit.SIEMENS_UNIT.baseValue))
    }
}
