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
 * Prefix × unit cross-matrix for the distance group. It verifies that applying every SI prefix (`kilo`,
 * `mega`, …) to every length unit through the bare-value DSL (`5 kilo meters`, …) yields the correct base
 * value, and — combined with the power operation — that a prefixed length raised to a power (`5 kilo meters
 * pow 2` / `pow 3`) yields the correctly scaled area/volume across the whole prefix × unit space.
 *
 * The `lengthOps` list captures the 24 prefix `infix` functions as `(prefix, (count, unit) -> instance)`
 * pairs: the lambda is the actual code under test (`{ n, u -> n kilo u }` calls `Number.kilo(unit)`), while
 * the expected value is computed independently from `prefix.factor * unit.baseValue`. The unit side is fed
 * from `lengthBareValues` (in `KDistanceTestFixtures.kt`) so the aliases themselves are exercised.
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

    /** Every prefixed length raised to `pow 2` squares the whole scaled length: `(count * factor * baseValue)²`. */
    @Test
    fun `prefixed length pow 2 x unit matrix`() {
        for ((prefix, op) in lengthOps) for (unit in lengthBareValues) {
            val area = op(3, unit) pow 2
            assertEquals(2, area.exponent, "$prefix x $unit exponent")
            val expected = (3.0 * prefix.factor * unit.baseValue).pow(2)
            assertEquals(expected, area.value, delta(expected), "$prefix x $unit")
        }
    }

    /** Every prefixed length raised to `pow 3` cubes the whole scaled length: `(count * factor * baseValue)³`. */
    @Test
    fun `prefixed length pow 3 x unit matrix`() {
        for ((prefix, op) in lengthOps) for (unit in lengthBareValues) {
            val volume = op(3, unit) pow 3
            assertEquals(3, volume.exponent, "$prefix x $unit exponent")
            val expected = (3.0 * prefix.factor * unit.baseValue).pow(3)
            assertEquals(expected, volume.value, delta(expected), "$prefix x $unit")
        }
    }

    /** Each prefix applied to 1 metre equals exactly its numeric factor — isolates the prefix from the unit. */
    @Test
    fun `every length prefix standalone against meter`() {
        for ((prefix, op) in lengthOps) {
            assertEquals(prefix.factor, op(1, meters).value, delta(prefix.factor), "$prefix")
        }
    }

    /** Each prefix applied to 1 metre then `pow 2` equals its factor squared — isolates the prefix at exponent 2. */
    @Test
    fun `every length prefix pow 2 standalone against meter`() {
        for ((prefix, op) in lengthOps) {
            val expected = prefix.factor.pow(2)
            assertEquals(expected, (op(1, meters) pow 2).value, delta(expected), "$prefix")
        }
    }

    /** Each prefix applied to 1 metre then `pow 3` equals its factor cubed — isolates the prefix at exponent 3. */
    @Test
    fun `every length prefix pow 3 standalone against meter`() {
        for ((prefix, op) in lengthOps) {
            val expected = prefix.factor.pow(3)
            assertEquals(expected, (op(1, meters) pow 3).value, delta(expected), "$prefix")
        }
    }

    /** Spot check that a prefixed length squares/cubes correctly: `(1 kilo meters) pow 2` == 1 km² == 1e6 m², `pow 3` == 1e9 m³. */
    @Test
    fun `kilo meters powered is square and cubic kilometer`() {
        assertEquals(1_000_000.0, (1 kilo meters pow 2).value, 1e-3)
        assertEquals(1_000_000_000.0, (1 kilo meters pow 3).value, 1e-3)
    }
}
