package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private fun area(sideA: Number, sideB: Number): KMixedUnitInstance = sideA.meters() * sideB.meters()

private fun volume(a: Number, b: Number, c: Number): KMixedUnitInstance = (a.meters() * b.meters()) * c.meters().toKMixedUnitInstance()

private fun volumeOf(cubicMeters: Double): KMixedUnitInstance = KMixedUnitInstance(cubicMeters, listOf(KUnitTerm(KLengthUnit.BASE, 3)))

class KLengthDerivedUnitTest {

    @Test
    fun `are - forward and backward conversion`() {
        val a = area(10, 10) // 100 m^2
        assertEquals(1.0, a.valueAs(KLengthDerivedUnit.ARE), 1e-9)

        val back = 5.ares().toKMixedUnitInstance() // 5 are, via the generator function
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.ARE), 1e-9)
    }

    @Test
    fun `hectare - forward and backward conversion`() {
        val a = area(200, 50) // 10 000 m^2
        assertEquals(1.0, a.valueAs(KLengthDerivedUnit.HECTARE), 1e-9)

        val back = 5.hectares().toKMixedUnitInstance() // 5 ha
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `acre - forward and backward conversion`() {
        val a = KMixedUnitInstance(4046.8564224, listOf(KUnitTerm(KLengthUnit.BASE, 2))) // exactly 1 acre
        assertEquals(1.0, a.valueAs(KLengthDerivedUnit.ACRE), 1e-9)

        val back = 5.acres().toKMixedUnitInstance() // 5 ac
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.ACRE), 1e-9)
    }

    @Test
    fun `liter - forward and backward conversion`() {
        val v = volume(2, 2, 2) // 8 m^3
        assertEquals(8000.0, v.valueAs(KLengthDerivedUnit.LITER), 1e-6)

        val back = 5.liters().toKMixedUnitInstance() // 5 L
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.LITER), 1e-9)
    }

    @Test
    fun `us gallon - forward and backward conversion`() {
        val v = volumeOf(1.0) // 1 m^3
        assertEquals(1.0 / KLengthDerivedUnit.US_GALLON.baseValue, v.valueAs(KLengthDerivedUnit.US_GALLON), 1e-6)

        val back = 5.usGallons().toKMixedUnitInstance() // 5 US gal
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.US_GALLON), 1e-9)
    }

    @Test
    fun `imperial gallon - forward and backward conversion`() {
        val v = volumeOf(1.0)
        assertEquals(1.0 / KLengthDerivedUnit.IMPERIAL_GALLON.baseValue, v.valueAs(KLengthDerivedUnit.IMPERIAL_GALLON), 1e-6)

        val back = 5.imperialGallons().toKMixedUnitInstance()
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.IMPERIAL_GALLON), 1e-9)
    }

    @Test
    fun `us fluid ounce - forward and backward conversion`() {
        val v = volumeOf(1.0)
        assertEquals(1.0 / KLengthDerivedUnit.US_FLUID_OUNCE.baseValue, v.valueAs(KLengthDerivedUnit.US_FLUID_OUNCE), 1e-3)

        val back = 5.usFluidOunces().toKMixedUnitInstance()
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.US_FLUID_OUNCE), 1e-9)
    }

    @Test
    fun `oil barrel - forward and backward conversion`() {
        val v = volumeOf(1.0)
        assertEquals(1.0 / KLengthDerivedUnit.OIL_BARREL.baseValue, v.valueAs(KLengthDerivedUnit.OIL_BARREL), 1e-6)

        val back = 5.oilBarrels().toKMixedUnitInstance()
        assertEquals(5.0, back.valueAs(KLengthDerivedUnit.OIL_BARREL), 1e-9)
    }

    @Test
    fun `hectare fails against a pure length exponent`() {
        val length = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 1)))

        assertFailsWith<IllegalStateException> { length.valueAs(KLengthDerivedUnit.HECTARE) }
    }

    @Test
    fun `hectare fails against a volume exponent`() {
        val volume = volumeOf(5.0)

        assertFailsWith<IllegalStateException> { volume.valueAs(KLengthDerivedUnit.HECTARE) }
    }

    @Test
    fun `liter combined with milli prefix - forward and backward conversion`() {
        val milliliter = KUnitPrefix.MILLI with KLengthDerivedUnit.LITER

        val v = volumeOf(0.000008) // 8 mL
        assertEquals(8.0, v.valueAs(milliliter), 1e-9)

        val back = volumeOf(5.0 * milliliter.baseValue) // 5 mL
        assertEquals(5.0, back.valueAs(milliliter), 1e-9)
    }

    @Test
    fun `valueAs and toString work directly on KLengthUnitInstance for a derived unit target`() {
        val liters = 5.liters()

        assertEquals(5.0, liters.valueAs(KLengthDerivedUnit.LITER), 1e-9)
        assertEquals("5.0 L", liters.toString(KLengthDerivedUnit.LITER))
    }

    @Test
    fun `valueAs and toString work directly on KLengthUnitInstance for a scaled derived unit target`() {
        val liters = 5.liters()
        val milliliter = KUnitPrefix.MILLI with KLengthDerivedUnit.LITER

        assertEquals(5000.0, liters.valueAs(milliliter), 1e-6)
        assertEquals("5000.0 mL", liters.toString(milliliter))
    }
}
