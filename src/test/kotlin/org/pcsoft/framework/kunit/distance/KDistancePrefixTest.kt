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

class KDistancePrefixTest {
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
    private val areaOps: List<Pair<KUnitPrefix, (Number, KDistanceUnit) -> KAreaUnitInstance>> = listOf(
        KUnitPrefix.QUETTA to { n, u -> n quetta KDistanceAreaUnit(u) },
        KUnitPrefix.RONNA to { n, u -> n ronna KDistanceAreaUnit(u) },
        KUnitPrefix.YOTTA to { n, u -> n yotta KDistanceAreaUnit(u) },
        KUnitPrefix.ZETTA to { n, u -> n zetta KDistanceAreaUnit(u) },
        KUnitPrefix.EXA to { n, u -> n exa KDistanceAreaUnit(u) },
        KUnitPrefix.PETA to { n, u -> n peta KDistanceAreaUnit(u) },
        KUnitPrefix.TERA to { n, u -> n tera KDistanceAreaUnit(u) },
        KUnitPrefix.GIGA to { n, u -> n giga KDistanceAreaUnit(u) },
        KUnitPrefix.MEGA to { n, u -> n mega KDistanceAreaUnit(u) },
        KUnitPrefix.KILO to { n, u -> n kilo KDistanceAreaUnit(u) },
        KUnitPrefix.HECTO to { n, u -> n hecto KDistanceAreaUnit(u) },
        KUnitPrefix.DECA to { n, u -> n deca KDistanceAreaUnit(u) },
        KUnitPrefix.DECI to { n, u -> n deci KDistanceAreaUnit(u) },
        KUnitPrefix.CENTI to { n, u -> n centi KDistanceAreaUnit(u) },
        KUnitPrefix.MILLI to { n, u -> n milli KDistanceAreaUnit(u) },
        KUnitPrefix.MICRO to { n, u -> n micro KDistanceAreaUnit(u) },
        KUnitPrefix.NANO to { n, u -> n nano KDistanceAreaUnit(u) },
        KUnitPrefix.PICO to { n, u -> n pico KDistanceAreaUnit(u) },
        KUnitPrefix.FEMTO to { n, u -> n femto KDistanceAreaUnit(u) },
        KUnitPrefix.ATTO to { n, u -> n atto KDistanceAreaUnit(u) },
        KUnitPrefix.ZEPTO to { n, u -> n zepto KDistanceAreaUnit(u) },
        KUnitPrefix.YOCTO to { n, u -> n yocto KDistanceAreaUnit(u) },
        KUnitPrefix.RONTO to { n, u -> n ronto KDistanceAreaUnit(u) },
        KUnitPrefix.QUECTO to { n, u -> n quecto KDistanceAreaUnit(u) },
    )
    private val volumeOps: List<Pair<KUnitPrefix, (Number, KDistanceUnit) -> KVolumeUnitInstance>> = listOf(
        KUnitPrefix.QUETTA to { n, u -> n quetta KDistanceVolumeUnit(u) },
        KUnitPrefix.RONNA to { n, u -> n ronna KDistanceVolumeUnit(u) },
        KUnitPrefix.YOTTA to { n, u -> n yotta KDistanceVolumeUnit(u) },
        KUnitPrefix.ZETTA to { n, u -> n zetta KDistanceVolumeUnit(u) },
        KUnitPrefix.EXA to { n, u -> n exa KDistanceVolumeUnit(u) },
        KUnitPrefix.PETA to { n, u -> n peta KDistanceVolumeUnit(u) },
        KUnitPrefix.TERA to { n, u -> n tera KDistanceVolumeUnit(u) },
        KUnitPrefix.GIGA to { n, u -> n giga KDistanceVolumeUnit(u) },
        KUnitPrefix.MEGA to { n, u -> n mega KDistanceVolumeUnit(u) },
        KUnitPrefix.KILO to { n, u -> n kilo KDistanceVolumeUnit(u) },
        KUnitPrefix.HECTO to { n, u -> n hecto KDistanceVolumeUnit(u) },
        KUnitPrefix.DECA to { n, u -> n deca KDistanceVolumeUnit(u) },
        KUnitPrefix.DECI to { n, u -> n deci KDistanceVolumeUnit(u) },
        KUnitPrefix.CENTI to { n, u -> n centi KDistanceVolumeUnit(u) },
        KUnitPrefix.MILLI to { n, u -> n milli KDistanceVolumeUnit(u) },
        KUnitPrefix.MICRO to { n, u -> n micro KDistanceVolumeUnit(u) },
        KUnitPrefix.NANO to { n, u -> n nano KDistanceVolumeUnit(u) },
        KUnitPrefix.PICO to { n, u -> n pico KDistanceVolumeUnit(u) },
        KUnitPrefix.FEMTO to { n, u -> n femto KDistanceVolumeUnit(u) },
        KUnitPrefix.ATTO to { n, u -> n atto KDistanceVolumeUnit(u) },
        KUnitPrefix.ZEPTO to { n, u -> n zepto KDistanceVolumeUnit(u) },
        KUnitPrefix.YOCTO to { n, u -> n yocto KDistanceVolumeUnit(u) },
        KUnitPrefix.RONTO to { n, u -> n ronto KDistanceVolumeUnit(u) },
        KUnitPrefix.QUECTO to { n, u -> n quecto KDistanceVolumeUnit(u) },
    )

    private fun delta(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-300)

    @Test
    fun `length prefix x unit matrix`() {
        for ((prefix, op) in lengthOps) for ((_, unit) in lengthUnitGenerators) {
            val expected = 3.0 * prefix.factor * unit.baseValue
            assertEquals(expected, op(3, unit).value, delta(expected), "$prefix x $unit")
        }
    }

    @Test
    fun `area prefix x unit matrix`() {
        for ((prefix, op) in areaOps) for ((_, unit) in areaUnitGenerators) {
            val expected = 3.0 * (prefix.factor * unit.baseValue).pow(2)
            assertEquals(expected, op(3, unit).value, delta(expected), "$prefix x $unit")
        }
    }

    @Test
    fun `volume prefix x unit matrix`() {
        for ((prefix, op) in volumeOps) for ((_, unit) in volumeUnitGenerators) {
            val expected = 3.0 * (prefix.factor * unit.baseValue).pow(3)
            assertEquals(expected, op(3, unit).value, delta(expected), "$prefix x $unit")
        }
    }

    @Test
    fun `every length prefix standalone against meter`() {
        for ((prefix, op) in lengthOps) {
            assertEquals(prefix.factor, op(1, KDistanceUnit.METER).value, delta(prefix.factor), "$prefix")
        }
    }

    @Test
    fun `every area prefix standalone against square meter`() {
        for ((prefix, op) in areaOps) {
            val expected = prefix.factor.pow(2)
            assertEquals(expected, op(1, KDistanceUnit.METER).value, delta(expected), "$prefix")
        }
    }

    @Test
    fun `every volume prefix standalone against cubic meter`() {
        for ((prefix, op) in volumeOps) {
            val expected = prefix.factor.pow(3)
            assertEquals(expected, op(1, KDistanceUnit.METER).value, delta(expected), "$prefix")
        }
    }

    @Test
    fun `kilo square meters is a square kilometer`() {
        assertEquals(1_000_000.0, (1 kilo squareMeters).value, 1e-3)
        assertEquals(1_000_000_000.0, (1 kilo cubicMeters).value, 1e-3)
    }
}
