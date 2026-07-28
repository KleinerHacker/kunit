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

package org.pcsoft.framework.kunit.electric.current

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete electric current units (`KElectricCurrentUnitBareValues` + `KElectricCurrentUnitExtensions`):
 * every unit builds to its ampere value, round-trips through `into`, and reads correctly against amperes.
 * Covers all bare tokens.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricCurrentUnitTest {

    private val tokens: List<Triple<String, KElectricCurrentUnitInstance, Double>> = listOf(
        Triple("amperes", amperes, KElectricCurrentUnit.AMPERE.baseValue),
        Triple("biot", biot, KElectricCurrentUnit.BIOT.baseValue),
        Triple("abamperes", abamperes, KElectricCurrentUnit.BIOT.baseValue),
        Triple("statamperes", statamperes, KElectricCurrentUnit.STATAMPERE.baseValue),
        Triple("ampereTurns", ampereTurns, KElectricCurrentUnit.AMPERE_TURN.baseValue),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-20)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented ampere value and round-trips through `into`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenArgs")
    fun `unit conversion`(name: String, token: KElectricCurrentUnitInstance, base: Double) {
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into amperes, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
    }

    /** The abampere token is exactly the biot (alias spelling). */
    @Test
    fun `abampere is biot`() {
        assertEquals(biot, abamperes)
        assertEquals(1 of biot, 1 of abamperes)
    }

    /** The declared symbols and base values of every electric current unit. */
    @Test
    fun `unit symbols and base values`() {
        assertEquals("A", KElectricCurrentUnit.AMPERE.symbol)
        assertEquals("Bi", KElectricCurrentUnit.BIOT.symbol)
        assertEquals("statA", KElectricCurrentUnit.STATAMPERE.symbol)
        assertEquals("At", KElectricCurrentUnit.AMPERE_TURN.symbol)
        assertEquals(1.0, KElectricCurrentUnit.AMPERE.baseValue, 1e-20)
        assertEquals(10.0, KElectricCurrentUnit.BIOT.baseValue, 1e-20)
        assertEquals(3.335641e-10, KElectricCurrentUnit.STATAMPERE.baseValue, 1e-20)
        assertEquals(1.0, KElectricCurrentUnit.AMPERE_TURN.baseValue, 1e-20)
        assertEquals(KElectricCurrentUnit.AMPERE, KElectricCurrentUnit.BASE)
        assertEquals(4, KElectricCurrentUnit.entries.size)
    }

    /**
     * The magnetomotive force of a coil: 800 turns carrying 2.5 A drive 2000 At - dimensionally the same
     * as 2000 A, but named to document the magnetic circuit context.
     */
    @Test
    fun `ampere turn equals ampere`() {
        assertEquals(2000.0, (800 * 2.5 of ampereTurns).value, 1e-9)
        assertEquals(1 of amperes, 1 of ampereTurns)
    }
}
