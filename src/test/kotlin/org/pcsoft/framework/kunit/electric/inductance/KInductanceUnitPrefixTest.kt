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

package org.pcsoft.framework.kunit.electric.inductance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the inductance units. Inductance accepts *any* magnitude, so all 24 SI prefixes
 * are available on every unit (`milli.henries` = mH, `micro.henries` = µH). Covers the prefixed extension property.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KInductanceUnitPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a henries template by its factor (`kilo.henries == 1000 H`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `si prefix on henries`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.henries).value, rel(factor))
    }

    /** Every named inductance unit exposes its own prefixed extension property (`milli.henries`, …). */
    @Test
    fun `every inductance unit has a prefixed property`() {
        val f = kilo.prefix.factor
        assertEquals(f * KInductanceUnit.HENRY.baseValue, (1 of kilo.henries).value, rel(f))
        assertEquals(
            f * KInductanceUnit.WEBER_PER_AMPERE.baseValue,
            (1 of kilo.webersPerAmpere).value,
            rel(f * KInductanceUnit.WEBER_PER_AMPERE.baseValue),
        )
        assertEquals(
            f * KInductanceUnit.ABHENRY.baseValue,
            (1 of kilo.abhenries).value,
            rel(f * KInductanceUnit.ABHENRY.baseValue),
        )
        assertEquals(
            f * KInductanceUnit.STATHENRY.baseValue,
            (1 of kilo.stathenries).value,
            rel(f * KInductanceUnit.STATHENRY.baseValue),
        )
    }

    /** The real-world choke notation `470 µH` equals 0.00047 H. */
    @Test
    fun `choke inductance in micro henries`() {
        assertEquals(4.7e-4, (470 of micro.henries).value, 1e-12)
    }
}
