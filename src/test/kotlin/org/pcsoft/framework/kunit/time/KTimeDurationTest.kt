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

import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KTimeDurationTest {

    @Test
    fun `toDuration and Duration_toKTimeUnit round trip`() {
        val duration = Duration.ofMinutes(90)

        val instance = duration.toKTimeUnit()

        assertEquals(1.5, instance.valueAs(KTimeUnit.HOUR), 1e-12)
        assertEquals(duration, instance.toDuration())
    }

    @Test
    fun `creators produce the expected Duration`() {
        assertEquals(Duration.ofHours(2), 2.hours().toDuration())
        assertEquals(Duration.ofDays(1), 1.days().toDuration())
    }

    @Test
    fun `plus and minus match underlying Duration arithmetic`() {
        val a = 1.hours()
        val b = 30.minutes()

        assertEquals(a.toDuration() + b.toDuration(), (a + b).toDuration())
        assertEquals(a.toDuration() - b.toDuration(), (a - b).toDuration())
    }

    @Test
    fun `compareTo and equals match underlying Duration`() {
        assertTrue(30.minutes() < 1.hours())
        assertTrue(1.hours() == 60.minutes())
        assertEquals(Duration.ofHours(1), 60.minutes().toDuration())
    }

    @Test
    fun `forwarded Duration mutators return a KTimeUnitInstance`() {
        assertEquals(Duration.ofHours(1).plusDays(1), 1.hours().plusDays(1).toDuration())
        assertEquals(Duration.ofHours(1).minusMinutes(30), 1.hours().minusMinutes(30).toDuration())
        assertEquals(Duration.ofHours(1).multipliedBy(3), 1.hours().multipliedBy(3).toDuration())
        assertEquals(Duration.ofHours(1).negated(), 1.hours().negated().toDuration())
        assertEquals(Duration.ofHours(-1).abs(), (-1).hours().abs().toDuration())
    }

    @Test
    fun `forwarded Duration query methods pass through`() {
        val t = 90.minutes() // 1 h 30 min

        assertEquals(1, t.toHours())
        assertEquals(90, t.toMinutes())
        assertEquals(1, t.toHoursPart())
        assertEquals(30, t.toMinutesPart())
        assertFalse(t.isNegative())
        assertFalse(t.isZero())
        assertTrue((-1).seconds().isNegative())
        assertTrue(0.seconds().isZero())
    }

    @Test
    fun `dividedBy another duration counts how many fit`() {
        assertEquals(3, 90.minutes().dividedBy(30.minutes()))
    }

    @Test
    fun `sub-second construction round trips through nanoseconds`() {
        val t = 0.25.seconds()

        assertEquals(250_000_000, t.getNano())
        assertEquals(0, t.getSeconds())
        assertEquals(0.25, t.value, 1e-12)
    }
}
