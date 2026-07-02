package org.pcsoft.framework.kunit.length

import kotlin.test.Test
import kotlin.test.assertEquals

class KLengthUnitTest {

    @Test
    fun `meter is the base unit`() {
        assertEquals(KLengthUnit.METER, KLengthUnit.BASE)
        assertEquals(1.0, KLengthUnit.METER.baseValue)
        assertEquals("m", KLengthUnit.METER.symbol)
    }

    @Test
    fun `mile symbol and baseValue`() {
        assertEquals("mi", KLengthUnit.MILE.symbol)
        assertEquals(1609.344, KLengthUnit.MILE.baseValue)
    }

    @Test
    fun `nautical mile symbol and baseValue`() {
        assertEquals("nmi", KLengthUnit.NAUTICAL_MILE.symbol)
        assertEquals(1852.0, KLengthUnit.NAUTICAL_MILE.baseValue)
    }

    @Test
    fun `yard symbol and baseValue`() {
        assertEquals("yd", KLengthUnit.YARD.symbol)
        assertEquals(0.9144, KLengthUnit.YARD.baseValue)
    }

    @Test
    fun `foot symbol and baseValue`() {
        assertEquals("ft", KLengthUnit.FOOT.symbol)
        assertEquals(0.3048, KLengthUnit.FOOT.baseValue)
    }

    @Test
    fun `inch symbol and baseValue`() {
        assertEquals("in", KLengthUnit.INCH.symbol)
        assertEquals(0.0254, KLengthUnit.INCH.baseValue)
    }

    @Test
    fun `fathom symbol and baseValue`() {
        assertEquals("ftm", KLengthUnit.FATHOM.symbol)
        assertEquals(1.8288, KLengthUnit.FATHOM.baseValue)
    }

    @Test
    fun `chain symbol and baseValue`() {
        assertEquals("ch", KLengthUnit.CHAIN.symbol)
        assertEquals(20.1168, KLengthUnit.CHAIN.baseValue)
    }

    @Test
    fun `furlong symbol and baseValue`() {
        assertEquals("fur", KLengthUnit.FURLONG.symbol)
        assertEquals(201.168, KLengthUnit.FURLONG.baseValue)
    }

    @Test
    fun `astronomical unit symbol and baseValue`() {
        assertEquals("AU", KLengthUnit.ASTRONOMICAL_UNIT.symbol)
        assertEquals(1.495978707e11, KLengthUnit.ASTRONOMICAL_UNIT.baseValue)
    }

    @Test
    fun `light year symbol and baseValue`() {
        assertEquals("ly", KLengthUnit.LIGHT_YEAR.symbol)
        assertEquals(9.4607304725808e15, KLengthUnit.LIGHT_YEAR.baseValue)
    }

    @Test
    fun `parsec symbol and baseValue`() {
        assertEquals("pc", KLengthUnit.PARSEC.symbol)
        assertEquals(3.0856775814913673e16, KLengthUnit.PARSEC.baseValue)
    }

    @Test
    fun `all enum values are covered`() {
        assertEquals(12, KLengthUnit.entries.size)
    }
}
