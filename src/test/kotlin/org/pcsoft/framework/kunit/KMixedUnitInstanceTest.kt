package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.length.KLengthDerivedUnit
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.length.meters
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class KMixedUnitInstanceTest {

    @Test
    fun `times merges exponent of matching unit`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        val result = a * b

        assertEquals(15.0, result.value)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 2)), result.units)
    }

    @Test
    fun `pure wrappers are usable polymorphically as KUnitMeasurable`() {
        val measurables: List<KUnitMeasurable> = listOf(5.meters(), 2.hours())

        // value + toKMixedUnitInstance come from the KUnitMeasurable surface (via by-delegation)
        assertEquals(listOf(5.0, 7200.0), measurables.map { it.value })
        assertEquals(5.0, measurables[0].toKMixedUnitInstance().value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), measurables[1].toKMixedUnitInstance().units)
    }

    @Test
    fun `times introduces new unit with its own exponent`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val timeSquared = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, -2)))

        val result = distance * timeSquared

        assertEquals(20.0, result.value)
        assertTrue(result.hasSameUnits(KMixedUnitInstance(0.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -2)))))
    }

    @Test
    fun `times cancels out matching unit to zero exponent`() {
        val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, 1)))
        val inverseTime = KMixedUnitInstance(1.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = speed * inverseTime

        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), result.units)
    }

    @Test
    fun `times adds exponents of matching unit`() {
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 3)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 5)), result.units)
    }

    @Test
    fun `times drives exponent through zero from negative to positive`() {
        // METER^-2 * METER^3 => METER^1 (crossing the 0-point upwards)
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, -2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 3)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), result.units)
    }

    @Test
    fun `times removes term when exponents cancel to exactly zero`() {
        // METER^-2 * METER^2 => METER^0 => term removed entirely
        val a = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, -2)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))

        val result = a * b

        assertEquals(6.0, result.value)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times against a mixed unit adds exponents across the zero point`() {
        // (METER^1 * SECOND^-1) * (METER^-1 * SECOND^-1)
        //   METER:  1 + (-1) = 0   -> removed
        //   SECOND: -1 + (-1) = -2 -> kept
        val a = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, -1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = a * b

        assertEquals(20.0, result.value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, -2)), result.units)
    }

    @Test
    fun `div drives exponent through zero from positive to negative`() {
        // METER^1 / METER^3 => METER^-2 (crossing the 0-point downwards)
        val a = KMixedUnitInstance(6.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, 3)))

        val result = a / b

        assertEquals(3.0, result.value)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, -2)), result.units)
    }

    @Test
    fun `div removes term when exponents cancel to exactly zero`() {
        // METER^2 / METER^2 => METER^0 => term removed entirely
        val a = KMixedUnitInstance(8.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))

        val result = a / b

        assertEquals(4.0, result.value)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div against a mixed unit subtracts exponents across the zero point`() {
        // (METER^1 * SECOND^1) / (METER^1 * SECOND^-1)
        //   METER:  1 - 1      = 0 -> removed
        //   SECOND: 1 - (-1)   = 2 -> kept
        val a = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, 1)))
        val b = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val result = a / b

        assertEquals(5.0, result.value)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, 2)), result.units)
    }

    @Test
    fun `div subtracts exponent of matching unit`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val time = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        val speed = distance / time

        assertEquals(5.0, speed.value)
        assertEquals(setOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `div introduces new unit with negated exponent`() {
        val distance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val time = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        val speed = distance / time

        val secondTerm = speed.units.single { it.unit == KTimeUnit.SECOND }
        assertEquals(-1, secondTerm.exponent)
    }

    @Test
    fun `plus succeeds when units are identical`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertEquals(8.0, (a + b).value)
    }

    @Test
    fun `plus converts different units of the same group automatically`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        val result = a + b

        assertEquals(5.0 + 3.0 * KLengthUnit.MILE.baseValue, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), result.units)
    }

    @Test
    fun `plus fails when unit groups differ`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { a + b }
    }

    @Test
    fun `plus fails when exponents differ`() {
        val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
        val length = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { area + length }
    }

    @Test
    fun `minus succeeds when units are identical`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertEquals(2.0, (a - b).value)
    }

    @Test
    fun `minus converts different units of the same group automatically`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        val result = a - b

        assertEquals(5.0 - 3.0 * KLengthUnit.MILE.baseValue, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), result.units)
    }

    @Test
    fun `minus fails when unit groups differ`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { a - b }
    }

    @Test
    fun `equals and hashCode are structural and order independent`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1), KUnitTerm(KLengthUnit.METER, 1)))
        val c = KMixedUnitInstance(9.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun `hasSameUnits is order independent`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))
        val b = KMixedUnitInstance(9.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1), KUnitTerm(KLengthUnit.METER, 1)))

        assertTrue(a.hasSameUnits(b))
    }

    @Test
    fun `hasSameUnits is false for different exponents`() {
        val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))

        assertFalse(a.hasSameUnits(b))
    }

    @Test
    fun `valueAs converts speed to km per hour using mixed targets in any order`() {
        // 10 m / 1 s
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        val kmh1 = speedInstance.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR)
        val kmh2 = speedInstance.valueAs(KTimeUnit.HOUR, KUnitPrefix.KILO with KLengthUnit.METER)

        assertEquals(36.0, kmh1, 1e-9)
        assertEquals(36.0, kmh2, 1e-9)
    }

    @Test
    fun `valueAs throws on target count mismatch`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KLengthUnit.METER) }
    }

    @Test
    fun `valueAs throws on group mismatch`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KLengthUnit.METER, KLengthUnit.MILE) }
    }

    @Test
    fun `valueAs supports derived units for area`() {
        val area = 200.meters() * 50.meters()

        assertEquals(1.0, area.valueAs(KLengthDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `toString without targets uses raw units`() {
        val instance = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals("5.0 m*s^-1", instance.toString())
    }

    @Test
    fun `toString with targets uses converted units`() {
        val speedInstance = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KTimeUnit.SECOND, -1)))

        assertEquals("36.0 km*h^-1", speedInstance.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR))
    }

    @Test
    fun `toString with a derived unit target does not append an exponent suffix`() {
        val area = 200.meters() * 50.meters()

        assertEquals("1.0 ha", area.toString(KLengthDerivedUnit.HECTARE))
    }
}
