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

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.time.KTimeUnit
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** All length-unit creator properties paired with the [KLengthUnit] they construct, shared across the length tests. */
internal val lengthUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KLengthUnit>> = listOf(
    ({ n: Number -> n.meters }) to KLengthUnit.METER,
    ({ n: Number -> n.miles }) to KLengthUnit.MILE,
    ({ n: Number -> n.nauticalMiles }) to KLengthUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards }) to KLengthUnit.YARD,
    ({ n: Number -> n.feet }) to KLengthUnit.FOOT,
    ({ n: Number -> n.inches }) to KLengthUnit.INCH,
    ({ n: Number -> n.fathoms }) to KLengthUnit.FATHOM,
    ({ n: Number -> n.chains }) to KLengthUnit.CHAIN,
    ({ n: Number -> n.furlongs }) to KLengthUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits }) to KLengthUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightSeconds }) to KLengthUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes }) to KLengthUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours }) to KLengthUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays }) to KLengthUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks }) to KLengthUnit.LIGHT_WEEK,
    ({ n: Number -> n.lightYears }) to KLengthUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs }) to KLengthUnit.PARSEC
)

/** All length-derived-unit creator properties paired with the [KDerivedUnit] they construct, shared across the length tests. */
internal val lengthDerivedUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KDerivedUnit<KLengthUnit>>> = listOf(
    ({ n: Number -> n.ares }) to KLengthDerivedUnit.ARE,
    ({ n: Number -> n.hectares }) to KLengthDerivedUnit.HECTARE,
    ({ n: Number -> n.acres }) to KLengthDerivedUnit.ACRE,
    ({ n: Number -> n.liters }) to KLengthDerivedUnit.LITER,
    ({ n: Number -> n.usGallons }) to KLengthDerivedUnit.US_GALLON,
    ({ n: Number -> n.imperialGallons }) to KLengthDerivedUnit.IMPERIAL_GALLON,
    ({ n: Number -> n.usFluidOunces }) to KLengthDerivedUnit.US_FLUID_OUNCE,
    ({ n: Number -> n.oilBarrels }) to KLengthDerivedUnit.OIL_BARREL
)

/** Builds a [KLengthUnitInstance] of [n] in [unit] via that unit's creator property (exercises the property creators). */
internal fun lengthOf(unit: KLengthUnit, n: Number): KLengthUnitInstance =
    lengthUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the enormous magnitude span (inch … parsec). */
