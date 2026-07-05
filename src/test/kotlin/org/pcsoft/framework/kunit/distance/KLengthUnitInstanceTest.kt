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
import org.pcsoft.framework.kunit.time.KTimeUnit
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthUnitInstanceTest {

    private fun units(): List<Arguments> = lengthUnitGenerators.map { Arguments.of(it.second) }
    private fun unitPairs(): List<Arguments> =
        lengthUnitGenerators.flatMap { (_, a) -> lengthUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every length creator round trips through valueAs`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, distanceDelta(5.0 * unit.baseValue))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
        assertEquals(1, instance.exponent)
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every length converts into every other length`(from: KDistanceUnit, to: KDistanceUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, mkLength(from, 5).valueAs(to), distanceDelta(expected))
    }

    // endregion

    // region operators (length x length)

    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of lengths`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KLengthUnitInstance = mkLength(a, 5) + mkLength(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.toUnit().units)
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of lengths`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KLengthUnitInstance = mkLength(a, 5) - mkLength(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.toUnit().units)
    }

    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `length times length is an area`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KAreaUnitInstance = mkLength(a, 5) * mkLength(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(2, result.exponent)
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `length div length is a dimensionless mixed unit`(a: KDistanceUnit, b: KDistanceUnit) {
        val result = mkLength(a, 5) / mkLength(b, 3)
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertTrue(result.units.isEmpty())
    }

    // endregion

    // region comparisons (length x length)

    @ParameterizedTest(name = "{0} cmp {1}")
    @MethodSource("unitPairs")
    fun `comparison operators follow normalized values`(a: KDistanceUnit, b: KDistanceUnit) {
        val x = mkLength(a, 5)
        val y = mkLength(b, 3)
        val xv = x.value
        val yv = y.value
        assertEquals(xv == yv, x == y)
        assertEquals(xv != yv, x != y)
        assertEquals(xv < yv, x < y)
        assertEquals(xv <= yv, x <= y)
        assertEquals(xv > yv, x > y)
        assertEquals(xv >= yv, x >= y)
    }

    // endregion

    // region toString

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in meters`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals("${instance.value} ${KDistanceUnit.BASE.symbol}", instance.toString())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with a scaled target renders the prefixed symbol`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}", instance.toString(scaled))
    }

    // endregion

    // region group-specific behaviour

    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.meters.value, 1e-9)
        assertEquals(5.0, 5L.meters.value, 1e-9)
        assertEquals(5.0, 5.0f.meters.value, 1e-9)
        assertEquals(5.0, 5.0.meters.value, 1e-9)
    }

    @Test
    fun `light units are defined via the speed of light`() {
        assertEquals(299792458.0, 1.lightSeconds.value, 1e-3)
        assertEquals(60.lightSeconds.value, 1.lightMinutes.value, 1e-3)
        assertEquals(60.lightMinutes.value, 1.lightHours.value, 1.0)
        assertEquals(24.lightHours.value, 1.lightDays.value, 1.0)
        assertEquals(7.lightDays.value, 1.lightWeeks.value, 1.0)
    }

    @Test
    fun `prefix infix equals scaled creator`() {
        assertEquals((5000).meters.value, (5 kilo meters).value, 1e-9)
    }

    @Test
    fun `times with KMixedUnitInstance delegates to the engine`() {
        val perMeter = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.BASE, -1)))
        val result = 10.meters * perMeter
        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KMixedUnitInstance delegates to the engine`() {
        val two = KMixedUnitInstance(2.0, listOf())
        val result = 10.meters / two
        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.units)
    }

    @Test
    fun `toUnit and toLength round trip`() {
        val original = 5.miles
        assertEquals(original, original.toUnit().toLength())
    }

    @Test
    fun `toDistance keeps the runtime leaf type by exponent`() {
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1))).toDistance() is KLengthUnitInstance)
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2))).toDistance() is KAreaUnitInstance)
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 3))).toDistance() is KVolumeUnitInstance)
        val m4 = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 4))).toDistance()
        assertTrue(m4 !is KLengthUnitInstance && m4 !is KAreaUnitInstance && m4 !is KVolumeUnitInstance)
        assertEquals(4, m4.exponent)
    }

    @Test
    fun `toDistance normalizes a non-base unit`() {
        val fiveMiles = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1)))
        assertEquals(5.0 * KDistanceUnit.MILE.baseValue, fiveMiles.toDistance().value, 1e-6)
    }

    @Test
    fun `toLength fails for a non-distance unit`() {
        val notDistance = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))
        assertFailsWith<IllegalStateException> { notDistance.toLength() }
    }

    @Test
    fun `toLength fails for a non-1 exponent`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2)))
        assertFailsWith<IllegalStateException> { area.toLength() }
    }

    @Test
    fun `general distance type is not additive but multiplies`() {
        val m4: KDistanceUnitInstance = 2.squareMeters * 3.squareMeters // exponent 4, general type
        val product = m4 * m4 // broad times -> mixed
        assertEquals(36.0, product.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 8)), product.units)
    }

    @Test
    fun `area valueAs raises base value to the power of the exponent`() {
        // 1 square kilometer == 1_000_000 m²
        assertEquals(1.0, (1_000_000).squareMeters.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER), 1e-6)
    }

    @Test
    fun `pow helper sanity`() {
        assertEquals(1_000_000.0, 1000.0.pow(2), 1e-3)
    }

    // endregion
}
