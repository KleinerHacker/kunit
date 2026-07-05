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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KUnitPrefix
import kotlin.math.abs
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Prefix × unit cross-matrix for the distance group. For each dimension (length/area/volume) it verifies
 * that applying every SI prefix (`kilo`, `mega`, …) to every unit through the bare-value DSL
 * (`5 kilo meters`, `5 kilo squareMiles`, …) yields the correct base value.
 *
 * The `*Ops` lists capture the 24 prefix `infix` functions as `(prefix, (count, unit) -> instance)` pairs:
 * the lambda is the actual code under test (`{ n, u -> n kilo u }` calls `Number.kilo(unit)`), while the
 * expected value is computed independently from `prefix.factor * unit.baseValue` (squared/cubed for
 * area/volume, because the prefix scales the linear base before the exponent is applied). The unit side is
 * fed from the bare-value lists (`lengthBareValues`/`areaBareValues`/`volumeBareValues` in
 * `KDistanceTestFixtures.kt`) so the aliases themselves are exercised.
 */
class KDistancePrefixTest {
    // The 24 length prefix infix functions, keyed by prefix. Each lambda applies the prefix to a count and a
    // bare KDistanceUnit (`n kilo meters`), returning the constructed length.
    private val lengthOps: List<Pair<KUnitPrefix, (Number, KDistanceUnit) -> KLengthUnitInstance>> = listOf(
        KUnitPrefix.QUETTA to { n, u -> n quetta u },
        KUnitPrefix.RONNA to { n, u -> n ronna u },
        KUnitPrefix.YOTTA to { n, u -> n yotta u },
        KUnitPrefix.ZETTA to { n, u -> n zetta u },
        KUnitPrefix.EXA to { n, u -> n exa u },
        KUnitPrefix.PETA to { n, u -> n peta u },
        KUnitPrefix.TERA to { n, u -> n tera u },
        KUnitPrefix.GIGA to { n, u -> n giga u },
        KUnitPrefix.MEGA to { n, u -> n mega u },
        KUnitPrefix.KILO to { n, u -> n kilo u },
        KUnitPrefix.HECTO to { n, u -> n hecto u },
        KUnitPrefix.DECA to { n, u -> n deca u },
        KUnitPrefix.DECI to { n, u -> n deci u },
        KUnitPrefix.CENTI to { n, u -> n centi u },
        KUnitPrefix.MILLI to { n, u -> n milli u },
        KUnitPrefix.MICRO to { n, u -> n micro u },
        KUnitPrefix.NANO to { n, u -> n nano u },
        KUnitPrefix.PICO to { n, u -> n pico u },
        KUnitPrefix.FEMTO to { n, u -> n femto u },
        KUnitPrefix.ATTO to { n, u -> n atto u },
        KUnitPrefix.ZEPTO to { n, u -> n zepto u },
        KUnitPrefix.YOCTO to { n, u -> n yocto u },
        KUnitPrefix.RONTO to { n, u -> n ronto u },
        KUnitPrefix.QUECTO to { n, u -> n quecto u },
    )
    // The 24 area prefix infix functions. They take an area bare-value token (`n kilo squareMeters`); the
    // prefix scales the linear base unit, which is then squared.
    private val areaOps: List<Pair<KUnitPrefix, (Number, KDistanceAreaUnit) -> KAreaUnitInstance>> = listOf(
        KUnitPrefix.QUETTA to { n, u -> n quetta u },
        KUnitPrefix.RONNA to { n, u -> n ronna u },
        KUnitPrefix.YOTTA to { n, u -> n yotta u },
        KUnitPrefix.ZETTA to { n, u -> n zetta u },
        KUnitPrefix.EXA to { n, u -> n exa u },
        KUnitPrefix.PETA to { n, u -> n peta u },
        KUnitPrefix.TERA to { n, u -> n tera u },
        KUnitPrefix.GIGA to { n, u -> n giga u },
        KUnitPrefix.MEGA to { n, u -> n mega u },
        KUnitPrefix.KILO to { n, u -> n kilo u },
        KUnitPrefix.HECTO to { n, u -> n hecto u },
        KUnitPrefix.DECA to { n, u -> n deca u },
        KUnitPrefix.DECI to { n, u -> n deci u },
        KUnitPrefix.CENTI to { n, u -> n centi u },
        KUnitPrefix.MILLI to { n, u -> n milli u },
        KUnitPrefix.MICRO to { n, u -> n micro u },
        KUnitPrefix.NANO to { n, u -> n nano u },
        KUnitPrefix.PICO to { n, u -> n pico u },
        KUnitPrefix.FEMTO to { n, u -> n femto u },
        KUnitPrefix.ATTO to { n, u -> n atto u },
        KUnitPrefix.ZEPTO to { n, u -> n zepto u },
        KUnitPrefix.YOCTO to { n, u -> n yocto u },
        KUnitPrefix.RONTO to { n, u -> n ronto u },
        KUnitPrefix.QUECTO to { n, u -> n quecto u },
    )
    // The 24 volume prefix infix functions. They take a volume bare-value token (`n kilo cubicMeters`); the
    // prefix scales the linear base unit, which is then cubed.
    private val volumeOps: List<Pair<KUnitPrefix, (Number, KDistanceVolumeUnit) -> KVolumeUnitInstance>> = listOf(
        KUnitPrefix.QUETTA to { n, u -> n quetta u },
        KUnitPrefix.RONNA to { n, u -> n ronna u },
        KUnitPrefix.YOTTA to { n, u -> n yotta u },
        KUnitPrefix.ZETTA to { n, u -> n zetta u },
        KUnitPrefix.EXA to { n, u -> n exa u },
        KUnitPrefix.PETA to { n, u -> n peta u },
        KUnitPrefix.TERA to { n, u -> n tera u },
        KUnitPrefix.GIGA to { n, u -> n giga u },
        KUnitPrefix.MEGA to { n, u -> n mega u },
        KUnitPrefix.KILO to { n, u -> n kilo u },
        KUnitPrefix.HECTO to { n, u -> n hecto u },
        KUnitPrefix.DECA to { n, u -> n deca u },
        KUnitPrefix.DECI to { n, u -> n deci u },
        KUnitPrefix.CENTI to { n, u -> n centi u },
        KUnitPrefix.MILLI to { n, u -> n milli u },
        KUnitPrefix.MICRO to { n, u -> n micro u },
        KUnitPrefix.NANO to { n, u -> n nano u },
        KUnitPrefix.PICO to { n, u -> n pico u },
        KUnitPrefix.FEMTO to { n, u -> n femto u },
        KUnitPrefix.ATTO to { n, u -> n atto u },
        KUnitPrefix.ZEPTO to { n, u -> n zepto u },
        KUnitPrefix.YOCTO to { n, u -> n yocto u },
        KUnitPrefix.RONTO to { n, u -> n ronto u },
        KUnitPrefix.QUECTO to { n, u -> n quecto u },
    )

