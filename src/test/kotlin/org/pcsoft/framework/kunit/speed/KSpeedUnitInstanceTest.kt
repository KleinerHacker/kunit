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

package org.pcsoft.framework.kunit.speed

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.with
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** All speed-unit creator properties paired with the [KSpeedUnit] they construct, shared across the speed tests. */
internal val speedUnitGenerators: List<Pair<(Number) -> KSpeedUnitInstance, KSpeedUnit>> = listOf(
    ({ n: Number -> n.metersPerSecond }) to KSpeedUnit.METERS_PER_SECOND,
    ({ n: Number -> n.kilometersPerHour }) to KSpeedUnit.KILOMETERS_PER_HOUR,
    ({ n: Number -> n.milesPerHour }) to KSpeedUnit.MILES_PER_HOUR,
    ({ n: Number -> n.knots }) to KSpeedUnit.KNOT,
    ({ n: Number -> n.feetPerSecond }) to KSpeedUnit.FEET_PER_SECOND,
    ({ n: Number -> n.mach }) to KSpeedUnit.MACH,
    ({ n: Number -> n.speedOfLight }) to KSpeedUnit.LIGHT_SPEED
)

/** Builds a [KSpeedUnitInstance] of [n] in [unit] via that unit's creator property (exercises the property creators). */
internal fun speedOf(unit: KSpeedUnit, n: Number): KSpeedUnitInstance =
    speedUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the enormous magnitude span (m/s … speed of light). */
