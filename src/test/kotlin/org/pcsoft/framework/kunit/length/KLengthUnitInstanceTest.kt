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

import org.pcsoft.framework.kunit.KDerivedUnit
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
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
    ({ n: Number -> n.lightSeconds() }) to KLengthUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes() }) to KLengthUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours() }) to KLengthUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays() }) to KLengthUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks() }) to KLengthUnit.LIGHT_WEEK,
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

    private fun Number.kilometers(): KLengthUnitInstance = this kilo KLengthUnit.METER

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
    fun `times with KLengthUnitInstance produces area as KMixedUnitInstance`() {
        val area = 200.meters() * 50.meters()

        assertEquals(10_000.0, area.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 2)), area.units)
    }

    @Test
    fun `div with KLengthUnitInstance cancels out to dimensionless KMixedUnitInstance`() {
        val result = 10.meters() / 2.meters()

        assertEquals(5.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `times with KMixedUnitInstance delegates to KMixedUnitInstance times`() {
        val speedPerSecond = KMixedUnitInstance(2.0, listOf(KUnitTerm(KLengthUnit.BASE, -1)))

        val result = 10.meters() * speedPerSecond

        assertEquals(20.0, result.value, 1e-9)
        assertTrue(result.units.isEmpty())
    }

    @Test
    fun `div with KMixedUnitInstance delegates to KMixedUnitInstance div`() {
        val time = KMixedUnitInstance(2.0, listOf())

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
    fun `every generator function round trips through valueAs`() {
        for ((generator, unit) in lengthUnitGenerators) {
            val instance = generator(5)
            assertEquals(5.0 * unit.baseValue, instance.value, instance.value.coerceAtLeast(1.0) * 1e-9,
                "value mismatch for $unit")
            assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9, "valueAs round trip mismatch for $unit")
        }
    }

    @Test
    fun `light units are defined via the speed of light`() {
        assertEquals(299792458.0, 1.lightSeconds().value, 1e-3)
        // Larger light units are exact multiples of the light-second.
        assertEquals(60.lightSeconds().value, 1.lightMinutes().value, 1e-3)
        assertEquals(60.lightMinutes().value, 1.lightHours().value, 1.0)
        assertEquals(24.lightHours().value, 1.lightDays().value, 1.0)
        assertEquals(7.lightDays().value, 1.lightWeeks().value, 1.0)
    }

    @Test
    fun `light unit mixes with another length unit via plus`() {
        val result = 1.lightSeconds() + 299792458.meters()
        assertEquals(2.0, result.valueAs(KLengthUnit.LIGHT_SECOND), 1e-6)
    }

    @Test
    fun `valueAs converts across units`() {
        assertEquals(5.0, 5.miles().valueAs(KLengthUnit.MILE), 1e-6)
        assertEquals(KLengthUnit.MILE.baseValue / KLengthUnit.METER.baseValue, 1.miles().valueAs(KLengthUnit.METER), 1e-6)
        assertEquals(1.0 / KLengthUnit.MILE.baseValue, 1.meters().valueAs(KLengthUnit.MILE), 1e-9)
    }

    @Test
    fun `valueAs supports scaled unit targets`() {
        val d = 5.miles()
        assertEquals(d.value / 1000.0, d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER), 1e-9)
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
    fun `toKMixedUnitInstance and toKLengthUnit round trip`() {
        val original = 5.miles()

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

    @Test
    fun `equals throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares() == 5.meters() }
    }

    @Test
    fun `compareTo throws for different exponents`() {
        assertFailsWith<IllegalStateException> { 5.hectares() < 5.meters() }
    }

    @Test
    fun `every derived unit generator round trips through valueAs`() {
        for ((generator, derived) in lengthDerivedUnitGenerators) {
            val instance = generator(5)
            assertEquals(5.0, instance.valueAs(derived), 5.0 * 1e-6, "valueAs round trip mismatch for $derived")
        }
    }

    @Test
    fun `valueAs and toString support derived unit targets`() {
        val area = 5.hectares()

        assertEquals(5.0, area.valueAs(KLengthDerivedUnit.HECTARE), 1e-9)
        assertEquals("5.0 ha", area.toString(KLengthDerivedUnit.HECTARE))
    }
}
