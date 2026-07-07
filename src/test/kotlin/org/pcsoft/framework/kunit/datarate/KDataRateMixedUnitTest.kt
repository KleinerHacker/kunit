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

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.storage.mkStorage
import org.pcsoft.framework.kunit.storage.storageUnitGenerators
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.timeOf
import org.pcsoft.framework.kunit.time.timeUnitGenerators
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Cross-group tests for the *constructed* data-rate unit: every core storage unit combined with every
 * core time unit, verifying the **bidirectional** decomposition required for composed units - from the
 * core units *into* the data rate, and from the data rate back *into* every core unit of both involved
 * groups.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDataRateMixedUnitTest {

    /** Provider: the full cross-product of every storage unit against every time unit (core → composed matrix). */
    private fun storageTimePairs(): List<Arguments> =
        storageUnitGenerators.flatMap { (_, s) -> timeUnitGenerators.map { (_, t) -> Arguments.of(s, t) } }

    /** Provider: every storage unit, for the "read back into every storage unit" decomposition. */
    private fun storages(): List<Arguments> = storageUnitGenerators.map { Arguments.of(it.second) }

    /** Provider: every time unit, for the "read back into every time unit" decomposition. */
    private fun times(): List<Arguments> = timeUnitGenerators.map { Arguments.of(it.second) }

    /** Relative tolerance across the wide magnitude span (bit/day … yobibyte/s). */
    private fun delta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    // region core -> composed (storage / time == data rate)

    /** core → composed: `storage / time` for every unit pair yields a typed [KDataRateUnitInstance] with the right value and `[B¹, s⁻¹]` signature. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("storageTimePairs")
    fun `dividing a storage by a time yields a typed data rate`(storage: KStorageUnit, time: KTimeUnit) {
        val rate: KDataRateUnitInstance = mkStorage(storage, 10) / timeOf(time, 2)

        val expected = 10.0 * storage.baseValue / (2.0 * time.baseValue)
        assertEquals(expected, rate.value, delta(expected), "$storage / $time value mismatch")
        assertEquals(
            setOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
            rate.toUnit().units.toSet(),
            "$storage / $time term signature mismatch"
        )
    }

    /** The typed `storage / time` operator and the raw mixed-engine division + `toDataRate()` produce the same value, for every unit pair. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("storageTimePairs")
    fun `the raw mixed division agrees with toDataRate`(storage: KStorageUnit, time: KTimeUnit) {
        val viaOperator = mkStorage(storage, 10) / timeOf(time, 2)
        val viaConversion = (mkStorage(storage, 10).toUnit() / timeOf(time, 2).toUnit()).toDataRate()

        assertEquals(viaConversion.value, viaOperator.value, delta(viaConversion.value), "$storage / $time mismatch")
    }

    // endregion

    // region composed -> core (data rate * time == storage, read back into every storage unit)

    /** composed → core: `data rate * time` recovers a typed storage amount and reads back correctly into every storage unit, for every time unit. */
    @ParameterizedTest(name = "rate * time read back in {0}")
    @MethodSource("storages")
    fun `data rate times time decomposes back into every storage unit`(readBack: KStorageUnit) {
        val rate = dataRateOf(KDataRateUnit.BYTES_PER_SECOND, 1000) // 1000 B/s

        timeUnitGenerators.forEach { (_, timeUnit) ->
            val storage: KStorageUnitInstance = rate * timeOf(timeUnit, 5)
            val expectedBytes = 1000.0 * (5.0 * timeUnit.baseValue)
            assertEquals(expectedBytes, storage.value, delta(expectedBytes), "value mismatch for $timeUnit")
            val expectedReadBack = expectedBytes / readBack.baseValue
            assertEquals(expectedReadBack, storage.valueAs(readBack), delta(expectedReadBack),
                "read-back mismatch into $readBack for $timeUnit")
            assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1)), storage.toUnit().units)
        }
    }

    // endregion

    // region composed -> core (storage / data rate == time, read back into every time unit)

    /** composed → core: `storage / data rate` recovers a typed time and reads back correctly into every time unit, for every storage unit. */
    @ParameterizedTest(name = "storage / rate read back in {0}")
    @MethodSource("times")
    fun `storage divided by data rate decomposes back into every time unit`(readBack: KTimeUnit) {
        val rate = dataRateOf(KDataRateUnit.BYTES_PER_SECOND, 1000) // 1000 B/s

        storageUnitGenerators.forEach { (_, storageUnit) ->
            val time: KTimeUnitInstance = mkStorage(storageUnit, 100_000) / rate
            val expectedSeconds = 100_000.0 * storageUnit.baseValue / 1000.0
            assertEquals(expectedSeconds, time.value, delta(expectedSeconds), "value mismatch for $storageUnit")
            val expectedReadBack = expectedSeconds / readBack.baseValue
            assertEquals(expectedReadBack, time.valueAs(readBack), delta(expectedReadBack),
                "read-back mismatch into $readBack for $storageUnit")
            assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), time.toUnit().units)
        }
    }

    // endregion

    // region commutativity and error cases

    /** `rate * time` and `time * rate` yield the same storage value — cross-group multiplication is commutative. */
    @Test
    fun `time times rate equals rate times time`() {
        val rate = dataRateOf(KDataRateUnit.BYTES_PER_SECOND, 1000)

        assertEquals((rate * timeOf(KTimeUnit.SECOND, 60)).value, (timeOf(KTimeUnit.SECOND, 60) * rate).value, 1e-9)
    }

    /** A `[B¹, s⁻²]` shape (a rate over time) is not a data rate and `toDataRate()` throws `IllegalStateException`. */
    @Test
    fun `a rate over time is not a data rate`() {
        val ratePerTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2)))

        assertFailsWith<IllegalStateException> { ratePerTime.toDataRate() }
    }

    /** A plain storage value (single `B¹` term) is not a data-rate shape, so `toDataRate()` throws `IllegalStateException`. */
    @Test
    fun `a plain storage value is not a data rate`() {
        assertFailsWith<IllegalStateException> { mkStorage(KStorageUnit.BYTE, 5).toUnit().toDataRate() }
    }

    // endregion
}
