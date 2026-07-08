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
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.storage.bits
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.kibi
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

/**
 * Behaviour matrix for the constructed data-rate group (storage·time⁻¹): core→composed
 * (`storage / time = data rate`) across a storage×time matrix, composed→core decomposition, the prefixed
 * numerator (`mega.bytes / seconds`, `kibi.bytes / seconds`), whole-unit tokens, and `of`/`into`.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDataRateTest {

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
        storages.flatMap { s -> times.map { t -> arrayOf<Any>(s.first, s.second, t.first, t.second) } }

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

    /** A non-rate shape (storage² / time) fails to become a data rate. */
    @Test
    fun `invalid rate decomposition fails`() {
        val sq = (2 of bytes).toUnit() * (2 of bytes).toUnit()
        assertFailsWith<IllegalStateException> { (sq / (1 of seconds).toUnit()).toDataRate() }
    }
}
