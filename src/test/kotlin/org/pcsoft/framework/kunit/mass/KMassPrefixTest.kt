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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.atto
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.deca
import org.pcsoft.framework.kunit.deci
import org.pcsoft.framework.kunit.exa
import org.pcsoft.framework.kunit.femto
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.into
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
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the mass units. Mass accepts *any* magnitude, so all 24 SI prefixes are
 * available on every unit (the kilogram is exactly `kilo.grams`). Covers all prefixed extension
 * properties.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMassPrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        quetta to 1e30, ronna to 1e27, yotta to 1e24, zetta to 1e21, exa to 1e18, peta to 1e15,
        tera to 1e12, giga to 1e9, mega to 1e6, kilo to 1e3, hecto to 1e2, deca to 1e1,
        deci to 1e-1, centi to 1e-2, milli to 1e-3, micro to 1e-6, nano to 1e-9, pico to 1e-12,
        femto to 1e-15, atto to 1e-18, zepto to 1e-21, yocto to 1e-24, ronto to 1e-27, quecto to 1e-30,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Each SI prefix scales a grams template by its factor (`kilo.grams == 1000 g`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `si prefix on grams`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.grams).value, rel(factor))
    }

    /** The kilogram is exactly `kilo.grams` and round-trips. */
    @Test
    fun `kilogram is kilo grams`() {
        assertEquals(1000.0, (1 of kilo.grams).value, 1e-9)
        assertEquals(2.0, (2000 of grams) into kilo.grams, 1e-9)
    }

    /** Every prefixed unit extension property resolves and scales its unit by the prefix factor. */
    @Test
    fun `all prefixed unit extensions`() {
        assertEquals(1e3 * KMassUnit.GRAM.baseValue, (1 of kilo.grams).value, rel(1e3))
        assertEquals(1e3 * KMassUnit.TONNE.baseValue, (1 of kilo.tonnes).value, rel(1e9))
        assertEquals(1e-3 * KMassUnit.CARAT.baseValue, (1 of milli.carats).value, rel(1e-3))
        assertEquals(1e-3 * KMassUnit.GRAIN.baseValue, (1 of milli.grains).value, rel(1e-4))
        assertEquals(1e-3 * KMassUnit.DRAM.baseValue, (1 of milli.drams).value, rel(1e-3))
        assertEquals(1e3 * KMassUnit.OUNCE.baseValue, (1 of kilo.ounces).value, rel(1e5))
        assertEquals(1e3 * KMassUnit.POUND.baseValue, (1 of kilo.pounds).value, rel(1e6))
        assertEquals(1e3 * KMassUnit.STONE.baseValue, (1 of kilo.stones).value, rel(1e7))
        assertEquals(1e3 * KMassUnit.HUNDREDWEIGHT_US.baseValue, (1 of kilo.hundredweightsUS).value, rel(1e8))
        assertEquals(1e3 * KMassUnit.HUNDREDWEIGHT_UK.baseValue, (1 of kilo.hundredweightsUK).value, rel(1e8))
        assertEquals(1e3 * KMassUnit.SHORT_TON.baseValue, (1 of kilo.shortTons).value, rel(1e9))
        assertEquals(1e3 * KMassUnit.LONG_TON.baseValue, (1 of kilo.longTons).value, rel(1e9))
        assertEquals(1e3 * KMassUnit.SLUG.baseValue, (1 of kilo.slugs).value, rel(1e7))
        assertEquals(1e-3 * KMassUnit.PENNYWEIGHT.baseValue, (1 of milli.pennyweights).value, rel(1e-3))
        assertEquals(1e3 * KMassUnit.TROY_OUNCE.baseValue, (1 of kilo.troyOunces).value, rel(1e5))
        assertEquals(1e3 * KMassUnit.TROY_POUND.baseValue, (1 of kilo.troyPounds).value, rel(1e5))
        assertEquals(1e3 * KMassUnit.GERMAN_POUND.baseValue, (1 of kilo.germanPounds).value, rel(1e5))
        assertEquals(1e3 * KMassUnit.ZENTNER.baseValue, (1 of kilo.zentners).value, rel(1e7))
        assertEquals(1e-3 * KMassUnit.LOT.baseValue, (1 of milli.lots).value, rel(1e-2))
        assertEquals(1e3 * KMassUnit.JIN.baseValue, (1 of kilo.jin).value, rel(1e5))
        assertEquals(1e3 * KMassUnit.LIANG.baseValue, (1 of kilo.liang).value, rel(1e4))
        assertEquals(1e-3 * KMassUnit.MOMME.baseValue, (1 of milli.momme).value, rel(1e-2))
        assertEquals(1e3 * KMassUnit.KAN.baseValue, (1 of kilo.kan).value, rel(1e6))
        assertEquals(1e3 * KMassUnit.DALTON.baseValue, (1 of kilo.daltons).value, rel(1e-21))
    }
}
