package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.length.KLengthDerivedUnit
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.length.meters
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private enum class TimeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    SECOND("s", 1.0),
    HOUR("h", 3600.0)
}

class KUnitInstanceTest {

    @Test
    fun `times merges exponent of matching unit`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        val result = a * b

        assertEquals(15.0, result.value)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 2)), result.units)
    }

    @Test
    fun `times introduces new unit with its own exponent`() {
        val distance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val timeSquared = KUnitInstance(2.0, listOf(KUnitTerm(TimeUnit.SECOND, -2)))

        val result = distance * timeSquared

        assertEquals(20.0, result.value)
        assertTrue(result.hasSameUnits(KUnitInstance(0.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -2)))))
    }

    @Test
    fun `times cancels out matching unit to zero exponent`() {
        val speed = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, 1)))
        val inverseTime = KUnitInstance(1.0, listOf(KUnitTerm(TimeUnit.SECOND, -1)))

        val result = speed * inverseTime

        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), result.units)
    }

    @Test
    fun `div subtracts exponent of matching unit`() {
        val distance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val time = KUnitInstance(2.0, listOf(KUnitTerm(TimeUnit.SECOND, 1)))

        val speed = distance / time

        assertEquals(5.0, speed.value)
        assertEquals(setOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `div introduces new unit with negated exponent`() {
        val distance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val time = KUnitInstance(2.0, listOf(KUnitTerm(TimeUnit.SECOND, 1)))

        val speed = distance / time

        val secondTerm = speed.units.single { it.unit == TimeUnit.SECOND }
        assertEquals(-1, secondTerm.exponent)
    }

    @Test
    fun `plus succeeds when units are identical`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertEquals(8.0, (a + b).value)
    }

    @Test
    fun `plus fails when units differ`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        assertFailsWith<IllegalStateException> { a + b }
    }

    @Test
    fun `plus fails when exponents differ`() {
        val area = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
        val length = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { area + length }
    }

    @Test
    fun `minus succeeds when units are identical`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))

        assertEquals(2.0, (a - b).value)
    }

    @Test
    fun `minus fails when units differ`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        assertFailsWith<IllegalStateException> { a - b }
    }

    @Test
    fun `equals and hashCode are structural and order independent`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))
        val b = KUnitInstance(5.0, listOf(KUnitTerm(TimeUnit.SECOND, -1), KUnitTerm(KLengthUnit.METER, 1)))
        val c = KUnitInstance(9.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun `hasSameUnits is order independent`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))
        val b = KUnitInstance(9.0, listOf(KUnitTerm(TimeUnit.SECOND, -1), KUnitTerm(KLengthUnit.METER, 1)))

        assertTrue(a.hasSameUnits(b))
    }

    @Test
    fun `hasSameUnits is false for different exponents`() {
        val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
        val b = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))

        assertFalse(a.hasSameUnits(b))
    }

    @Test
    fun `valueAs converts speed to km per hour using mixed targets in any order`() {
        // 10 m / 1 s
        val speedInstance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        val kmh1 = speedInstance.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR)
        val kmh2 = speedInstance.valueAs(TimeUnit.HOUR, KUnitPrefix.KILO with KLengthUnit.METER)

        assertEquals(36.0, kmh1, 1e-9)
        assertEquals(36.0, kmh2, 1e-9)
    }

    @Test
    fun `valueAs throws on target count mismatch`() {
        val speedInstance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KLengthUnit.METER) }
    }

    @Test
    fun `valueAs throws on group mismatch`() {
        val speedInstance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        assertFailsWith<IllegalStateException> { speedInstance.valueAs(KLengthUnit.METER, KLengthUnit.MILE) }
    }

    @Test
    fun `valueAs supports derived units for area`() {
        val area = 200.meters() * 50.meters()

        assertEquals(1.0, area.valueAs(KLengthDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `toString without targets uses raw units`() {
        val instance = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        assertEquals("5.0 m*s^-1", instance.toString())
    }

    @Test
    fun `toString with targets uses converted units`() {
        val speedInstance = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))

        assertEquals("36.0 km*h^-1", speedInstance.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR))
    }

    @Test
    fun `toString with a derived unit target does not append an exponent suffix`() {
        val area = 200.meters() * 50.meters()

        assertEquals("1.0 ha", area.toString(KLengthDerivedUnit.HECTARE))
    }
}
