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

package org.pcsoft.framework.kunit.datarate

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bits
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

/**
 * `KDataRateUnitInstance` surface: a data rate is built as an expression (`bytes / seconds`, incl. a
 * prefixed numerator `mega.bytes / seconds`), typed as a data rate, and read back via `into`.
 */
class KDataRateUnitSystemTest {

    /** Prefixed numerator, klammerfrei: `5 of mega.bytes / seconds` and `10 of kibi.bytes / seconds`. */
    @Test
    fun `prefixed numerator rate`() {
        val a = 5 of mega.bytes / seconds
        assertIs<KDataRateUnitInstance>(a)
        assertEquals(5e6, a.value, 1e-3)
        val b = 10 of kibi.bytes / seconds
        assertEquals(10.0 * 1024, b.value, 1e-9)
    }

    /** Data rates are built as expressions; byte/bit rate reads back via `bits / seconds`. */
    @Test
    fun `expression rates and conversion`() {
        assertEquals(10.0, (10 of bytes / seconds).value, 1e-9)
        assertEquals(80.0, ((100 of bytes) / (10 of seconds)) into (bits / seconds), 1e-9)
    }

    /** Equality/hash by normalized B/s value, and the base-unit string form. */
    @Test
    fun `equals hashCode and toString`() {
        val r = 10 of bytes / seconds
        assertEquals(10 of bytes / seconds, r)
        assertEquals((10 of bytes / seconds).hashCode(), r.hashCode())
        assertEquals("10.0 B/s", r.toString())
        assertNotEquals(10 of bytes / seconds, 20 of bytes / seconds)
        assertFalse(r.equals(1.0)) // not a KDataRateUnitInstance
    }

    /** `format` a data rate into kB/s (generic target keeps the prefixed display symbol). */
    @Test
    fun `format compositions`() {
        assertEquals("0.01 kB/s", (10 of bytes / seconds) format (kilo.bytes.toUnit() / seconds.toUnit()))
    }
}
