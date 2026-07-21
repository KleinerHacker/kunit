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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.assertEquals

/**
 * The concrete mass units (`KMassUnitBareValues` + `KMassUnitExtensions`): every unit builds to its
 * gram value, round-trips through `into`, and reads correctly against grams. Covers all bare tokens.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMassUnitTest {

    private val tokens: List<Triple<String, KMassUnitInstance, Double>> = listOf(
        Triple("grams", grams, KMassUnit.GRAM.baseValue),
        Triple("tonnes", tonnes, KMassUnit.TONNE.baseValue),
        Triple("carats", carats, KMassUnit.CARAT.baseValue),
        Triple("grains", grains, KMassUnit.GRAIN.baseValue),
        Triple("drams", drams, KMassUnit.DRAM.baseValue),
        Triple("ounces", ounces, KMassUnit.OUNCE.baseValue),
        Triple("pounds", pounds, KMassUnit.POUND.baseValue),
        Triple("stones", stones, KMassUnit.STONE.baseValue),
        Triple("hundredweightsUS", hundredweightsUS, KMassUnit.HUNDREDWEIGHT_US.baseValue),
        Triple("hundredweightsUK", hundredweightsUK, KMassUnit.HUNDREDWEIGHT_UK.baseValue),
        Triple("shortTons", shortTons, KMassUnit.SHORT_TON.baseValue),
        Triple("longTons", longTons, KMassUnit.LONG_TON.baseValue),
        Triple("slugs", slugs, KMassUnit.SLUG.baseValue),
        Triple("pennyweights", pennyweights, KMassUnit.PENNYWEIGHT.baseValue),
        Triple("troyOunces", troyOunces, KMassUnit.TROY_OUNCE.baseValue),
        Triple("troyPounds", troyPounds, KMassUnit.TROY_POUND.baseValue),
        Triple("germanPounds", germanPounds, KMassUnit.GERMAN_POUND.baseValue),
        Triple("zentners", zentners, KMassUnit.ZENTNER.baseValue),
        Triple("lots", lots, KMassUnit.LOT.baseValue),
        Triple("jin", jin, KMassUnit.JIN.baseValue),
        Triple("liang", liang, KMassUnit.LIANG.baseValue),
        Triple("momme", momme, KMassUnit.MOMME.baseValue),
        Triple("kan", kan, KMassUnit.KAN.baseValue),
        Triple("daltons", daltons, KMassUnit.DALTON.baseValue),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-15)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented gram value and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KMassUnitInstance, base: Double) {
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into grams, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
    }
}
