package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnit
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private enum class NonTimeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    METER("m", 1.0)
}

/** All time-unit generator functions paired with the [KTimeUnit] they construct, shared with [KTimeUnitPrefixTest]. */
internal val timeUnitGenerators: List<Pair<(Number) -> KTimeUnitInstance, KTimeUnit>> = listOf(
    ({ n: Number -> n.seconds() }) to KTimeUnit.SECOND,
    ({ n: Number -> n.minutes() }) to KTimeUnit.MINUTE,
    ({ n: Number -> n.hours() }) to KTimeUnit.HOUR,
    ({ n: Number -> n.days() }) to KTimeUnit.DAY
)

class KTimeUnitInstanceTest {

    @Test
    fun `plus converts between same-group units`() {
        val result = 1.hours() + 30.minutes()
        assertEquals(5400.0, result.value, 1e-9)
    }

    @Test
    fun `plus works for every pair of distinct time units`() {
        val a = 1.hours() + 1.minutes()
        assertEquals(KTimeUnit.HOUR.baseValue + KTimeUnit.MINUTE.baseValue, a.value, 1e-9)

        val b = 1.days() + 1.hours()
        assertEquals(KTimeUnit.DAY.baseValue + KTimeUnit.HOUR.baseValue, b.value, 1e-9)
    }

    @Test
    fun `minus converts between same-group units`() {
        val result = 1.hours() - 30.minutes()
        assertEquals(1800.0, result.value, 1e-9)
    }

    @Test
    fun `minus works for two different units`() {
        val result = 2.hours() - 1.minutes()
        assertEquals(2 * KTimeUnit.HOUR.baseValue - KTimeUnit.MINUTE.baseValue, result.value, 1e-9)
    }

    @Test
    fun `times with KTimeUnitInstance produces second squared as KUnitInstance`() {
        val product = 3.seconds() * 4.seconds()

        assertEquals(12.0, product.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 2)), product.units)
    }

    @Test
    fun `div with KTimeUnitInstance cancels out to dimensionless KUnitInstance`() {
        val result = 10.seconds() / 2.seconds()

        assertEquals(5.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times with KUnitInstance delegates to KUnitInstance times`() {
        val perSecond = KUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.BASE, -1)))

        val result = 10.seconds() * perSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KUnitInstance delegates to KUnitInstance div`() {
        val dimensionless = KUnitInstance(2.0, listOf())

        val result = 10.seconds() / dimensionless

        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), result.units)
    }

    @Test
    fun `equals and not equals`() {
        assertTrue(1.hours() == 60.minutes())
        assertFalse(1.hours() == 59.minutes())
    }

    @Test
    fun `less than and less than or equal`() {
        assertTrue(30.minutes() < 1.hours())
        assertFalse(1.hours() < 60.minutes())
        assertTrue(1.hours() <= 60.minutes())
        assertTrue(30.minutes() <= 1.hours())
        assertFalse(1.hours() <= 30.minutes())
    }

    @Test
    fun `greater than and greater than or equal`() {
        assertTrue(1.hours() > 30.minutes())
        assertFalse(30.minutes() > 1.hours())
        assertTrue(1.hours() >= 60.minutes())
        assertTrue(1.hours() >= 30.minutes())
        assertFalse(30.minutes() >= 1.hours())
    }

    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.seconds().value, 1e-9)
        assertEquals(5.0, 5L.seconds().value, 1e-9)
        assertEquals(5.0, 5.0f.seconds().value, 1e-9)
        assertEquals(5.0, 5.0.seconds().value, 1e-9)
    }

    @Test
    fun `every generator function round trips through valueIn`() {
        for ((generator, unit) in timeUnitGenerators) {
            val instance = generator(5)
            assertEquals(5.0 * unit.baseValue, instance.value, instance.value.coerceAtLeast(1.0) * 1e-9,
                "value mismatch for $unit")
            assertEquals(5.0, instance.valueIn(unit), 5.0 * 1e-9, "valueIn round trip mismatch for $unit")
        }
    }

    @Test
    fun `valueIn converts across units`() {
        assertEquals(2.0, 2.hours().valueIn(KTimeUnit.HOUR), 1e-9)
        assertEquals(120.0, 2.hours().valueIn(KTimeUnit.MINUTE), 1e-9)
        assertEquals(1.0 / 3600.0, 1.seconds().valueIn(KTimeUnit.HOUR), 1e-12)
    }

    @Test
    fun `valueIn supports scaled unit targets`() {
        val t = 2.hours()
        assertEquals(t.value * 1000.0, t.valueIn(KUnitPrefix.MILLI with KTimeUnit.SECOND), 1e-6)
    }

    @Test
    fun `toString default uses base unit`() {
        assertEquals("5.0 s", 5.seconds().toString())
    }

    @Test
    fun `toString with target unit`() {
        assertEquals("2.0 h", 2.hours().toString(KTimeUnit.HOUR))
    }

    @Test
    fun `toString with scaled target unit`() {
        val t = 2.hours()
        val expected = "${t.value * 1000.0} ms"
        assertEquals(expected, t.toString(KUnitPrefix.MILLI with KTimeUnit.SECOND))
    }

    @Test
    fun `toKUnitInstance and toKTimeUnit round trip`() {
        val original = 2.hours()

        val roundTripped = original.toKUnitInstance().toKTimeUnit()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toKTimeUnit normalizes a non-base time unit to seconds`() {
        val twoHours = KUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.HOUR, 1)))

        assertEquals(7200.0, twoHours.toKTimeUnit().value, 1e-9)
    }

    @Test
    fun `toKTimeUnit fails for a non-time unit`() {
        val notTime = KUnitInstance(5.0, listOf(KUnitTerm(NonTimeUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { notTime.toKTimeUnit() }
    }

    @Test
    fun `toKTimeUnit fails for a mixed unit with more than one term`() {
        val notPure = KUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1), KUnitTerm(NonTimeUnit.METER, 1)))

        assertFailsWith<IllegalStateException> { notPure.toKTimeUnit() }
    }

    @Test
    fun `toKTimeUnit fails for an exponent other than one`() {
        val secondSquared = KUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 2)))

        assertFailsWith<IllegalStateException> { secondSquared.toKTimeUnit() }
    }

    @Test
    fun `construction via prefix builder round trips`() {
        val fiveMillis = (5 milli KTimeUnit.SECOND).toKUnitInstance().toKTimeUnit()
        assertEquals(0.005, fiveMillis.value, 1e-12)
    }
}
