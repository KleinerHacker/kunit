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

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KUnitTerm
import kotlin.math.pow
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Full behavioural matrix for [KVolumeUnitInstance] (the exponent-3 distance leaf): construction/conversion,
 * every operator and every comparison, parameterized over every volume unit (and every unit pair). Instances
 * come from [volumeUnitGenerators] (creator properties); expected values from `unit.baseValue` cubed.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KVolumeUnitInstanceTest {

    /** Provider: every volume unit, for the single-unit parameterized tests. */
    private fun units(): List<Arguments> = volumeUnitGenerators.map { Arguments.of(it.second) }
    /** Provider: the full cross-product of every unit against every other unit, for the pairwise tests. */
    private fun unitPairs(): List<Arguments> =
        volumeUnitGenerators.flatMap { (_, a) -> volumeUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    /** The expected base value of `1 unit³`: the unit's linear base value cubed. */
    private fun cu(u: KDistanceUnit) = u.baseValue.pow(3)

    /** Each `cubic…` creator builds `5 unit³`, normalizes to m³, reads back exactly 5 via `valueAs`, and is exponent 3. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every volume creator round trips through valueAs`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals(5.0 * cu(unit), instance.value, distanceDelta(5.0 * cu(unit)))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
        assertEquals(3, instance.exponent)
    }

    /** Converting `5 from³` into every other volume unit yields `5 * cu(from) / cu(to)` — the full volume conversion matrix. */
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every volume converts into every other volume`(from: KDistanceUnit, to: KDistanceUnit) {
        val expected = 5.0 * cu(from) / cu(to)
        assertEquals(expected, mkVolume(from, 5).valueAs(to), distanceDelta(expected))
    }

    /** `volume + volume` for every unit pair normalizes both and returns their sum as a volume (exponent 3). */
    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of volumes`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KVolumeUnitInstance = mkVolume(a, 5) + mkVolume(b, 3)
        val expected = 5.0 * cu(a) + 3.0 * cu(b)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 3)), result.toUnit().units)
    }

    /** `volume - volume` for every unit pair normalizes both and returns their difference as a volume (exponent 3). */
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of volumes`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KVolumeUnitInstance = mkVolume(a, 5) - mkVolume(b, 3)
        val expected = 5.0 * cu(a) - 3.0 * cu(b)
        assertEquals(expected, result.value, distanceDelta(expected))
    }

    /** `volume * volume` yields exponent 6, which has no leaf type, so it falls back to the general [KDistanceUnitInstance]. */
    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `volume times volume is m6 (general distance)`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KDistanceUnitInstance = mkVolume(a, 5) * mkVolume(b, 3)
        val expected = (5.0 * cu(a)) * (3.0 * cu(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(6, result.exponent)
    }

    /** `volume / volume` for every unit pair cancels the terms to a dimensionless [KMixedUnitInstance] holding the ratio. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `volume div volume is a dimensionless mixed unit`(a: KDistanceUnit, b: KDistanceUnit) {
        val result = mkVolume(a, 5) / mkVolume(b, 3)
        val expected = (5.0 * cu(a)) / (3.0 * cu(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertTrue(result.units.isEmpty())
    }

    /** All six comparison operators follow the normalized base values, across every volume unit pair. */
    @ParameterizedTest(name = "{0} cmp {1}")
    @MethodSource("unitPairs")
    fun `comparison operators follow normalized values`(a: KDistanceUnit, b: KDistanceUnit) {
        val x = mkVolume(a, 5)
        val y = mkVolume(b, 3)
        assertEquals(x.value == y.value, x == y)
        assertEquals(x.value != y.value, x != y)
        assertEquals(x.value < y.value, x < y)
        assertEquals(x.value <= y.value, x <= y)
        assertEquals(x.value > y.value, x > y)
        assertEquals(x.value >= y.value, x >= y)
    }

    /** `toString()` with no target renders the value followed by the cubed base-unit symbol (`m^3`). */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders cubic meters`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals("${instance.value} ${KDistanceUnit.BASE.symbol}^3", instance.toString())
    }

    /** `toString(unit)` renders the value in that unit followed by its cubed symbol (`unit^3`). */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with own unit renders cubed symbol`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}^3", instance.toString(unit))
    }
}
