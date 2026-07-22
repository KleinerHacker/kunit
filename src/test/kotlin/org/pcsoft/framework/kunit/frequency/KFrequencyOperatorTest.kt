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

package org.pcsoft.framework.kunit.frequency

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.KDistanceUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * All frequency operators: same-type `+`/`-`/comparison and `*`/`/` (mixed), the generic cross-group
 * fallback, and the typed **inverse-of-time** cross operators (`count / time = frequency`,
 * `frequency * time = count`, `length * frequency = speed`, `speed / frequency = length`).
 */
class KFrequencyOperatorTest {

    /** Add/subtract normalize to hertz; comparison uses the hertz value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(1500.0, ((1 of kilo.hertz) + (500 of hertz)).value, 1e-9)
        assertEquals(500.0, ((1 of kilo.hertz) - (500 of hertz)).value, 1e-9)
        assertTrue((1 of kilo.hertz) > (500 of hertz))
        assertTrue((500 of hertz) < (1 of kilo.hertz))
        assertTrue((1 of kilo.hertz).compareTo(1000 of hertz) == 0)
    }

    /** Same-type `*`/`/` escape to a generic mixed unit. */
    @Test
    fun `same-type times and div`() {
        assertEquals(6.0, ((2 of hertz) * (3 of hertz)).value, 1e-9)
        assertEquals(2.0, ((6 of hertz) / (3 of hertz)).value, 1e-9)
    }

    /** A non-meaningful cross-group combination falls back to a generic mixed unit. */
    @Test
    fun `generic times fallback`() {
        val product = (2 of hertz).toUnit() * (3 of grams).toUnit()
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)
    }

    /** `count / time = frequency` (typed), overriding the generic reciprocal. */
    @Test
    fun `count over time is frequency`() {
        val f = 60 / (1 of seconds)
        assertIs<KFrequencyUnitInstance>(f)
        assertEquals(60.0, f.value, 1e-9)
        assertEquals(0.5, (1 / (2 of seconds)).value, 1e-9)
    }

    /** `count / frequency = time` (the period). */
    @Test
    fun `count over frequency is time`() {
        val period = 1 / (2 of hertz)
        assertIs<KTimeUnitInstance>(period)
        assertEquals(0.5, period into seconds, 1e-9)
    }

    /** `frequency * time = count` (dimensionless), commutative. */
    @Test
    fun `frequency times time is count`() {
        assertEquals(100.0, (50 of hertz) * (2 of seconds), 1e-9)
        assertEquals(100.0, (2 of seconds) * (50 of hertz), 1e-9)
    }

    /** `length * frequency = speed`, commutative, and equal to the inverse `length / period`. */
    @Test
    fun `length times frequency is speed`() {
        val v = (2 of meters) * (5 of hertz)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(10.0, v.value, 1e-9)
        assertEquals(10.0, ((5 of hertz) * (2 of meters)).value, 1e-9)
        // inverse-of-time equivalence: multiply by 5 Hz == divide by a period of 0.2 s
        assertEquals(((2 of meters) / (0.2 of seconds)).value, v.value, 1e-9)
    }

    /** `speed / frequency = length`, the inverse of `speed * time`. */
    @Test
    fun `speed over frequency is length`() {
        val v = (10 of meters) / (1 of seconds) // 10 m/s
        val length = v / (5 of hertz)
        assertIs<KLengthUnitInstance>(length)
        assertEquals(2.0, length into meters, 1e-9)
    }

    /** The general-distance fallback: `distance * frequency = speed` for a runtime-exponent distance. */
    @Test
    fun `general distance times frequency`() {
        val d: KDistanceUnitInstance = 2 of meters
        val v = d * (5 of hertz)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(10.0, v.value, 1e-9)
    }
}
