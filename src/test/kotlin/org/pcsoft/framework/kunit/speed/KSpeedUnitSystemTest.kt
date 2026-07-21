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

package org.pcsoft.framework.kunit.speed

import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

/**
 * `KSpeedUnitInstance` surface: a speed is built as an expression (`kilo.meters / hours`), typed as a
 * speed, and read back via `into` against an expression template.
 */
class KSpeedUnitSystemTest {

    /** Speeds are built as expressions; km/h reads back via a `kilo.meters / hours` template. */
    @Test
    fun `expression speeds`() {
        assertEquals(10.0, (36 of kilo.meters / hours).value, 1e-9)
        assertEquals(36.0, ((100 of meters) / (10 of seconds)) into (kilo.meters / hours), 1e-9)
        // klammerfreier prefixed length rate
        val r = 10 of kilo.meters / hours
        assertIs<KSpeedUnitInstance>(r)
        assertEquals(10_000.0 / 3600.0, r.value, 1e-9)
    }

    /** Equality/hash by normalized m/s value, and the base-unit string form. */
    @Test
    fun `equals hashCode and toString`() {
        val v = 10 of meters / seconds
        assertEquals(10 of meters / seconds, v)
        assertEquals((10 of meters / seconds).hashCode(), v.hashCode())
        assertEquals("10.0 m/s", v.toString())
        assertNotEquals(10 of meters / seconds, 20 of meters / seconds)
        assertFalse(v.equals(1.0)) // not a KSpeedUnitInstance
    }
}
