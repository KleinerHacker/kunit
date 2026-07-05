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

package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.toDistance
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KTimeMixedUnitTest {

    /** Dividing a length by a time through the mixed engine produces a `[m¹, s⁻¹]` speed-shaped mixed unit with the right value. */
    @Test
    fun `dividing a length by a time produces speed`() {
        val speed = 10.meters / 2.seconds.toUnit()

        assertEquals(5.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    /** A speed-shaped mixed unit reads back as km/h via `valueAs`/`toString` with a composed length-per-time target (10 m/s == 36 km/h). */
    @Test
    fun `speed converts to kilometers per hour`() {
        val speed = 10.meters / 1.seconds.toUnit() // 10 m/s

        val kmh = speed.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)

        assertEquals(36.0, kmh, 1e-9)
        assertEquals("36.0 km*h^-1", speed.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR))
    }

    /** Multiplying a speed-shaped mixed unit back by a time cancels the second term and recovers a pure length. */
    @Test
    fun `multiplying speed back by time recovers a pure length`() {
        val speed = 10.meters / 2.seconds.toUnit() // 5 m/s
        val time = 2.seconds

        val distance = speed * time.toUnit()

        assertEquals(10.0, distance.toDistance().value, 1e-9)
    }

    /** A speed-shaped mixed unit is not a pure time value, so `toTime()` throws `IllegalStateException`. */
    @Test
    fun `dividing a length by a time and multiplying back is not a pure time`() {
        val speed = 10.meters / 2.seconds.toUnit()

        assertFailsWith<IllegalStateException> { speed.toTime() }
    }
}
