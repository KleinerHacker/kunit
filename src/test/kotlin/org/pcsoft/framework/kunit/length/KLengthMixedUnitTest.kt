package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnit
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private enum class TimeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    SECOND("s", 1.0),
    HOUR("h", 3600.0)
}

private fun Number.seconds(): KUnitInstance = KUnitInstance(toDouble(), listOf(KUnitTerm(TimeUnit.SECOND, 1)))

class KLengthMixedUnitTest {

    @Test
    fun `dividing a length by a mixed unit produces speed`() {
        val speed = 10.meters() / 2.seconds()

        assertEquals(5.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(TimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `multiplying a length with a mixed unit`() {
        val perSecond = KUnitInstance(2.0, listOf(KUnitTerm(TimeUnit.SECOND, -1)))

        val speed = 10.meters() * perSecond

        assertEquals(20.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(TimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `multiplying speed back by time converts back to a pure length`() {
        val speed = 10.meters() / 2.seconds() // 5 m/s
        val time = 2.seconds()

        val distance = speed * time

        assertEquals(10.0, distance.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `toKLengthUnit throws for an instance that is not a pure length`() {
        val speed = 10.meters() / 2.seconds()

        assertFailsWith<IllegalStateException> { speed.toKLengthUnit() }
    }
}