internal fun speedDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KSpeedUnitInstanceTest {

    private fun units(): List<Arguments> = speedUnitGenerators.map { Arguments.of(it.second) }

    private fun unitPairs(): List<Arguments> =
        speedUnitGenerators.flatMap { (_, a) -> speedUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion matrix

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every creator property round trips through valueAs`(unit: KSpeedUnit) {
        val instance = speedOf(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, speedDelta(5.0 * unit.baseValue), "value mismatch for $unit")
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9, "valueAs round trip mismatch for $unit")
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every unit converts into every other unit`(from: KSpeedUnit, to: KSpeedUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, speedOf(from, 5).valueAs(to), speedDelta(expected), "$from -> $to mismatch")
    }

    // endregion

    // region operator matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of units`(a: KSpeedUnit, b: KSpeedUnit) {
        val result = speedOf(a, 5) + speedOf(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, speedDelta(expected), "$a + $b mismatch")
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)), result.toUnit().units.toSet())
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of units`(a: KSpeedUnit, b: KSpeedUnit) {
        val result = speedOf(a, 5) - speedOf(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, speedDelta(expected), "$a - $b mismatch")
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)), result.toUnit().units.toSet())
    }

    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `times combines every pair of units`(a: KSpeedUnit, b: KSpeedUnit) {
        val result = speedOf(a, 5) * speedOf(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, speedDelta(expected), "$a * $b mismatch")
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -2)), result.units.toSet())
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `div combines every pair of units into a dimensionless ratio`(a: KSpeedUnit, b: KSpeedUnit) {
        val result = speedOf(a, 5) / speedOf(b, 3)
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, speedDelta(expected), "$a / $b mismatch")
        assertTrue(result.units.isEmpty(), "$a / $b should be dimensionless")
    }

    // endregion

    // region comparison matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} == {1}")
    @MethodSource("unitPairs")
    fun `equals holds exactly when normalized values match`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value == speedOf(b, 3).value, speedOf(a, 5) == speedOf(b, 3), "$a == $b mismatch")
    }

    @ParameterizedTest(name = "{0} != {1}")
    @MethodSource("unitPairs")
    fun `not equals is the negation of equals`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value != speedOf(b, 3).value, speedOf(a, 5) != speedOf(b, 3), "$a != $b mismatch")
    }

    @ParameterizedTest(name = "{0} < {1}")
    @MethodSource("unitPairs")
    fun `less than follows normalized values`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value < speedOf(b, 3).value, speedOf(a, 5) < speedOf(b, 3), "$a < $b mismatch")
    }

    @ParameterizedTest(name = "{0} <= {1}")
    @MethodSource("unitPairs")
    fun `less than or equal follows normalized values`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value <= speedOf(b, 3).value, speedOf(a, 5) <= speedOf(b, 3), "$a <= $b mismatch")
    }

    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("unitPairs")
    fun `greater than follows normalized values`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value > speedOf(b, 3).value, speedOf(a, 5) > speedOf(b, 3), "$a > $b mismatch")
    }

    @ParameterizedTest(name = "{0} >= {1}")
    @MethodSource("unitPairs")
    fun `greater than or equal follows normalized values`(a: KSpeedUnit, b: KSpeedUnit) {
        assertEquals(speedOf(a, 5).value >= speedOf(b, 3).value, speedOf(a, 5) >= speedOf(b, 3), "$a >= $b mismatch")
    }

    // endregion

    // region toString matrix (every unit)

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in the base unit`(unit: KSpeedUnit) {
        val instance = speedOf(unit, 5)
        assertEquals("${instance.value} ${KSpeedUnit.BASE.symbol}", instance.toString())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KSpeedUnit) {
        val instance = speedOf(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with a length-per-time pair target renders the composed symbol`(unit: KSpeedUnit) {
        val instance = speedOf(unit, 5)
        val kmh = instance.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)
        assertEquals("$kmh km*h^-1", instance.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR))
    }

    // endregion

    // region concrete conversions and group-specific behaviour

    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(10.0, 10.metersPerSecond.value, 1e-9)
        assertEquals(10.0, 10L.metersPerSecond.value, 1e-9)
        assertEquals(10.0, 10.0f.metersPerSecond.value, 1e-9)
        assertEquals(10.0, 10.0.metersPerSecond.value, 1e-9)
    }

    @Test
    fun `kilometers per hour convert to meters per second`() {
        assertEquals(10.0, 36.kilometersPerHour.value, 1e-9)
        assertEquals(36.0, 10.metersPerSecond.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR), 1e-9)
    }

    @Test
    fun `mach and speed of light carry their reference magnitudes`() {
        assertEquals(340.29, 1.mach.value, 1e-9)
        assertEquals(299792458.0, 1.speedOfLight.value, 1e-3)
    }

    @Test
    fun `valueAs as a length per time pair works in any order`() {
        val v = 10.metersPerSecond
        assertEquals(36.0, v.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR), 1e-9)
        assertEquals(36.0, v.valueAs(KTimeUnit.HOUR, KUnitPrefix.KILO with KDistanceUnit.METER), 1e-9)
    }

    @Test
    fun `toUnit and toSpeed round trip`() {
        val original = 50.kilometersPerHour

        val roundTripped = original.toUnit().toSpeed()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toSpeed normalizes non-base length and time terms`() {
        // a raw [MILE^1, HOUR^-1] instance == miles per hour
        val mph = KMixedUnitInstance(60.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1), KUnitTerm(KTimeUnit.HOUR, -1)))

        assertEquals(60.0, mph.toSpeed().valueAs(KSpeedUnit.MILES_PER_HOUR), 1e-9)
    }

    @Test
    fun `toSpeed fails for a non-speed mixed unit`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2)))

        assertFailsWith<IllegalStateException> { area.toSpeed() }
    }

    @Test
    fun `toSpeed fails when the length exponent is not one`() {
        // an "area per time" (m^2 / s) is not a speed
        val areaPerTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -1)))

        assertFailsWith<IllegalStateException> { areaPerTime.toSpeed() }
    }

    @Test
    fun `toSpeed fails when the time exponent is not minus one`() {
        val lengthTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, 1)))

        assertFailsWith<IllegalStateException> { lengthTime.toSpeed() }
    }

    // endregion
}
