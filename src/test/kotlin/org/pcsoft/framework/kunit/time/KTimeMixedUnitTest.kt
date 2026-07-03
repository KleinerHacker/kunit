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
        val speed = 10.meters() / 2.seconds().toKMixedUnitInstance()

        assertEquals(5.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `speed converts to kilometers per hour`() {
        val speed = 10.meters() / 1.seconds().toKMixedUnitInstance() // 10 m/s

        val kmh = speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR)

        assertEquals(36.0, kmh, 1e-9)
        assertEquals("36.0 km*h^-1", speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR))
    }

    @Test
    fun `multiplying speed back by time recovers a pure length`() {
        val speed = 10.meters() / 2.seconds().toKMixedUnitInstance() // 5 m/s
        val time = 2.seconds()

        val distance = speed * time.toKMixedUnitInstance()

        assertEquals(10.0, distance.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `dividing a length by a time and multiplying back is not a pure time`() {
        val speed = 10.meters() / 2.seconds().toKMixedUnitInstance()

        assertFailsWith<IllegalStateException> { speed.toKTimeUnit() }
    }
}
