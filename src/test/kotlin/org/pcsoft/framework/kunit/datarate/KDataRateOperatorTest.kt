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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.storage.bits
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The typed cross-group data-rate operators: core→composed (`storage / time = data rate`) across a
 * storage×time matrix, composed→core decomposition (`rate * time`, `storage / rate`), and the
 * invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDataRateOperatorTest {

    private val storages = listOf(
        bytes to KStorageUnit.BYTE.baseValue,
        bits to KStorageUnit.BIT.baseValue
    )
    private val times = listOf(
        seconds to KTimeUnit.SECOND.baseValue,
        minutes to KTimeUnit.MINUTE.baseValue,
        hours to KTimeUnit.HOUR.baseValue
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun matrix(): List<Array<Any>> =
        storages.flatMap { s -> times.map { t -> arrayOf(s.first, s.second, t.first, t.second) } }

    /** core→composed: `storage / time` is a typed data rate whose B/s value is `s.base / t.base`. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `storage over time is data rate`(s: KStorageUnitInstance, sb: Double, t: KTimeUnitInstance, tb: Double) {
        val r = (100 of s) / (5 of t)
        assertIs<KDataRateUnitInstance>(r)
        val expected = 100.0 * sb / (5.0 * tb)
        assertEquals(expected, r.value, rel(expected))
    }

    /** composed→core: `data rate * time = storage`, recovered in bytes. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `rate times time is storage`(s: KStorageUnitInstance, sb: Double, t: KTimeUnitInstance, tb: Double) {
        val r = (100 of s) / (5 of t)
        val amount = r * (5 of t)
        assertIs<KStorageUnitInstance>(amount)
        assertEquals(100.0 * sb, amount into bytes, rel(100.0 * sb))
    }

    /** composed→core: `storage / rate = time`, recovered in seconds. */
    @Test
    fun `storage over rate is time`() {
        val r = (100 of bytes) / (10 of seconds) // 10 B/s
        val time = (600 of bytes) / r
        assertIs<KTimeUnitInstance>(time)
        assertEquals(60.0, time into seconds, 1e-9)
    }

    /** A non-rate shape (storage² / time, or a single storage term) fails to become a data rate. */
    @Test
    fun `invalid rate decomposition fails`() {
        val sq = (2 of bytes).toUnit() * (2 of bytes).toUnit()
        assertFailsWith<IllegalStateException> { (sq / (1 of seconds).toUnit()).toDataRate() }
        // a single storage term (size != 2, no time term) also fails
        assertFailsWith<IllegalStateException> { (100 of bytes).toUnit().toDataRate() }
        // two terms, storage present, but the time term has the wrong exponent (+1, not -1)
        assertFailsWith<IllegalStateException> { ((1 of bytes).toUnit() * (1 of seconds).toUnit()).toDataRate() }
    }

    /** Same-type data-rate operators: `+`/`-`, comparison, and `rate*rate`/`rate/rate` escaping to a mixed unit. */
    @Test
    fun `rate same-type operators`() {
        val r1 = (100 of bytes) / (10 of seconds) // 10 B/s
        val r2 = (20 of bytes) / (10 of seconds)  // 2 B/s
        assertEquals(12.0, (r1 + r2).value, 1e-9)
        assertEquals(8.0, (r1 - r2).value, 1e-9)
        assertTrue(r1 > r2)
        assertIs<KMixedUnitInstance>(r1 * r2)
        assertIs<KMixedUnitInstance>(r1 / r2)
    }

    /** The commutative `time * data rate = storage`. */
    @Test
    fun `time times rate is storage`() {
        val r = (100 of bytes) / (10 of seconds) // 10 B/s
        val amount = (60 of seconds) * r
        assertIs<KStorageUnitInstance>(amount)
        assertEquals(600.0, amount into bytes, 1e-9)
    }
}
