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

package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Core `of`/`into` verbs, `scaledBy`, and the cross-group `*`/`/` operators (generic vs typed). */
class KMixedUnitTest {

    /** `of` scales a value-1 template; `into` reads it back; both preserve dimensions. */
    @Test
    fun `of and into round-trip`() {
        val v = 10.5 of meters
        assertEquals(10.5, v.value, 1e-9)
        assertEquals(10.5, v into meters, 1e-9)
    }

    /** Two unrelated groups combine via the generic cross-group `/` into a KMixedUnitInstance. */
    @Test
    fun `generic cross-group div gives mixed`() {
        val mixed = (20 of bytes) / (20 of meters)
        assertIs<KMixedUnitInstance>(mixed)
        assertEquals(1.0, mixed.value, 1e-9)
        assertTrue(mixed.hasSameUnits(KMixedUnitInstance(1.0, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(org.pcsoft.framework.kunit.distance.KDistanceUnit.BASE, -1)))))
    }

    /** The typed speed extension still wins over the generic operator (`length / time = speed`). */
    @Test
    fun `typed speed operator wins`() {
        val v = (100 of meters) / (5 of seconds)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(20.0, v.value, 1e-9)
    }

    /** The member `length * length` still wins and returns a typed area. */
    @Test
    fun `member area operator wins`() {
        val area = (3 of meters) * (4 of meters)
        assertIs<KAreaUnitInstance>(area)
        assertEquals(12.0, area.value, 1e-9)
    }

    /** Building a composite via `of` on a `*`/`pow` expression yields a mixed unit with the right terms. */
    @Test
    fun `composite construction`() {
        val x = 10 of meters * ((2 of seconds) pow 2) // 10 * (1 m * (2 s)²) = 40
        assertEquals(40.0, x.value, 1e-9)
        val sig = x.units.associate { it.unit to it.exponent }
        assertEquals(1, sig[org.pcsoft.framework.kunit.distance.KDistanceUnit.BASE])
        assertEquals(2, sig[KTimeUnit.SECOND])
    }

    /** Mixed `+` requires matching dimensions; unrelated shapes fail. */
    @Test
    fun `mixed plus dimension check`() {
        val a = (10 of meters) / (2 of seconds) // m/s
        val b = (4 of meters) / (1 of seconds)  // m/s
        assertEquals(9.0, (a.toUnit() + b.toUnit()).value, 1e-9)
        assertFailsWith<IllegalStateException> { (10 of meters).toUnit() + (2 of seconds).toUnit() }
    }

    /** `scaledBy` returns the same static/runtime type (backs `of`). */
    @Test
    fun `scaledBy preserves type`() {
        assertIs<KLengthUnitInstance>((1 of meters).scaledBy(3.0))
        assertEquals(3.0, (1 of meters).scaledBy(3.0).value, 1e-9)
    }
}
