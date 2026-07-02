package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** All length-unit generator functions paired with the [LengthUnit] they construct, shared with [LengthUnitPrefixTest]. */
internal val lengthUnitGenerators: List<Pair<(Number) -> LengthUnitInstance, LengthUnit>> = listOf(
    ({ n: Number -> n.meters() }) to LengthUnit.METER,
    ({ n: Number -> n.miles() }) to LengthUnit.MILE,
    ({ n: Number -> n.nauticalMiles() }) to LengthUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards() }) to LengthUnit.YARD,
    ({ n: Number -> n.feet() }) to LengthUnit.FOOT,
    ({ n: Number -> n.inches() }) to LengthUnit.INCH,
    ({ n: Number -> n.fathoms() }) to LengthUnit.FATHOM,
    ({ n: Number -> n.chains() }) to LengthUnit.CHAIN,
    ({ n: Number -> n.furlongs() }) to LengthUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits() }) to LengthUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightYears() }) to LengthUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs() }) to LengthUnit.PARSEC
)

class LengthUnitInstanceTest {

    @Test
    fun `plus converts between same-group units`() {
        val result = 1.kilometers() + 500.meters()
        assertEquals(1500.0, result.value, 1e-9)
    }

    private fun Number.kilometers(): LengthUnitInstance = this kilo LengthUnit.METER

    @Test
    fun `plus works for every pair of distinct length units`() {
        val a = 1.miles() + 1.yards()
        assertEquals(LengthUnit.MILE.baseValue + LengthUnit.YARD.baseValue, a.value, 1e-6)

        val b = 1.fathoms() + 1.chains()
        assertEquals(LengthUnit.FATHOM.baseValue + LengthUnit.CHAIN.baseValue, b.value, 1e-9)

        val c = 1.furlongs() + 1.feet()
        assertEquals(LengthUnit.FURLONG.baseValue + LengthUnit.FOOT.baseValue, c.value, 1e-9)

        val d = 1.lightYears() + 1.astronomicalUnits()
        assertEquals(LengthUnit.LIGHT_YEAR.baseValue + LengthUnit.ASTRONOMICAL_UNIT.baseValue, d.value, 1.0)
    }

    @Test
    fun `minus converts between same-group units`() {
        val result = 1.kilometers() - 500.meters()
        assertEquals(500.0, result.value, 1e-9)
    }

    @Test
    fun `minus works for two different units`() {
        val result = 2.miles() - 1.yards()
        assertEquals(2 * LengthUnit.MILE.baseValue - LengthUnit.YARD.baseValue, result.value, 1e-6)
    }

    @Test
    fun `times with LengthUnitInstance produces area as KUnitInstance`() {
        val area = 200.meters() * 50.meters()

        assertEquals(10_000.0, area.value, 1e-9)
        assertEquals(listOf(KUnitTerm(LengthUnit.BASE, 2)), area.units)
    }

    @Test
    fun `div with LengthUnitInstance cancels out to dimensionless KUnitInstance`() {
        val result = 10.meters() / 2.meters()

        assertEquals(5.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times with KUnitInstance delegates to KUnitInstance times`() {
        val speedPerSecond = KUnitInstance(2.0, listOf(KUnitTerm(LengthUnit.BASE, -1)))

        val result = 10.meters() * speedPerSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KUnitInstance delegates to KUnitInstance div`() {
        val time = KUnitInstance(2.0, listOf())

        val result = 10.meters() / time

        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(LengthUnit.BASE, 1)), result.units)
    }

    @Test
    fun `equals and not equals`() {
        assertTrue(1.kilometers() == 1000.meters())
        assertFalse(1.kilometers() == 999.meters())
    }

    @Test
    fun `less than and less than or equal`() {
        assertTrue(500.meters() < 1.kilometers())
        assertFalse(1.kilometers() < 1000.meters())
        assertTrue(1.kilometers() <= 1000.meters())
        assertTrue(500.meters() <= 1.kilometers())
        assertFalse(1.kilometers() <= 500.meters())
    }

    @Test
    fun `greater than and greater than or equal`() {
        assertTrue(1.kilometers() > 500.meters())
        assertFalse(500.meters() > 1.kilometers())
        assertTrue(1.kilometers() >= 1000.meters())
        assertTrue(1.kilometers() >= 500.meters())
        assertFalse(500.meters() >= 1.kilometers())
    }

    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.meters().value, 1e-9)
        assertEquals(5.0, 5L.meters().value, 1e-9)
        assertEquals(5.0, 5.0f.meters().value, 1e-9)
        assertEquals(5.0, 5.0.meters().value, 1e-9)
    }

    @Test
    fun `every generator function round trips through valueIn`() {
        for ((generator, unit) in lengthUnitGenerators) {
            val instance = generator(5)
            assertEquals(5.0 * unit.baseValue, instance.value, instance.value.coerceAtLeast(1.0) * 1e-9,
                "value mismatch for $unit")
            assertEquals(5.0, instance.valueIn(unit), 5.0 * 1e-9, "valueIn round trip mismatch for $unit")
        }
    }

    @Test
    fun `valueIn converts across units`() {
        assertEquals(5.0, 5.miles().valueIn(LengthUnit.MILE), 1e-6)
        assertEquals(LengthUnit.MILE.baseValue / LengthUnit.METER.baseValue, 1.miles().valueIn(LengthUnit.METER), 1e-6)
        assertEquals(1.0 / LengthUnit.MILE.baseValue, 1.meters().valueIn(LengthUnit.MILE), 1e-9)
    }

    @Test
    fun `valueIn supports scaled unit targets`() {
        val d = 5.miles()
        assertEquals(d.value / 1000.0, d.valueIn(KUnitPrefix.KILO with LengthUnit.METER), 1e-9)
    }

    @Test
    fun `toString default uses base unit`() {
        assertEquals("5.0 m", 5.meters().toString())
    }

    @Test
    fun `toString with target unit`() {
        assertEquals("5.0 mi", 5.miles().toString(LengthUnit.MILE))
    }

    @Test
    fun `toString with scaled target unit`() {
        val d = 5.miles()
        val expected = "${d.value / 1000.0} km"
        assertEquals(expected, d.toString(KUnitPrefix.KILO with LengthUnit.METER))
    }

    @Test
    fun `toKUnitInstance and toLengthUnit round trip`() {
        val original = 5.miles()

        val roundTripped = original.toKUnitInstance().toLengthUnit()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toLengthUnit fails for non-pure-length instance`() {
        val notPureLength = KUnitInstance(5.0, listOf(KUnitTerm(LengthUnit.BASE, 2)))

        kotlin.test.assertFailsWith<IllegalStateException> { notPureLength.toLengthUnit() }
    }

}