    // Relative tolerance: 1e-9 of the expected magnitude, floored just above zero so the prefix span
    // (quetta 1e30 … quecto 1e-30, squared/cubed) stays comparable without a fixed absolute epsilon.
    private fun delta(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-300)

    /** Every SI prefix applied to every length unit via `n <prefix> unit` yields `count * factor * baseValue` metres. */
    @Test
    fun `length prefix x unit matrix`() {
        for ((prefix, op) in lengthOps) for (unit in lengthBareValues) {
            val expected = 3.0 * prefix.factor * unit.baseValue
            assertEquals(expected, op(3, unit).value, delta(expected), "$prefix x $unit")
        }
    }

    /** Every SI prefix applied to every area token scales the linear base first, then squares it: `count * (factor * baseValue)²`. */
    @Test
    fun `area prefix x unit matrix`() {
        for ((prefix, op) in areaOps) for ((token, unit) in areaBareValues) {
            val expected = 3.0 * (prefix.factor * unit.baseValue).pow(2)
            assertEquals(expected, op(3, token).value, delta(expected), "$prefix x $unit")
        }
    }

    /** Every SI prefix applied to every volume token scales the linear base first, then cubes it: `count * (factor * baseValue)³`. */
    @Test
    fun `volume prefix x unit matrix`() {
        for ((prefix, op) in volumeOps) for ((token, unit) in volumeBareValues) {
            val expected = 3.0 * (prefix.factor * unit.baseValue).pow(3)
            assertEquals(expected, op(3, token).value, delta(expected), "$prefix x $unit")
        }
    }

    /** Each prefix applied to 1 metre equals exactly its numeric factor — isolates the prefix from the unit. */
    @Test
    fun `every length prefix standalone against meter`() {
        for ((prefix, op) in lengthOps) {
            assertEquals(prefix.factor, op(1, meters).value, delta(prefix.factor), "$prefix")
        }
    }

    /** Each prefix applied to 1 square metre equals its factor squared — isolates the prefix at exponent 2. */
    @Test
    fun `every area prefix standalone against square meter`() {
        for ((prefix, op) in areaOps) {
            val expected = prefix.factor.pow(2)
            assertEquals(expected, op(1, squareMeters).value, delta(expected), "$prefix")
        }
    }

    /** Each prefix applied to 1 cubic metre equals its factor cubed — isolates the prefix at exponent 3. */
    @Test
    fun `every volume prefix standalone against cubic meter`() {
        for ((prefix, op) in volumeOps) {
            val expected = prefix.factor.pow(3)
            assertEquals(expected, op(1, cubicMeters).value, delta(expected), "$prefix")
        }
    }

    /** Spot check that the prefix scales the linear base before squaring/cubing: `1 kilo squareMeters` == 1 km² == 1e6 m², `1 kilo cubicMeters` == 1e9 m³. */
    @Test
    fun `kilo square meters is a square kilometer`() {
        assertEquals(1_000_000.0, (1 kilo squareMeters).value, 1e-3)
        assertEquals(1_000_000_000.0, (1 kilo cubicMeters).value, 1e-3)
    }
}
