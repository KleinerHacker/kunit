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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KVolumeUnitInstanceTest {

    private fun units(): List<Arguments> = volumeUnitGenerators.map { Arguments.of(it.second) }
    private fun unitPairs(): List<Arguments> =
        volumeUnitGenerators.flatMap { (_, a) -> volumeUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    private fun cu(u: KDistanceUnit) = u.baseValue.pow(3)

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every volume creator round trips through valueAs`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals(5.0 * cu(unit), instance.value, distanceDelta(5.0 * cu(unit)))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
        assertEquals(3, instance.exponent)
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every volume converts into every other volume`(from: KDistanceUnit, to: KDistanceUnit) {
        val expected = 5.0 * cu(from) / cu(to)
        assertEquals(expected, mkVolume(from, 5).valueAs(to), distanceDelta(expected))
    }

    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of volumes`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KVolumeUnitInstance = mkVolume(a, 5) + mkVolume(b, 3)
        val expected = 5.0 * cu(a) + 3.0 * cu(b)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 3)), result.toUnit().units)
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of volumes`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KVolumeUnitInstance = mkVolume(a, 5) - mkVolume(b, 3)
        val expected = 5.0 * cu(a) - 3.0 * cu(b)
        assertEquals(expected, result.value, distanceDelta(expected))
    }

    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `volume times volume is m6 (general distance)`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KDistanceUnitInstance = mkVolume(a, 5) * mkVolume(b, 3)
        val expected = (5.0 * cu(a)) * (3.0 * cu(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(6, result.exponent)
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `volume div volume is a dimensionless mixed unit`(a: KDistanceUnit, b: KDistanceUnit) {
        val result = mkVolume(a, 5) / mkVolume(b, 3)
        val expected = (5.0 * cu(a)) / (3.0 * cu(b))
        assertEquals(expected, result.value, distanceDelta(expected))
        assertTrue(result.units.isEmpty())
    }

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

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders cubic meters`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals("${instance.value} ${KDistanceUnit.BASE.symbol}^3", instance.toString())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with own unit renders cubed symbol`(unit: KDistanceUnit) {
        val instance = mkVolume(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}^3", instance.toString(unit))
    }
}
