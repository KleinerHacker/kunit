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

/**
 * The length-group prefix `infix` functions (e.g. `5 kilo meters`), each returning a
 * [KLengthUnitInstance] directly. All 24 SI prefixes are representable for length.
 */
private val prefixFunctions: List<Pair<(Number, KLengthUnit) -> KLengthUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KLengthUnit -> n quetta u }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KLengthUnit -> n ronna u }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KLengthUnit -> n yotta u }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KLengthUnit -> n zetta u }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KLengthUnit -> n exa u }) to KUnitPrefix.EXA,
    ({ n: Number, u: KLengthUnit -> n peta u }) to KUnitPrefix.PETA,
    ({ n: Number, u: KLengthUnit -> n tera u }) to KUnitPrefix.TERA,
    ({ n: Number, u: KLengthUnit -> n giga u }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KLengthUnit -> n mega u }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KLengthUnit -> n kilo u }) to KUnitPrefix.KILO,
    ({ n: Number, u: KLengthUnit -> n hecto u }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KLengthUnit -> n deca u }) to KUnitPrefix.DECA,
    ({ n: Number, u: KLengthUnit -> n deci u }) to KUnitPrefix.DECI,
    ({ n: Number, u: KLengthUnit -> n centi u }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KLengthUnit -> n milli u }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KLengthUnit -> n micro u }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KLengthUnit -> n nano u }) to KUnitPrefix.NANO,
    ({ n: Number, u: KLengthUnit -> n pico u }) to KUnitPrefix.PICO,
    ({ n: Number, u: KLengthUnit -> n femto u }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KLengthUnit -> n atto u }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KLengthUnit -> n zepto u }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KLengthUnit -> n yocto u }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KLengthUnit -> n ronto u }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KLengthUnit -> n quecto u }) to KUnitPrefix.QUECTO
)

private fun applyPrefix(prefix: KUnitPrefix, n: Number, unit: KLengthUnit): KLengthUnitInstance =
    prefixFunctions.first { it.second == prefix }.first(n, unit)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthUnitPrefixTest {

    private fun prefixes(): List<Arguments> = prefixFunctions.map { Arguments.of(it.second) }

    private fun prefixUnitPairs(): List<Arguments> =
        prefixFunctions.flatMap { (_, prefix) -> lengthUnitGenerators.map { (_, unit) -> Arguments.of(prefix, unit) } }

    private fun prefixDerivedPairs(): List<Arguments> =
        KUnitPrefix.entries.flatMap { prefix -> lengthDerivedUnitGenerators.map { (_, derived) -> Arguments.of(prefix, derived) } }

    @ParameterizedTest(name = "{0}")
    @MethodSource("prefixes")
    fun `every prefix scales against the equivalent unprefixed calculation`(prefix: KUnitPrefix) {
        val expected = (5.0 * prefix.factor).meters.value
        val actual = applyPrefix(prefix, 5, KLengthUnit.METER).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("prefixUnitPairs")
    fun `every prefix combined with every unit round trips`(prefix: KUnitPrefix, unit: KLengthUnit) {
        val result = applyPrefix(prefix, 5, unit)
        val expectedBase = 5.0 * prefix.factor * unit.baseValue

        assertEquals(expectedBase, result.value, expectedBase.coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit mismatch")
        assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit valueAs mismatch")
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("prefixDerivedPairs")
    fun `every prefix combined with every derived unit round trips`(prefix: KUnitPrefix, derived: KDerivedUnit<KLengthUnit>) {
        val scaled = prefix with derived
        // A KMixedUnitInstance whose value is exactly 1 scaled-derived-unit, expressed in the base unit.
        val instance = KMixedUnitInstance(scaled.baseValue, listOf(KUnitTerm(KLengthUnit.BASE, derived.exponent)))

        assertEquals(1.0, instance.valueAs(scaled), 1e-6, "prefix $prefix with derived unit $derived mismatch")
    }

    @Test
    fun `prefix infix returns a KLengthUnitInstance directly`() {
        val km: KLengthUnitInstance = 5 kilo meters

        assertEquals(5000.0, km.value, 1e-9)
        assertEquals(5.0, km.valueAs(KUnitPrefix.KILO with KLengthUnit.METER), 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), km.toKMixedUnitInstance().units)
    }
}
