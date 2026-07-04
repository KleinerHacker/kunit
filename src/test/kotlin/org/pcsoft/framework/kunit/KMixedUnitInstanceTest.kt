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

package org.pcsoft.framework.kunit

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.distance.KDistanceDerivedUnit
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.mkLength
import org.pcsoft.framework.kunit.distance.lengthUnitGenerators
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.timeOf
import org.pcsoft.framework.kunit.time.timeUnitGenerators
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMixedUnitInstanceTest {

    /** Every combination of a length unit with a time unit, for the cross-group mixed-unit matrices. */
    private fun lengthTimePairs(): List<Arguments> =
        lengthUnitGenerators.flatMap { (_, l) -> timeUnitGenerators.map { (_, t) -> Arguments.of(l, t) } }

    @ParameterizedTest(name = "{0} per {1}")
    @MethodSource("lengthTimePairs")
    fun `dividing every length by every time yields the expected speed term`(length: KDistanceUnit, time: KTimeUnit) {
        val speed = mkLength(length, 10) / timeOf(time, 2).toUnit()

        val expected = 10.0 * length.baseValue / (2.0 * time.baseValue)
        assertEquals(expected, speed.value, (kotlin.math.abs(expected) * 1e-9).coerceAtLeast(1e-12), "$length / $time mismatch")
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)), speed.units.toSet())
    }

    @ParameterizedTest(name = "{0} times {1}")
    @MethodSource("lengthTimePairs")
    fun `multiplying every length by every time yields the expected mixed term`(length: KDistanceUnit, time: KTimeUnit) {
        val product = mkLength(length, 10) * timeOf(time, 2).toUnit()

        val expected = 10.0 * length.baseValue * (2.0 * time.baseValue)
        assertEquals(expected, product.value, (kotlin.math.abs(expected) * 1e-9).coerceAtLeast(1e-12), "$length * $time mismatch")
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, 1)), product.units.toSet())
    }

    @Test
    fun `times merges exponent of matching unit`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))

        val result = a * b

        assertEquals(15.0, result.value)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 2)), result.units)
    }

    @Test
    fun `pure wrappers are usable polymorphically as KUnitMeasurable`() {
        val measurables: List<KUnitMeasurable> = listOf(5.meters, 2.hours)

        // value + toUnit come from the KUnitMeasurable surface (via by-delegation)
        assertEquals(listOf(5.0, 7200.0), measurables.map { it.value })
        assertEquals(5.0, measurables[0].toUnit().value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), measurables[1].toUnit().units)
    }

    @Test
    fun `times introduces new unit with its own exponent`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val timeSquared = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, -2)))

        val result = distance * timeSquared

        assertEquals(20.0, result.value)
        assertTrue(result.hasSameUnits(KMixedUnitInstance(0.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -2)))))
    }

    @Test
    fun `times cancels out matching unit to zero exponent`() {
        val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, 1)))
        val inverseTime = KMixedUnitInstance(1.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = speed * inverseTime

        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 1)), result.units)
    }

    @Test
    fun `times adds exponents of matching unit`() {
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 3)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 5)), result.units)
    }

    @Test
    fun `times drives exponent through zero from negative to positive`() {
        // METER^-2 * METER^3 => METER^1 (crossing the 0-point upwards)
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, -2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 3)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 1)), result.units)
    }

    @Test
    fun `times removes term when exponents cancel to exactly zero`() {
        // METER^-2 * METER^2 => METER^0 => term removed entirely
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, -2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times against a mixed unit adds exponents across the zero point`() {
        // (METER^1 * SECOND^-1) * (METER^-1 * SECOND^-1)
        //   METER:  1 + (-1) = 0   -> removed
        //   SECOND: -1 + (-1) = -2 -> kept
        val a = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, -1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = a * b

        assertEquals(20.0, result.value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, -2)), result.units)
    }

    @Test
    fun `div drives exponent through zero from positive to negative`() {
        // METER^1 / METER^3 => METER^-2 (crossing the 0-point downwards)
        val a = KMixedUnitInstance(6.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, 3)))

        val result = a / b

        assertEquals(3.0, result.value)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, -2)), result.units)
    }

    @Test
    fun `div removes term when exponents cancel to exactly zero`() {
        // METER^2 / METER^2 => METER^0 => term removed entirely
        val a = KMixedUnitInstance(8.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))

        val result = a / b

        assertEquals(4.0, result.value)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div against a mixed unit subtracts exponents across the zero point`() {
        // (METER^1 * SECOND^1) / (METER^1 * SECOND^-1)
        //   METER:  1 - 1      = 0 -> removed
        //   SECOND: 1 - (-1)   = 2 -> kept
        val a = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, 1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = a / b

        assertEquals(5.0, result.value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, 2)), result.units)
    }

    @Test
    fun `div subtracts exponent of matching unit`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val time = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        val speed = distance / time

        assertEquals(5.0, speed.value)
        assertEquals(setOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `div introduces new unit with negated exponent`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val time = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        val speed = distance / time

        val secondTerm = speed.units.single { it.unit == KTimeUnit.SECOND }
        assertEquals(-1, secondTerm.exponent)
    }

    @Test
    fun `plus succeeds when units are identical`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))

        assertEquals(8.0, (a + b).value)
    }

    @Test
    fun `plus converts different units of the same group automatically`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1)))

        val result = a + b

        assertEquals(5.0 + 3.0 * KDistanceUnit.MILE.baseValue, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 1)), result.units)
    }

    @Test
    fun `plus fails when unit groups differ`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { a + b }
    }

    @Test
    fun `plus fails when exponents differ`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))
        val length = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { area + length }
    }

    @Test
    fun `minus succeeds when units are identical`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))

        assertEquals(2.0, (a - b).value)
    }

    @Test
    fun `minus converts different units of the same group automatically`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1)))

        val result = a - b

        assertEquals(5.0 - 3.0 * KDistanceUnit.MILE.baseValue, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KDistanceUnit.METER, 1)), result.units)
    }

    @Test
    fun `minus fails when unit groups differ`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { a - b }
    }

    @Test
    fun `equals and hashCode are structural and order independent`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1), KUnitTerm(KDistanceUnit.METER, 1)))
        val c = KMixedUnitInstance(9.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun `hasSameUnits is order independent`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(9.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1), KUnitTerm(KDistanceUnit.METER, 1)))

        assertTrue(a.hasSameUnits(b))
    }

    @Test
    fun `hasSameUnits is false for different exponents`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
        val b = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))

        assertFalse(a.hasSameUnits(b))
    }

    @Test
    fun `valueAs converts speed to km per hour using mixed targets in any order`() {
        // 10 m / 1 s
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val kmh1 = speedInstance.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)
        val kmh2 = speedInstance.valueAs(KTimeUnit.HOUR, KUnitPrefix.KILO with KDistanceUnit.METER)

        assertEquals(36.0, kmh1, 1e-9)
        assertEquals(36.0, kmh2, 1e-9)
    }

    @Test
    fun `valueAs throws on target count mismatch`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KDistanceUnit.METER) }
    }

    @Test
    fun `valueAs throws on group mismatch`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KDistanceUnit.METER, KDistanceUnit.MILE) }
    }

    @Test
    fun `valueAs supports derived units for area`() {
        val area = 200.meters * 50.meters

        assertEquals(1.0, area.valueAs(KDistanceDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `toString without targets uses raw units`() {
        val instance = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals("5.0 m*s^-1", instance.toString())
    }

    @Test
    fun `toString with targets uses converted units`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals("36.0 km*h^-1", speedInstance.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR))
    }

    @Test
    fun `toString with a derived unit target does not append an exponent suffix`() {
        val area = 200.meters * 50.meters

        assertEquals("1.0 ha", area.toString(KDistanceDerivedUnit.HECTARE))
    }
}