internal fun lengthDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthUnitInstanceTest {

    private fun units(): List<Arguments> = lengthUnitGenerators.map { Arguments.of(it.second) }

    private fun unitPairs(): List<Arguments> =
        lengthUnitGenerators.flatMap { (_, a) -> lengthUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion matrix

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every creator property round trips through valueAs`(unit: KLengthUnit) {
        val instance = lengthOf(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, lengthDelta(5.0 * unit.baseValue), "value mismatch for $unit")
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9, "valueAs round trip mismatch for $unit")
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every unit converts into every other unit`(from: KLengthUnit, to: KLengthUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, lengthOf(from, 5).valueAs(to), lengthDelta(expected), "$from -> $to mismatch")
    }

    // endregion

    // region operator matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of units`(a: KLengthUnit, b: KLengthUnit) {
        val result = lengthOf(a, 5) + lengthOf(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, lengthDelta(expected), "$a + $b mismatch")
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), result.toKMixedUnitInstance().units)
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of units`(a: KLengthUnit, b: KLengthUnit) {
        val result = lengthOf(a, 5) - lengthOf(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, lengthDelta(expected), "$a - $b mismatch")
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), result.toKMixedUnitInstance().units)
    }

    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `times combines every pair of units into an area`(a: KLengthUnit, b: KLengthUnit) {
        val result = lengthOf(a, 5) * lengthOf(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, lengthDelta(expected), "$a * $b mismatch")
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 2)), result.units)
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `div combines every pair of units into a dimensionless ratio`(a: KLengthUnit, b: KLengthUnit) {
        val result = lengthOf(a, 5) / lengthOf(b, 3)
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, lengthDelta(expected), "$a / $b mismatch")
        assertTrue(result.units.isEmpty(), "$a / $b should be dimensionless")
    }

    // endregion

    // region comparison matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} == {1}")
    @MethodSource("unitPairs")
    fun `equals holds exactly when normalized values match`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av == bv, lengthOf(a, 5) == lengthOf(b, 3), "$a == $b mismatch")
    }

    @ParameterizedTest(name = "{0} != {1}")
    @MethodSource("unitPairs")
    fun `not equals is the negation of equals`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av != bv, lengthOf(a, 5) != lengthOf(b, 3), "$a != $b mismatch")
    }

    @ParameterizedTest(name = "{0} < {1}")
    @MethodSource("unitPairs")
    fun `less than follows normalized values`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av < bv, lengthOf(a, 5) < lengthOf(b, 3), "$a < $b mismatch")
    }

    @ParameterizedTest(name = "{0} <= {1}")
    @MethodSource("unitPairs")
    fun `less than or equal follows normalized values`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av <= bv, lengthOf(a, 5) <= lengthOf(b, 3), "$a <= $b mismatch")
    }

    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("unitPairs")
    fun `greater than follows normalized values`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av > bv, lengthOf(a, 5) > lengthOf(b, 3), "$a > $b mismatch")
    }

    @ParameterizedTest(name = "{0} >= {1}")
    @MethodSource("unitPairs")
    fun `greater than or equal follows normalized values`(a: KLengthUnit, b: KLengthUnit) {
        val av = lengthOf(a, 5).value
        val bv = lengthOf(b, 3).value
        assertEquals(av >= bv, lengthOf(a, 5) >= lengthOf(b, 3), "$a >= $b mismatch")
    }

    // endregion

    // region toString matrix (every unit)

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in the base unit`(unit: KLengthUnit) {
        val instance = lengthOf(unit, 5)
        assertEquals("${instance.value} ${KLengthUnit.BASE.symbol}", instance.toString())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KLengthUnit) {
        val instance = lengthOf(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with a scaled target renders the prefixed symbol`(unit: KLengthUnit) {
        val instance = lengthOf(unit, 5)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}", instance.toString(scaled))
    }

    // endregion

    // region error cases (representative — mixing exponents)

    @Test
    fun `plus fails for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares + 5.meters }
    }

    @Test
    fun `minus fails for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares - 5.meters }
    }

    @Test
    fun `equals throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares == 5.meters }
    }

    @Test
    fun `compareTo throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares < 5.meters }
    }

    // endregion

    // region group-specific behaviour retained from the original suite

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
        // Larger light units are exact multiples of the light-second.
        assertEquals(60.lightSeconds.value, 1.lightMinutes.value, 1e-3)
        assertEquals(60.lightMinutes.value, 1.lightHours.value, 1.0)
        assertEquals(24.lightHours.value, 1.lightDays.value, 1.0)
        assertEquals(7.lightDays.value, 1.lightWeeks.value, 1.0)
    }

    @Test
    fun `times with KMixedUnitInstance delegates to KMixedUnitInstance times`() {
        val speedPerSecond = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.BASE, -1)))

        val result = 10.meters * speedPerSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KMixedUnitInstance delegates to KMixedUnitInstance div`() {
        val time = KMixedUnitInstance(2.0, listOf())

        val result = 10.meters / time

        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), result.units)
    }

    @Test
    fun `valueAs supports scaled unit targets`() {
        val d = 5.miles
        assertEquals(d.value / 1000.0, d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER), 1e-9)
    }

    @Test
    fun `toKMixedUnitInstance and toKLengthUnit round trip`() {
        val original = 5.miles

        val roundTripped = original.toKMixedUnitInstance().toKLengthUnit()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toKLengthUnit succeeds for any exponent of the length base unit`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 2)))

        assertEquals(5.0, area.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `toKLengthUnit normalizes a non-base length unit to the base unit`() {
        val fiveMiles = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        assertEquals(5.0 * KLengthUnit.MILE.baseValue, fiveMiles.toKLengthUnit().value, 1e-6)
    }

    @Test
    fun `toKLengthUnit fails for a non-length unit`() {
        val notLength = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { notLength.toKLengthUnit() }
    }

    @Test
    fun `toKLengthUnit fails for a mixed unit with more than one term`() {
        val notPure = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KLengthUnit.BASE, 2)))

        assertFailsWith<IllegalStateException> { notPure.toKLengthUnit() }
    }

    // endregion
}
