package org.pcsoft.framework.kunit.length

import kotlin.test.Test
import kotlin.test.assertEquals

class LengthUnitTest {

    @Test
    fun `meter is the base unit`() {
        assertEquals(LengthUnit.METER, LengthUnit.BASE)
        assertEquals(1.0, LengthUnit.METER.baseValue)
        assertEquals("m", LengthUnit.METER.symbol)
    }

    @Test
    fun `mile symbol and baseValue`() {
        assertEquals("mi", LengthUnit.MILE.symbol)
        assertEquals(1609.344, LengthUnit.MILE.baseValue)
    }

    @Test
    fun `nautical mile symbol and baseValue`() {
        assertEquals("nmi", LengthUnit.NAUTICAL_MILE.symbol)
        assertEquals(1852.0, LengthUnit.NAUTICAL_MILE.baseValue)
    }

    @Test
    fun `yard symbol and baseValue`() {
        assertEquals("yd", LengthUnit.YARD.symbol)
        assertEquals(0.9144, LengthUnit.YARD.baseValue)
    }

    @Test
    fun `foot symbol and baseValue`() {
        assertEquals("ft", LengthUnit.FOOT.symbol)
        assertEquals(0.3048, LengthUnit.FOOT.baseValue)
    }

    @Test
    fun `inch symbol and baseValue`() {
        assertEquals("in", LengthUnit.INCH.symbol)
        assertEquals(0.0254, LengthUnit.INCH.baseValue)
    }

    @Test
    fun `fathom symbol and baseValue`() {
        assertEquals("ftm", LengthUnit.FATHOM.symbol)
        assertEquals(1.8288, LengthUnit.FATHOM.baseValue)
    }

    @Test
    fun `chain symbol and baseValue`() {
        assertEquals("ch", LengthUnit.CHAIN.symbol)
        assertEquals(20.1168, LengthUnit.CHAIN.baseValue)
    }

    @Test
    fun `furlong symbol and baseValue`() {
        assertEquals("fur", LengthUnit.FURLONG.symbol)
        assertEquals(201.168, LengthUnit.FURLONG.baseValue)
    }

    @Test
    fun `astronomical unit symbol and baseValue`() {
        assertEquals("AU", LengthUnit.ASTRONOMICAL_UNIT.symbol)
        assertEquals(1.495978707e11, LengthUnit.ASTRONOMICAL_UNIT.baseValue)
    }

    @Test
    fun `light year symbol and baseValue`() {
        assertEquals("ly", LengthUnit.LIGHT_YEAR.symbol)
        assertEquals(9.4607304725808e15, LengthUnit.LIGHT_YEAR.baseValue)
    }

    @Test
    fun `parsec symbol and baseValue`() {
        assertEquals("pc", LengthUnit.PARSEC.symbol)
        assertEquals(3.0856775814913673e16, LengthUnit.PARSEC.baseValue)
    }

    @Test
    fun `all enum values are covered`() {
        assertEquals(12, LengthUnit.entries.size)
    }
}
