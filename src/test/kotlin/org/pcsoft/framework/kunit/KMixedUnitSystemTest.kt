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

import org.pcsoft.framework.kunit.formatter.displaySymbol
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

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

    /** `combineUnits` keeps each term's cosmetic display metadata (so `km/h` still renders prefixed). */
    @Test
    fun `division preserves display metadata`() {
        val target = kilo.meters / hours
        val km = target.units.single { it.unit == KDistanceUnit.BASE }
        val h = target.units.single { it.unit == KTimeUnit.BASE }
        assertEquals("km", km.displaySymbol)
        assertEquals("h", h.displaySymbol)
    }

    /** `pow` carries the per-term display metadata through unchanged. */
    @Test
    fun `pow preserves display metadata`() {
        val squared = kilo.meters pow 2
        assertEquals("km", squared.units.single().displaySymbol)
    }

    /** On a display collision (same unit on both sides), the left side's display wins in `combineUnits`. */
    @Test
    fun `display collision keeps left side`() {
        val product = kilo.meters.toUnit() * milli.meters.toUnit() // both KDistanceUnit.BASE -> exponent 2
        assertEquals("km", product.units.single().displaySymbol)
    }

    /** A value built via `of` drops the template's display (base-unit rendering for computed values). */
    @Test
    fun `of drops display on constructed value`() {
        assertNull((1 of kilo.meters).toUnit().units.single().display)
    }
}
