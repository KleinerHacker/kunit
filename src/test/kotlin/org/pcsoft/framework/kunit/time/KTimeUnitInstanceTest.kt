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

package org.pcsoft.framework.kunit.time

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KUnit
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal enum class NonTimeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    METER("m", 1.0)
}

/** All time-unit creator properties paired with the [KTimeUnit] they construct, shared across the time tests. */
internal val timeUnitGenerators: List<Pair<(Number) -> KTimeUnitInstance, KTimeUnit>> = listOf(
    ({ n: Number -> n.seconds }) to KTimeUnit.SECOND,
    ({ n: Number -> n.minutes }) to KTimeUnit.MINUTE,
    ({ n: Number -> n.hours }) to KTimeUnit.HOUR,
    ({ n: Number -> n.days }) to KTimeUnit.DAY
)

/** Builds a [KTimeUnitInstance] of [n] in [unit] via that unit's creator property (exercises the property creators). */
internal fun timeOf(unit: KTimeUnit, n: Number): KTimeUnitInstance =
    timeUnitGenerators.first { it.second == unit }.first(n)

internal fun timeDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimeUnitInstanceTest {

    private fun units(): List<Arguments> = timeUnitGenerators.map { Arguments.of(it.second) }

    private fun unitPairs(): List<Arguments> =
        timeUnitGenerators.flatMap { (_, a) -> timeUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion matrix

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every creator property round trips through valueAs`(unit: KTimeUnit) {
        val instance = timeOf(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, timeDelta(5.0 * unit.baseValue), "value mismatch for $unit")
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9, "valueAs round trip mismatch for $unit")
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every unit converts into every other unit`(from: KTimeUnit, to: KTimeUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, timeOf(from, 5).valueAs(to), timeDelta(expected), "$from -> $to mismatch")
    }

    // endregion

    // region operator matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of units`(a: KTimeUnit, b: KTimeUnit) {
        val result = timeOf(a, 5) + timeOf(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, timeDelta(expected), "$a + $b mismatch")
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of units`(a: KTimeUnit, b: KTimeUnit) {
        val result = timeOf(a, 5) - timeOf(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, timeDelta(expected), "$a - $b mismatch")
    }

    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `times combines every pair of units into second squared`(a: KTimeUnit, b: KTimeUnit) {
        val result = timeOf(a, 5) * timeOf(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, timeDelta(expected), "$a * $b mismatch")
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 2)), result.units)
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `div combines every pair of units into a dimensionless ratio`(a: KTimeUnit, b: KTimeUnit) {
        val result = timeOf(a, 5) / timeOf(b, 3)
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, timeDelta(expected), "$a / $b mismatch")
        assertTrue(result.units.isEmpty(), "$a / $b should be dimensionless")
    }

    // endregion

    // region comparison matrix (every unit against every other unit)

    @ParameterizedTest(name = "{0} == {1}")
    @MethodSource("unitPairs")
    fun `equals holds exactly when normalized values match`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av == bv, timeOf(a, 5) == timeOf(b, 3), "$a == $b mismatch")
    }

    @ParameterizedTest(name = "{0} != {1}")
    @MethodSource("unitPairs")
    fun `not equals is the negation of equals`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av != bv, timeOf(a, 5) != timeOf(b, 3), "$a != $b mismatch")
    }

    @ParameterizedTest(name = "{0} < {1}")
    @MethodSource("unitPairs")
    fun `less than follows normalized values`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av < bv, timeOf(a, 5) < timeOf(b, 3), "$a < $b mismatch")
    }

    @ParameterizedTest(name = "{0} <= {1}")
    @MethodSource("unitPairs")
    fun `less than or equal follows normalized values`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av <= bv, timeOf(a, 5) <= timeOf(b, 3), "$a <= $b mismatch")
    }

    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("unitPairs")
    fun `greater than follows normalized values`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av > bv, timeOf(a, 5) > timeOf(b, 3), "$a > $b mismatch")
    }

    @ParameterizedTest(name = "{0} >= {1}")
    @MethodSource("unitPairs")
    fun `greater than or equal follows normalized values`(a: KTimeUnit, b: KTimeUnit) {
        val av = timeOf(a, 5).value
        val bv = timeOf(b, 3).value
        assertEquals(av >= bv, timeOf(a, 5) >= timeOf(b, 3), "$a >= $b mismatch")
    }

    // endregion

    // region toString matrix (every unit)

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in the base unit`(unit: KTimeUnit) {
        val instance = timeOf(unit, 5)
        assertEquals("${instance.value} ${KTimeUnit.BASE.symbol}", instance.toString())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KTimeUnit) {
        val instance = timeOf(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    @ParameterizedTest(name = "milli {0}")
    @MethodSource("units")
    fun `toString with a scaled target renders the prefixed symbol`(unit: KTimeUnit) {
        val instance = timeOf(unit, 5)
        val scaled = KUnitPrefix.MILLI with unit
        assertEquals("${instance.valueAs(scaled)} m${unit.symbol}", instance.toString(scaled))
    }

    // endregion

    // region group-specific behaviour retained from the original suite

    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.seconds.value, 1e-9)
        assertEquals(5.0, 5L.seconds.value, 1e-9)
        assertEquals(5.0, 5.0f.seconds.value, 1e-9)
        assertEquals(5.0, 5.0.seconds.value, 1e-9)
    }

    @Test
    fun `times with KMixedUnitInstance delegates to KMixedUnitInstance times`() {
        val perSecond = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.BASE, -1)))

        val result = 10.seconds * perSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KMixedUnitInstance delegates to KMixedUnitInstance div`() {
        val dimensionless = KMixedUnitInstance(2.0, listOf())

        val result = 10.seconds / dimensionless

        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), result.units)
    }

    @Test
    fun `toKMixedUnitInstance and toKTimeUnit round trip`() {
        val original = 2.hours

        val roundTripped = original.toKMixedUnitInstance().toKTimeUnit()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toKTimeUnit normalizes a non-base time unit to seconds`() {
        val twoHours = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.HOUR, 1)))

        assertEquals(7200.0, twoHours.toKTimeUnit().value, 1e-9)
    }

    @Test
    fun `toKTimeUnit fails for a non-time unit`() {
        val notTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(NonTimeUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { notTime.toKTimeUnit() }
    }

    @Test
    fun `toKTimeUnit fails for a mixed unit with more than one term`() {
        val notPure = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1), KUnitTerm(NonTimeUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { notPure.toKTimeUnit() }
    }

    @Test
    fun `toKTimeUnit ignores the exponent since a time term stays time-typed`() {
        // The exponent is irrelevant to this conversion: a KTimeUnit term is a time-typed unit
        // regardless of exponent, and the resulting duration simply wraps the numeric value.
        val secondSquared = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 2)))

        assertEquals(5.0, secondSquared.toKTimeUnit().value, 1e-12)
    }

    // endregion
}
