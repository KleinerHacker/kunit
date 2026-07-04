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

package org.pcsoft.framework.kunit.length

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KDerivedUnit
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private fun area(sideA: Number, sideB: Number): KMixedUnitInstance = sideA.meters * sideB.meters

private fun volume(a: Number, b: Number, c: Number): KMixedUnitInstance = (a.meters * b.meters) * c.meters.toKMixedUnitInstance()

private fun volumeOf(cubicMeters: Double): KMixedUnitInstance = KMixedUnitInstance(cubicMeters, listOf(KUnitTerm(KLengthUnit.BASE, 3)))

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthDerivedUnitTest {

    private fun deriveds(): List<Arguments> = lengthDerivedUnitGenerators.map { Arguments.of(it.second) }

    private fun deriveOf(derived: KDerivedUnit<KLengthUnit>, n: Number): KLengthUnitInstance =
        lengthDerivedUnitGenerators.first { it.second == derived }.first(n)

    // region parameterized coverage of every derived unit

    @ParameterizedTest(name = "{0}")
    @MethodSource("deriveds")
    fun `every derived creator round trips through valueAs`(derived: KDerivedUnit<KLengthUnit>) {
        assertEquals(5.0, deriveOf(derived, 5).valueAs(derived), 5.0 * 1e-6, "valueAs round trip mismatch for $derived")
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deriveds")
    fun `every derived unit renders via toString`(derived: KDerivedUnit<KLengthUnit>) {
        val instance = deriveOf(derived, 5)
        assertEquals("${instance.valueAs(derived)} ${derived.symbol}", instance.toString(derived))
    }

    @ParameterizedTest(name = "milli {0}")
    @MethodSource("deriveds")
    fun `every derived unit round trips against its milli scale`(derived: KDerivedUnit<KLengthUnit>) {
        val milli = KUnitPrefix.MILLI with derived
        assertEquals(5000.0, deriveOf(derived, 5).valueAs(milli), 5000.0 * 1e-6, "milli $derived mismatch")
    }

    // endregion

    // region concrete forward conversions from geometry

    @Test
    fun `are from a 10x10 square`() {
        assertEquals(1.0, area(10, 10).valueAs(KLengthDerivedUnit.ARE), 1e-9)
    }

    @Test
    fun `hectare from a 200x50 rectangle`() {
        assertEquals(1.0, area(200, 50).valueAs(KLengthDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `liter from a 2x2x2 cube`() {
        assertEquals(8000.0, volume(2, 2, 2).valueAs(KLengthDerivedUnit.LITER), 1e-6)
    }

    // endregion

    // region error cases

    @Test
    fun `hectare fails against a pure length exponent`() {
        val length = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 1)))
        assertFailsWith<IllegalStateException> { length.valueAs(KLengthDerivedUnit.HECTARE) }
    }

    @Test
    fun `hectare fails against a volume exponent`() {
        assertFailsWith<IllegalStateException> { volumeOf(5.0).valueAs(KLengthDerivedUnit.HECTARE) }
    }

    // endregion

    @Test
    fun `valueAs and toString work directly on KLengthUnitInstance for a scaled derived unit target`() {
        val liters = 5.liters
        val milliliter = KUnitPrefix.MILLI with KLengthDerivedUnit.LITER

        assertEquals(5000.0, liters.valueAs(milliliter), 1e-6)
        assertEquals("5000.0 mL", liters.toString(milliliter))
    }
}
