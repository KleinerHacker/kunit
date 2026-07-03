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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The length-group prefix `infix` functions (e.g. `5 kilo meters`), each returning a
 * [KLengthUnitInstance] directly.
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

class KLengthUnitPrefixTest {

    /** Verifies that applying [prefix] via [apply] to 5 meters matches the equivalent unprefixed calculation. */
    private fun assertPrefixScales(prefix: KUnitPrefix, apply: (Number, KLengthUnit) -> KLengthUnitInstance) {
        val expected = (5.0 * prefix.factor).meters().value
        val actual = apply(5, KLengthUnit.METER).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @Test
    fun `quetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUETTA) { n, u -> n quetta u }

    @Test
    fun `ronna scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONNA) { n, u -> n ronna u }

    @Test
    fun `yotta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOTTA) { n, u -> n yotta u }

    @Test
    fun `zetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZETTA) { n, u -> n zetta u }

    @Test
    fun `exa scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.EXA) { n, u -> n exa u }

    @Test
    fun `peta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PETA) { n, u -> n peta u }

    @Test
    fun `tera scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.TERA) { n, u -> n tera u }

    @Test
    fun `giga scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.GIGA) { n, u -> n giga u }

    @Test
    fun `mega scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MEGA) { n, u -> n mega u }

    @Test
    fun `kilo scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.KILO) { n, u -> n kilo u }

    @Test
    fun `hecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.HECTO) { n, u -> n hecto u }

    @Test
    fun `deca scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECA) { n, u -> n deca u }

    @Test
    fun `deci scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECI) { n, u -> n deci u }

    @Test
    fun `centi scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.CENTI) { n, u -> n centi u }

    @Test
    fun `milli scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MILLI) { n, u -> n milli u }

    @Test
    fun `micro scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MICRO) { n, u -> n micro u }

    @Test
    fun `nano scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.NANO) { n, u -> n nano u }

    @Test
    fun `pico scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PICO) { n, u -> n pico u }

    @Test
    fun `femto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.FEMTO) { n, u -> n femto u }

    @Test
    fun `atto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ATTO) { n, u -> n atto u }

    @Test
    fun `zepto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZEPTO) { n, u -> n zepto u }

    @Test
    fun `yocto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOCTO) { n, u -> n yocto u }

    @Test
    fun `ronto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONTO) { n, u -> n ronto u }

    @Test
    fun `quecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUECTO) { n, u -> n quecto u }

    @Test
    fun `prefix infix returns a KLengthUnitInstance directly`() {
        val km: KLengthUnitInstance = 5 kilo meters

        assertEquals(5000.0, km.value, 1e-9)
        assertEquals(5.0, km.valueAs(KUnitPrefix.KILO with KLengthUnit.METER), 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), km.toKMixedUnitInstance().units)
    }

    @Test
    fun `every length unit combined with every prefix round trips`() {
        for ((applyPrefix, prefix) in prefixFunctions) {
            for ((_, unit) in lengthUnitGenerators) {
                val result = applyPrefix(5, unit)
                val expectedBase = 5.0 * prefix.factor * unit.baseValue

                assertEquals(expectedBase, result.value, expectedBase.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit mismatch")
                assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit valueAs mismatch")
            }
        }
    }

    @Test
    fun `every derived unit combined with every prefix round trips`() {
        for (prefix in KUnitPrefix.entries) {
            for ((_, derived) in lengthDerivedUnitGenerators) {
                val scaled = prefix with derived
                // A KMixedUnitInstance whose value is exactly 1 scaled-derived-unit, expressed in the base unit.
                val instance = KMixedUnitInstance(scaled.baseValue, listOf(KUnitTerm(KLengthUnit.BASE, derived.exponent)))

                val convertedBack = instance.valueAs(scaled)

                assertEquals(1.0, convertedBack, 1e-6, "prefix $prefix with derived unit $derived mismatch")
            }
        }
    }
}
