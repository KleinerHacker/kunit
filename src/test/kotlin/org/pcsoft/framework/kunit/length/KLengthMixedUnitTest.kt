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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.time.KTimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Test-local helper: a time term as a raw KMixedUnitInstance (not the time-group creator), so that
// `length / this` exercises KLengthUnitInstance.div(KMixedUnitInstance).
private val Number.seconds: KMixedUnitInstance get() = KMixedUnitInstance(toDouble(), listOf(KUnitTerm(KTimeUnit.SECOND, 1)))

class KLengthMixedUnitTest {

    @Test
    fun `dividing a length by a mixed unit produces speed`() {
        val speed = 10.meters / 2.seconds

        assertEquals(5.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `multiplying a length with a mixed unit`() {
        val perSecond = KMixedUnitInstance(2.0, listOf(KUnitTerm(KTimeUnit.SECOND, -1)))

        val speed = 10.meters * perSecond

        assertEquals(20.0, speed.value, 1e-9)
        assertEquals(setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), speed.units.toSet())
    }

    @Test
    fun `multiplying speed back by time converts back to a pure length`() {
        val speed = 10.meters / 2.seconds // 5 m/s
        val time = 2.seconds

        val distance = speed * time

        assertEquals(10.0, distance.toKLengthUnit().value, 1e-9)
    }

    @Test
    fun `toKLengthUnit throws for an instance that is not a pure length`() {
        val speed = 10.meters / 2.seconds

        assertFailsWith<IllegalStateException> { speed.toKLengthUnit() }
    }
}
