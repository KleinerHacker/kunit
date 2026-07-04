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

package org.pcsoft.framework.kunit.speed

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The speed-group prefix `infix` functions (e.g. `5 kilo metersPerSecond`), each returning a
 * [KSpeedUnitInstance] directly. All 24 SI prefixes are representable for speed.
 */
private val prefixFunctions: List<Pair<(Number, KSpeedUnit) -> KSpeedUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KSpeedUnit -> n quetta u }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KSpeedUnit -> n ronna u }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KSpeedUnit -> n yotta u }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KSpeedUnit -> n zetta u }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KSpeedUnit -> n exa u }) to KUnitPrefix.EXA,
    ({ n: Number, u: KSpeedUnit -> n peta u }) to KUnitPrefix.PETA,
    ({ n: Number, u: KSpeedUnit -> n tera u }) to KUnitPrefix.TERA,
    ({ n: Number, u: KSpeedUnit -> n giga u }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KSpeedUnit -> n mega u }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KSpeedUnit -> n kilo u }) to KUnitPrefix.KILO,
    ({ n: Number, u: KSpeedUnit -> n hecto u }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KSpeedUnit -> n deca u }) to KUnitPrefix.DECA,
    ({ n: Number, u: KSpeedUnit -> n deci u }) to KUnitPrefix.DECI,
    ({ n: Number, u: KSpeedUnit -> n centi u }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KSpeedUnit -> n milli u }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KSpeedUnit -> n micro u }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KSpeedUnit -> n nano u }) to KUnitPrefix.NANO,
    ({ n: Number, u: KSpeedUnit -> n pico u }) to KUnitPrefix.PICO,
    ({ n: Number, u: KSpeedUnit -> n femto u }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KSpeedUnit -> n atto u }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KSpeedUnit -> n zepto u }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KSpeedUnit -> n yocto u }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KSpeedUnit -> n ronto u }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KSpeedUnit -> n quecto u }) to KUnitPrefix.QUECTO
)

private fun applyPrefix(prefix: KUnitPrefix, n: Number, unit: KSpeedUnit): KSpeedUnitInstance =
    prefixFunctions.first { it.second == prefix }.first(n, unit)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KSpeedUnitPrefixTest {

    private fun prefixes(): List<Arguments> = prefixFunctions.map { Arguments.of(it.second) }

    private fun prefixUnitPairs(): List<Arguments> =
        prefixFunctions.flatMap { (_, prefix) -> speedUnitGenerators.map { (_, unit) -> Arguments.of(prefix, unit) } }

    @ParameterizedTest(name = "{0}")
    @MethodSource("prefixes")
    fun `every prefix scales against the equivalent unprefixed calculation`(prefix: KUnitPrefix) {
        val expected = (5.0 * prefix.factor).metersPerSecond.value
        val actual = applyPrefix(prefix, 5, KSpeedUnit.METERS_PER_SECOND).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("prefixUnitPairs")
    fun `every prefix combined with every unit round trips`(prefix: KUnitPrefix, unit: KSpeedUnit) {
        val result = applyPrefix(prefix, 5, unit)
        val expectedBase = 5.0 * prefix.factor * unit.baseValue

        assertEquals(expectedBase, result.value, expectedBase.coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit mismatch")
        assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit valueAs mismatch")
    }

    @Test
    fun `prefix infix returns a KSpeedUnitInstance directly`() {
        val kmps: KSpeedUnitInstance = 5 kilo metersPerSecond

        assertEquals(5000.0, kmps.value, 1e-9)
        assertEquals(5.0, kmps.valueAs(KUnitPrefix.KILO with KSpeedUnit.METERS_PER_SECOND), 1e-9)
        assertEquals(
            setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
            kmps.toKMixedUnitInstance().units.toSet()
        )
    }
}
