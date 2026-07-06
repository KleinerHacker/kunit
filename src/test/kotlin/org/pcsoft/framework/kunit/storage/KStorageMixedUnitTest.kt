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

package org.pcsoft.framework.kunit.storage

import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Cross-group behaviour of the storage group: storage x time through the mixed engine (a data rate,
 * `byte·second⁻¹`), decomposed in both directions across every storage and every time unit.
 */
class KStorageMixedUnitTest {

    /** Dividing a storage value by a time forms a raw two-term `[B¹, s⁻¹]` mixed unit (a data rate) with the right value. */
    @Test
    fun `storage over time forms a two-term mixed unit`() {
        val rate = 1000.bytes.toUnit() / 2.seconds.toUnit()
        assertEquals(500.0, rate.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), rate.units)
    }

    /** core → composed: every storage unit over every time unit yields the data rate with the right value and term signature. */
    @Test
    fun `every storage over every time forms the right rate`() {
        for ((_, storageUnit) in storageUnitGenerators) for (timeUnit in KTimeUnit.entries) {
            val storage = mkStorage(storageUnit, 5)
            val time = org.pcsoft.framework.kunit.KMixedUnitInstance(2.0, listOf(KUnitTerm(timeUnit, 1)))
            val rate = storage.toUnit() / time
            // The mixed engine divides raw values; storage.toUnit() is normalized to bytes, time keeps its raw 2.0.
            val expected = (5.0 * storageUnit.baseValue) / 2.0
            assertEquals(expected, rate.value, storageDelta(expected), "$storageUnit / $timeUnit")
            assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(timeUnit, -1)), rate.units, "$storageUnit / $timeUnit")
        }
    }

    /** composed → core: `rate * time` recovers the storage amount and reads back in **every** storage unit. */
    @Test
    fun `rate times time recovers storage in every storage unit`() {
        val rate = 1000.bytes.toUnit() / 1.seconds.toUnit() // 1000 B/s
        val recovered = (rate * 60.seconds.toUnit()).toStorage() // 60000 B
        for ((_, unit) in storageUnitGenerators) {
            val expected = 60_000.0 / unit.baseValue
            assertEquals(expected, recovered.valueAs(unit), storageDelta(expected), "read back in $unit")
        }
    }

    /** composed → core: `storage / rate` recovers the time and reads back in **every** time unit. */
    @Test
    fun `storage divided by rate recovers time in every time unit`() {
        val rate = 1000.bytes.toUnit() / 1.seconds.toUnit() // 1000 B/s
        val time = 60_000.bytes.toUnit() / rate // 60 s
        for (unit in KTimeUnit.entries) {
            val expected = 60.0 / unit.baseValue
            assertEquals(expected, time.valueAs(unit), 1e-6, "read back in $unit")
        }
    }

    /** A data rate divided again by a time (`B·s⁻²`) is not a pure storage value and `toStorage()` fails with `IllegalStateException`. */
    @Test
    fun `a rate over time is not a pure storage value`() {
        val rate = 1000.bytes.toUnit() / 1.seconds.toUnit()
        val weird = rate / 2.seconds.toUnit() // [B¹, s⁻²]
        assertFailsWith<IllegalStateException> { weird.toStorage() }
    }
}
