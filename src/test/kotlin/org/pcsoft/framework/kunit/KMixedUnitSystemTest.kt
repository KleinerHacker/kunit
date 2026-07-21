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

import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Surface of the generic engine: the `of`/`into` verbs, composite construction, and `scaledBy`. */
class KMixedUnitSystemTest {

    /** `of` scales a value-1 template; `into` reads it back; both preserve dimensions. */
    @Test
    fun `of and into round-trip`() {
        val v = 10.5 of meters
        assertEquals(10.5, v.value, 1e-9)
        assertEquals(10.5, v into meters, 1e-9)
    }

    /** Building a composite via `of` on a `*`/`pow` expression yields a mixed unit with the right terms. */
    @Test
    fun `composite construction`() {
        val x = 10 of meters * ((2 of seconds) pow 2) // 10 * (1 m * (2 s)²) = 40
        assertEquals(40.0, x.value, 1e-9)
        val sig = x.units.associate { it.unit to it.exponent }
        assertEquals(1, sig[KDistanceUnit.BASE])
        assertEquals(2, sig[KTimeUnit.SECOND])
    }

    /** `scaledBy` returns the same static/runtime type (backs `of`). */
    @Test
    fun `scaledBy preserves type`() {
        assertIs<KLengthUnitInstance>((1 of meters).scaledBy(3.0))
        assertEquals(3.0, (1 of meters).scaledBy(3.0).value, 1e-9)
    }
}
