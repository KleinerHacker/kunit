package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KDerivedUnit
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** All length-unit generator functions paired with the [KLengthUnit] they construct, shared with [KLengthUnitPrefixTest]. */
internal val lengthUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KLengthUnit>> = listOf(
    ({ n: Number -> n.meters() }) to KLengthUnit.METER,
    ({ n: Number -> n.miles() }) to KLengthUnit.MILE,
    ({ n: Number -> n.nauticalMiles() }) to KLengthUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards() }) to KLengthUnit.YARD,
    ({ n: Number -> n.feet() }) to KLengthUnit.FOOT,
    ({ n: Number -> n.inches() }) to KLengthUnit.INCH,
    ({ n: Number -> n.fathoms() }) to KLengthUnit.FATHOM,
    ({ n: Number -> n.chains() }) to KLengthUnit.CHAIN,
    ({ n: Number -> n.furlongs() }) to KLengthUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits() }) to KLengthUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightYears() }) to KLengthUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs() }) to KLengthUnit.PARSEC
)

/** All length-derived-unit generator functions paired with the [KDerivedUnit] they construct, shared with [KLengthUnitPrefixTest]. */
internal val lengthDerivedUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KDerivedUnit<KLengthUnit>>> = listOf(
    ({ n: Number -> n.ares() }) to KLengthDerivedUnit.ARE,
    ({ n: Number -> n.hectares() }) to KLengthDerivedUnit.HECTARE,
    ({ n: Number -> n.acres() }) to KLengthDerivedUnit.ACRE,
    ({ n: Number -> n.liters() }) to KLengthDerivedUnit.LITER,
    ({ n: Number -> n.usGallons() }) to KLengthDerivedUnit.US_GALLON,
    ({ n: Number -> n.imperialGallons() }) to KLengthDerivedUnit.IMPERIAL_GALLON,
    ({ n: Number -> n.usFluidOunces() }) to KLengthDerivedUnit.US_FLUID_OUNCE,
    ({ n: Number -> n.oilBarrels() }) to KLengthDerivedUnit.OIL_BARREL
)

class KLengthUnitInstanceTest {

    @Test
    fun `plus converts between same-group units`() {
        val result = 1.kilometers() + 500.meters()
        assertEquals(1500.0, result.value, 1e-9)
    }

    private fun Number.kilometers(): KLengthUnitInstance = (this kilo KLengthUnit.METER).toKUnitInstance().toKLengthUnit()

    @Test
    fun `plus works for every pair of distinct length units`() {
        val a = 1.miles() + 1.yards()
        assertEquals(KLengthUnit.MILE.baseValue + KLengthUnit.YARD.baseValue, a.value, 1e-6)

        val b = 1.fathoms() + 1.chains()
        assertEquals(KLengthUnit.FATHOM.baseValue + KLengthUnit.CHAIN.baseValue, b.value, 1e-9)

        val c = 1.furlongs() + 1.feet()
        assertEquals(KLengthUnit.FURLONG.baseValue + KLengthUnit.FOOT.baseValue, c.value, 1e-9)

        val d = 1.lightYears() + 1.astronomicalUnits()
        assertEquals(KLengthUnit.LIGHT_YEAR.baseValue + KLengthUnit.ASTRONOMICAL_UNIT.baseValue, d.value, 1.0)
    }

    @Test
    fun `minus converts between same-group units`() {
        val result = 1.kilometers() - 500.meters()
        assertEquals(500.0, result.value, 1e-9)
    }

    @Test
    fun `minus works for two different units`() {
        val result = 2.miles() - 1.yards()
        assertEquals(2 * KLengthUnit.MILE.baseValue - KLengthUnit.YARD.baseValue, result.value, 1e-6)
    }

    @Test
    fun `times with KLengthUnitInstance produces area as KUnitInstance`() {
        val area = 200.meters() * 50.meters()

        assertEquals(10_000.0, area.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 2)), area.units)
    }

    @Test
    fun `div with KLengthUnitInstance cancels out to dimensionless KUnitInstance`() {
        val result = 10.meters() / 2.meters()

        assertEquals(5.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times with KUnitInstance delegates to KUnitInstance times`() {
        val speedPerSecond = KUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.BASE, -1)))

        val result = 10.meters() * speedPerSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KUnitInstance delegates to KUnitInstance div`() {
        val time = KUnitInstance(2.0, listOf())

        val result = 10.meters() / time

        assertEquals(5.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), result.units)
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
        assertEquals(5.0, 5.miles().valueIn(KLengthUnit.MILE), 1e-6)
        assertEquals(KLengthUnit.MILE.baseValue / KLengthUnit.METER.baseValue, 1.miles().valueIn(KLengthUnit.METER), 1e-6)
        assertEquals(1.0 / KLengthUnit.MILE.baseValue, 1.meters().valueIn(KLengthUnit.MILE), 1e-9)
    }

    @Test
    fun `valueIn supports scaled unit targets`() {
        val d = 5.miles()
        assertEquals(d.value / 1000.0, d.valueIn(KUnitPrefix.KILO with KLengthUnit.METER), 1e-9)
    }

    @Test
    fun `toString default uses base unit`() {
        assertEquals("5.0 m", 5.meters().toString())
    }

    @Test
    fun `toString with target unit`() {
        assertEquals("5.0 mi", 5.miles().toString(KLengthUnit.MILE))
    }

    @Test
    fun `toString with scaled target unit`() {
        val d = 5.miles()
        val expected = "${d.value / 1000.0} km"
        assertEquals(expected, d.toString(KUnitPrefix.KILO with KLengthUnit.METER))
    }

    @Test
    fun `toKUnitInstance and toKLengthUnit round trip`() {
        val original = 5.miles()

        val roundTripped = original.toKUnitInstance().toKLengthUnit()

        assertEquals(original, roundTripped)
    }

    @Test
    fun `toKLengthUnit succeeds for any exponent of the length base unit`() {
        val area = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 2)))

        assertEquals(5.0, area.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `toKLengthUnit normalizes a non-base length unit to the base unit`() {
        val fiveMiles = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))

        assertEquals(5.0 * KLengthUnit.MILE.baseValue, fiveMiles.toKLengthUnit().value, 1e-6)
    }

    @Test
    fun `toKLengthUnit fails for a non-length unit`() {
        val notLength = KUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

        assertFailsWith<IllegalStateException> { notLength.toKLengthUnit() }
    }

    @Test
    fun `toKLengthUnit fails for a mixed unit with more than one term`() {
        val notPure = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KLengthUnit.BASE, 2)))

        assertFailsWith<IllegalStateException> { notPure.toKLengthUnit() }
    }

    @Test
    fun `equals throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares() == 5.meters() }
    }

    @Test
    fun `compareTo throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares() < 5.meters() }
    }

    @Test
    fun `every derived unit generator round trips through valueIn`() {
        for ((generator, derived) in lengthDerivedUnitGenerators) {
            val instance = generator(5)
            assertEquals(5.0, instance.valueIn(derived), 5.0 * 1e-6, "valueIn round trip mismatch for $derived")
        }
    }

    @Test
    fun `valueIn and toString support derived unit targets`() {
        val area = 5.hectares()

        assertEquals(5.0, area.valueIn(KLengthDerivedUnit.HECTARE), 1e-9)
        assertEquals("5.0 ha", area.toString(KLengthDerivedUnit.HECTARE))
    }
}
