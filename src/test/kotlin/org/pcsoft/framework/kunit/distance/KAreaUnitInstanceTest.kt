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
import org.pcsoft.framework.kunit.*
import kotlin.math.pow
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Full behavioural matrix for [KAreaUnitInstance] (the exponent-2 distance leaf): construction/conversion,
 * every operator and every comparison, parameterized over every area unit (and every unit pair). Instances
 * come from [areaUnitGenerators] (creator properties); expected values from `unit.baseValue` squared.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KAreaUnitInstanceTest {

    /** Provider: every area unit, for the single-unit parameterized tests. */
    private fun units(): List<Arguments> = areaUnitGenerators.map { Arguments.of(it.second) }
    /** Provider: the full cross-product of every unit against every other unit, for the pairwise tests. */
    private fun unitPairs(): List<Arguments> =
        areaUnitGenerators.flatMap { (_, a) -> areaUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    /** The expected base value of `1 unit²`: the unit's linear base value squared. */
    private fun sq(u: KDistanceUnit) = u.baseValue.pow(2)

    /** Each `square…` creator builds `5 unit²`, normalizes to m², reads back exactly 5 via `valueAs`, and is exponent 2. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every area creator round trips through valueAs`(unit: KDistanceUnit) {
        val instance = mkArea(unit, 5)
        assertEquals(5.0 * sq(unit), instance.value, distanceDelta(5.0 * sq(unit)))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
        assertEquals(2, instance.exponent)
    }

    /** Converting `5 from²` into every other area unit yields `5 * sq(from) / sq(to)` — the full area conversion matrix. */
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every area converts into every other area`(from: KDistanceUnit, to: KDistanceUnit) {
        val expected = 5.0 * sq(from) / sq(to)
        assertEquals(expected, mkArea(from, 5).valueAs(to), distanceDelta(expected))
    }

    /** `area + area` for every unit pair normalizes both and returns their sum as an area (exponent 2). */
    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of areas`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KAreaUnitInstance = mkArea(a, 5) + mkArea(b, 3)
        val expected = 5.0 * sq(a) + 3.0 * sq(b)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 2)), result.toUnit().units)
    }

    /** `area - area` for every unit pair normalizes both and returns their difference as an area (exponent 2). */
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of areas`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KAreaUnitInstance = mkArea(a, 5) - mkArea(b, 3)
        val expected = 5.0 * sq(a) - 3.0 * sq(b)
        assertEquals(expected, result.value, distanceDelta(expected))
    }

    /** `area * area` yields exponent 4, which has no leaf type, so it falls back to the general [KDistanceUnitInstance]. */
    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `area times area is m4 (general distance)`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KDistanceUnitInstance = mkArea(a, 5) * mkArea(b, 3)
        val expected = (5.0 * sq(a)) * (3.0 * sq(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(4, result.exponent)
        assertTrue(result !is KLengthUnitInstance && result !is KAreaUnitInstance && result !is KVolumeUnitInstance)
    }

    /** `area / area` for every unit pair cancels the terms to a dimensionless [KMixedUnitInstance] holding the ratio. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `area div area is a dimensionless mixed unit`(a: KDistanceUnit, b: KDistanceUnit) {
        val result = mkArea(a, 5) / mkArea(b, 3)
        val expected = (5.0 * sq(a)) / (3.0 * sq(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertTrue(result.units.isEmpty())
    }

    /** All six comparison operators follow the normalized base values, across every area unit pair. */
    @ParameterizedTest(name = "{0} cmp {1}")
    @MethodSource("unitPairs")
    fun `comparison operators follow normalized values`(a: KDistanceUnit, b: KDistanceUnit) {
        val x = mkArea(a, 5)
        val y = mkArea(b, 3)
        assertEquals(x.value == y.value, x == y)
        assertEquals(x.value != y.value, x != y)
        assertEquals(x.value < y.value, x < y)
        assertEquals(x.value <= y.value, x <= y)
        assertEquals(x.value > y.value, x > y)
        assertEquals(x.value >= y.value, x >= y)
    }

    /** `toString()` with no target renders the value followed by the squared base-unit symbol (`m^2`). */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders square meters`(unit: KDistanceUnit) {
        val instance = mkArea(unit, 5)
        assertEquals("${instance.value} ${KDistanceUnit.BASE.symbol}^2", instance.toString())
    }

    /** `toString(unit)` renders the value in that unit followed by its squared symbol (`unit^2`). */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with own unit renders squared symbol`(unit: KDistanceUnit) {
        val instance = mkArea(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}^2", instance.toString(unit))
    }

    /** `toString(kilo·unit)` renders the value in the prefix-scaled target with the squared prefixed symbol (`k…^2`). */
    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with scaled target renders squared prefixed symbol`(unit: KDistanceUnit) {
        val instance = mkArea(unit, 5)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}^2", instance.toString(scaled))
    }
}
