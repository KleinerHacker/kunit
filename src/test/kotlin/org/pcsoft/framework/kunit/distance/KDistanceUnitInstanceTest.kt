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
import kotlin.math.abs
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** All length (exponent 1) creator properties paired with the [KDistanceUnit] they construct. */
internal val lengthUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.meters }) to KDistanceUnit.METER,
    ({ n: Number -> n.miles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.nauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.feet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.inches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.fathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.chains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.furlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.lightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs }) to KDistanceUnit.PARSEC
)

/** All area (exponent 2) `square…` creator properties paired with the [KDistanceUnit] they square. */
internal val areaUnitGenerators: List<Pair<(Number) -> KAreaUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.squareMeters }) to KDistanceUnit.METER,
    ({ n: Number -> n.squareMiles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.squareNauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.squareYards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.squareFeet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.squareInches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.squareFathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.squareChains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.squareFurlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.squareAstronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.squareLightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.squareLightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.squareLightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.squareLightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.squareLightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.squareLightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.squareParsecs }) to KDistanceUnit.PARSEC
)

/** All volume (exponent 3) `cubic…` creator properties paired with the [KDistanceUnit] they cube. */
internal val volumeUnitGenerators: List<Pair<(Number) -> KVolumeUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.cubicMeters }) to KDistanceUnit.METER,
    ({ n: Number -> n.cubicMiles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.cubicNauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.cubicYards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.cubicFeet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.cubicInches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.cubicFathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.cubicChains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.cubicFurlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.cubicAstronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.cubicLightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.cubicLightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.cubicLightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.cubicLightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.cubicLightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.cubicLightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.cubicParsecs }) to KDistanceUnit.PARSEC
)

/** Area derived-unit creator properties paired with the [KDerivedUnit] they construct. */
internal val areaDerivedGenerators: List<Pair<(Number) -> KAreaUnitInstance, KDerivedUnit<KDistanceUnit>>> = listOf(
    ({ n: Number -> n.ares }) to KDistanceDerivedUnit.ARE,
    ({ n: Number -> n.hectares }) to KDistanceDerivedUnit.HECTARE,
    ({ n: Number -> n.acres }) to KDistanceDerivedUnit.ACRE
)

/** Volume derived-unit creator properties paired with the [KDerivedUnit] they construct. */
internal val volumeDerivedGenerators: List<Pair<(Number) -> KVolumeUnitInstance, KDerivedUnit<KDistanceUnit>>> = listOf(
    ({ n: Number -> n.liters }) to KDistanceDerivedUnit.LITER,
    ({ n: Number -> n.usGallons }) to KDistanceDerivedUnit.US_GALLON,
    ({ n: Number -> n.imperialGallons }) to KDistanceDerivedUnit.IMPERIAL_GALLON,
    ({ n: Number -> n.usFluidOunces }) to KDistanceDerivedUnit.US_FLUID_OUNCE,
    ({ n: Number -> n.oilBarrels }) to KDistanceDerivedUnit.OIL_BARREL
)

/** Builds a length of [n] in [unit] via that unit's creator property. */
internal fun mkLength(unit: KDistanceUnit, n: Number): KLengthUnitInstance = lengthUnitGenerators.first { it.second == unit }.first(n)

/** Builds an area of [n] `square unit` via that unit's creator property. */
internal fun mkArea(unit: KDistanceUnit, n: Number): KAreaUnitInstance = areaUnitGenerators.first { it.second == unit }.first(n)

/** Builds a volume of [n] `cubic unit` via that unit's creator property. */
internal fun mkVolume(unit: KDistanceUnit, n: Number): KVolumeUnitInstance = volumeUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the enormous magnitude span (inch … parsec). */
internal fun distanceDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceUnitInstanceTest {

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
