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

package org.pcsoft.framework.kunit.time

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * All 24 time-group prefix `infix` functions (e.g. `5 milli seconds`). The result is backed by a
 * [java.time.Duration] whose whole-seconds part is a `Long`, so extreme prefixes on a multi-second base
 * are not representable. Standalone per-prefix tests therefore apply the prefix to a count of `1 / factor`
 * (which always lands at exactly `1.0` second), while the prefix × unit matrix is restricted to the band of
 * prefixes representable for **all** time units (see [representablePrefixFunctions]).
 */
private val allPrefixFunctions: List<Pair<(Number, KTimeUnit) -> KTimeUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KTimeUnit -> n quetta u }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KTimeUnit -> n ronna u }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KTimeUnit -> n yotta u }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KTimeUnit -> n zetta u }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KTimeUnit -> n exa u }) to KUnitPrefix.EXA,
    ({ n: Number, u: KTimeUnit -> n peta u }) to KUnitPrefix.PETA,
    ({ n: Number, u: KTimeUnit -> n tera u }) to KUnitPrefix.TERA,
    ({ n: Number, u: KTimeUnit -> n giga u }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KTimeUnit -> n mega u }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KTimeUnit -> n kilo u }) to KUnitPrefix.KILO,
    ({ n: Number, u: KTimeUnit -> n hecto u }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KTimeUnit -> n deca u }) to KUnitPrefix.DECA,
    ({ n: Number, u: KTimeUnit -> n deci u }) to KUnitPrefix.DECI,
    ({ n: Number, u: KTimeUnit -> n centi u }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KTimeUnit -> n milli u }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KTimeUnit -> n micro u }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KTimeUnit -> n nano u }) to KUnitPrefix.NANO,
    ({ n: Number, u: KTimeUnit -> n pico u }) to KUnitPrefix.PICO,
    ({ n: Number, u: KTimeUnit -> n femto u }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KTimeUnit -> n atto u }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KTimeUnit -> n zepto u }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KTimeUnit -> n yocto u }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KTimeUnit -> n ronto u }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KTimeUnit -> n quecto u }) to KUnitPrefix.QUECTO
)

/** The band of prefixes that stays representable for every time unit (second … day) at a base count of 5. */
private val representablePrefixFunctions: List<KUnitPrefix> = listOf(
    KUnitPrefix.GIGA, KUnitPrefix.MEGA, KUnitPrefix.KILO, KUnitPrefix.HECTO, KUnitPrefix.DECA,
    KUnitPrefix.DECI, KUnitPrefix.CENTI, KUnitPrefix.MILLI, KUnitPrefix.MICRO, KUnitPrefix.NANO
)

private fun applyPrefix(prefix: KUnitPrefix, n: Number, unit: KTimeUnit): KTimeUnitInstance =
    allPrefixFunctions.first { it.second == prefix }.first(n, unit)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimeUnitPrefixTest {

    private fun prefixes(): List<Arguments> = allPrefixFunctions.map { Arguments.of(it.second) }

    private fun prefixUnitPairs(): List<Arguments> =
        representablePrefixFunctions.flatMap { prefix -> timeUnitGenerators.map { (_, unit) -> Arguments.of(prefix, unit) } }

    @ParameterizedTest(name = "{0}")
    @MethodSource("prefixes")
    fun `every prefix scales by its factor`(prefix: KUnitPrefix) {
        // Applying the prefix to 1 / factor seconds must land at exactly 1.0 second, which stays within the
        // Duration-backed range for every prefix, however extreme.
        val actual = applyPrefix(prefix, 1.0 / prefix.factor, KTimeUnit.SECOND).value
        assertEquals(1.0, actual, 1e-9, "prefix $prefix mismatch")
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("prefixUnitPairs")
    fun `every representable prefix combined with every unit round trips`(prefix: KUnitPrefix, unit: KTimeUnit) {
        val result = applyPrefix(prefix, 5, unit)
        val expectedSeconds = 5.0 * prefix.factor * unit.baseValue

        assertEquals(expectedSeconds, result.value, expectedSeconds.coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit mismatch")
        assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
            "prefix $prefix with unit $unit read-back mismatch")
    }

    @Test
    fun `prefix infix returns a KTimeUnitInstance directly`() {
        val ms: KTimeUnitInstance = 5 milli seconds

        assertEquals(0.005, ms.value, 1e-12)
        assertEquals(5.0, ms.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND), 1e-9)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), ms.toKMixedUnitInstance().units)
    }
}
