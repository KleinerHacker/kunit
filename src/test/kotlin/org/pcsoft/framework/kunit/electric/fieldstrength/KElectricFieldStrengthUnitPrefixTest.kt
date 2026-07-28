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

package org.pcsoft.framework.kunit.electric.fieldstrength

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
 * The SI prefix builders on the electric field strength units. The group accepts *any* magnitude, so all 24
 * SI prefixes are available on every unit (`kilo.voltsPerMeter` = kV/m). Covers every prefixed extension
 * property.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricFieldStrengthUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-40)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a voltsPerMeter template by its factor (`kilo.voltsPerMeter == 1000 V/m`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `si prefix on volts per meter`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.voltsPerMeter).value, rel(factor))
    }

    /** Every named unit exposes its own prefixed extension property (`kilo.voltsPerMeter`, …). */
    @Test
    fun `every electric field strength unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(
            f * KElectricFieldStrengthUnit.VOLT_PER_METER.baseValue,
            (1 of kilo.voltsPerMeter).value,
            rel(f),
        )
        assertEquals(
            f * KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue,
            (1 of kilo.voltsPerCentimeter).value,
            rel(f * KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue),
        )
        assertEquals(
            f * KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.baseValue,
            (1 of kilo.statvoltsPerCentimeter).value,
            rel(f * KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.baseValue),
        )
    }

    /** The real-world dielectric strength of air, roughly `3 kV/mm` = 3 MV/m. */
    @Test
    fun `dielectric strength of air in mega volts per meter`() {
        assertEquals(3.0e6, (3 of mega.voltsPerMeter).value, 1e-3)
    }
}
