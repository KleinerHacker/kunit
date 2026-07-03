package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.length.meters
import org.pcsoft.framework.kunit.length.toKLengthUnit
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KTimeMixedUnitTest {

    @Test
    fun `dividing a length by a time produces speed`() {
        val speed = 10.meters() / 2.seconds().toKUnitInstance()

        assertEquals(5.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `speed converts to kilometers per hour`() {
        val speed = 10.meters() / 1.seconds().toKUnitInstance() // 10 m/s

        val kmh = speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR)

        assertEquals(36.0, kmh, 1e-9)
        assertEquals("36.0 km*h^-1", speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR))
    }

    @Test
    fun `multiplying speed back by time recovers a pure length`() {
        val speed = 10.meters() / 2.seconds().toKUnitInstance() // 5 m/s
        val time = 2.seconds()

        val distance = speed * time.toKUnitInstance()

        assertEquals(10.0, distance.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `dividing a length by a time and multiplying back is not a pure time`() {
        val speed = 10.meters() / 2.seconds().toKUnitInstance()

        assertFailsWith<IllegalStateException> { speed.toKTimeUnit() }
    }
}
