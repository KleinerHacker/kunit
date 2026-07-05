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

/**
 * Full behavioural matrix for [KLengthUnitInstance] (the exponent-1 distance leaf): construction/conversion,
 * every operator and every comparison, parameterized over every length unit (and every unit pair). Instances
 * come from [lengthUnitGenerators] (creator properties); expected values from `unit.baseValue`.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KLengthUnitInstanceTest {

    /** Provider: every length unit, for the single-unit parameterized tests. */
    private fun units(): List<Arguments> = lengthUnitGenerators.map { Arguments.of(it.second) }
    /** Provider: the full cross-product of every unit against every other unit, for the pairwise tests. */
    private fun unitPairs(): List<Arguments> =
        lengthUnitGenerators.flatMap { (_, a) -> lengthUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion

    /** Each length creator builds `5 unit`, normalizes to metres, reads back exactly 5 via `valueAs`, and is exponent 1. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every length creator round trips through valueAs`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, distanceDelta(5.0 * unit.baseValue))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
        assertEquals(1, instance.exponent)
    }

    /** Converting `5 from` into every other length unit yields `5 * from.baseValue / to.baseValue` — the full conversion matrix. */
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every length converts into every other length`(from: KDistanceUnit, to: KDistanceUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, mkLength(from, 5).valueAs(to), distanceDelta(expected))
    }

    // endregion

    // region operators (length x length)

    /** `length + length` for every unit pair normalizes both and returns their sum as a length (exponent 1). */
    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of lengths`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KLengthUnitInstance = mkLength(a, 5) + mkLength(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.toUnit().units)
    }

    /** `length - length` for every unit pair normalizes both and returns their difference as a length (exponent 1). */
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of lengths`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KLengthUnitInstance = mkLength(a, 5) - mkLength(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.toUnit().units)
    }

    /** `length * length` for every unit pair returns a statically typed [KAreaUnitInstance] (exponent 2) with the product value. */
    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `length times length is an area`(a: KDistanceUnit, b: KDistanceUnit) {
        val result: KAreaUnitInstance = mkLength(a, 5) * mkLength(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, distanceDelta(expected))
        assertEquals(2, result.exponent)
    }

    /** `length / length` for every unit pair cancels the terms to a dimensionless [KMixedUnitInstance] holding the ratio. */
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

    /** All six comparison operators (`==`, `!=`, `<`, `<=`, `>`, `>=`) follow the normalized base values, across every unit pair. */
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

    /** `toString()` with no target renders the normalized value followed by the metre symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in meters`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals("${instance.value} ${KDistanceUnit.BASE.symbol}", instance.toString())
    }

    /** `toString(unit)` renders the value converted into that unit followed by the unit's own symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    /** `toString(kilo·unit)` renders the value in the prefix-scaled target with the prefixed symbol (`k…`). */
    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with a scaled target renders the prefixed symbol`(unit: KDistanceUnit) {
        val instance = mkLength(unit, 5)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}", instance.toString(scaled))
    }

    // endregion

    // region group-specific behaviour

    /** The creator property accepts any `Number` (`Int`/`Long`/`Float`/`Double`) and normalizes to the same Double value. */
    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.meters.value, 1e-9)
        assertEquals(5.0, 5L.meters.value, 1e-9)
        assertEquals(5.0, 5.0f.meters.value, 1e-9)
        assertEquals(5.0, 5.0.meters.value, 1e-9)
    }

    /** The light-based distance units are defined consistently off the speed of light (light-second, -minute, -hour, -day, -week chain). */
    @Test
    fun `light units are defined via the speed of light`() {
        assertEquals(299792458.0, 1.lightSeconds.value, 1e-3)
        assertEquals(60.lightSeconds.value, 1.lightMinutes.value, 1e-3)
        assertEquals(60.lightMinutes.value, 1.lightHours.value, 1.0)
        assertEquals(24.lightHours.value, 1.lightDays.value, 1.0)
        assertEquals(7.lightDays.value, 1.lightWeeks.value, 1.0)
    }

    /** The prefix `infix` form `5 kilo meters` is exactly equivalent to the scaled creator `5000.meters`. */
    @Test
    fun `prefix infix equals scaled creator`() {
        assertEquals((5000).meters.value, (5 kilo meters).value, 1e-9)
    }

    /** Multiplying a length by a raw [KMixedUnitInstance] delegates to the mixed engine (here `m * m⁻¹` cancels to a dimensionless value). */
    @Test
    fun `times with KMixedUnitInstance delegates to the engine`() {
        val perMeter = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.BASE, -1)))
        val result = 10.meters * perMeter
        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    /** Dividing a length by a scalar [KMixedUnitInstance] delegates to the mixed engine and keeps the metre term. */
    @Test
    fun `div with KMixedUnitInstance delegates to the engine`() {
        val two = KMixedUnitInstance(2.0, listOf())
        val result = 10.meters / two
        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 1)), result.units)
    }

    /** A length decomposed via `toUnit()` and recomposed via `toLength()` equals the original. */
    @Test
    fun `toUnit and toLength round trip`() {
        val original = 5.miles
        assertEquals(original, original.toUnit().toLength())
    }

    /** `toDistance()` returns the leaf type matching the exponent (1→length, 2→area, 3→volume) and the general base for exponent 4. */
    @Test
    fun `toDistance keeps the runtime leaf type by exponent`() {
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1))).toDistance() is KLengthUnitInstance)
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2))).toDistance() is KAreaUnitInstance)
        assertTrue(KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 3))).toDistance() is KVolumeUnitInstance)
        val m4 = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 4))).toDistance()
        assertTrue(m4 !is KLengthUnitInstance && m4 !is KAreaUnitInstance && m4 !is KVolumeUnitInstance)
        assertEquals(4, m4.exponent)
    }

    /** `toDistance()` normalizes a non-base unit term (e.g. miles) to metres before wrapping it. */
    @Test
    fun `toDistance normalizes a non-base unit`() {
        val fiveMiles = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1)))
        assertEquals(5.0 * KDistanceUnit.MILE.baseValue, fiveMiles.toDistance().value, 1e-6)
    }

    /** `toLength()` on a non-distance term (e.g. a second) throws `IllegalStateException`. */
    @Test
    fun `toLength fails for a non-distance unit`() {
        val notDistance = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))
        assertFailsWith<IllegalStateException> { notDistance.toLength() }
    }

    /** `toLength()` on a distance term whose exponent is not 1 (e.g. an area) throws `IllegalStateException`. */
    @Test
    fun `toLength fails for a non-1 exponent`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2)))
        assertFailsWith<IllegalStateException> { area.toLength() }
    }

    /** The general (exponent-4) distance type has no `+`, but `*` still works and falls back to a raw mixed unit (exponent 8). */
    @Test
    fun `general distance type is not additive but multiplies`() {
        val m4: KDistanceUnitInstance = (2.meters * 1.meters) * (3.meters * 1.meters) // 2 m² * 3 m² = exponent 4, general type
        val product = m4 * m4 // broad times -> mixed
        assertEquals(36.0, product.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 8)), product.units)
    }

    /** For an area, `valueAs` raises the target unit's base value to the exponent (1 km² == 1 000 000 m²). */
    @Test
    fun `area valueAs raises base value to the power of the exponent`() {
        // 1 square kilometer == 1_000_000 m²
        assertEquals(1.0, (1_000_000.meters * 1.meters).valueAs(KUnitPrefix.KILO with KDistanceUnit.METER), 1e-6)
    }

    /** Sanity check on the `kotlin.math.pow` helper used by the expected-value computations (1000² == 1e6). */
    @Test
    fun `pow helper sanity`() {
        assertEquals(1_000_000.0, 1000.0.pow(2), 1e-3)
    }

    // endregion
}
