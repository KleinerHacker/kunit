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

import org.pcsoft.framework.kunit.KAugmentingPrefixBuilder
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The prefix builders on the storage units: the decimal SI prefixes (augmenting only) and the binary IEC
 * prefixes, and the guarantee that `kilo` (1000) and `kibi` (1024) are genuinely distinct.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KStoragePrefixTest {

    private val siPrefixes: List<Pair<KAugmentingPrefixBuilder, Double>> =
        listOf(kilo to 1e3, mega to 1e6, giga to 1e9)

    private val binaryPrefixes: List<Pair<KStorageBinaryPrefixBuilder, Double>> =
        listOf(kibi to 1024.0, mebi to 1_048_576.0, gibi to 1_073_741_824.0)

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun siArgs(): List<Array<Any>> = siPrefixes.map { arrayOf<Any>(it.first, it.second) }
    private fun binaryArgs(): List<Array<Any>> = binaryPrefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Decimal SI prefixes scale a bytes template by powers of 1000 (`kilo.bytes == 1000 B`). */
    @ParameterizedTest
    @MethodSource("siArgs")
    fun `si prefix`(builder: KAugmentingPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.bytes).value, rel(factor))
    }

    /** Binary IEC prefixes scale a bytes template by powers of 1024 (`kibi.bytes == 1024 B`). */
    @ParameterizedTest
    @MethodSource("binaryArgs")
    fun `binary prefix`(builder: KStorageBinaryPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.bytes).value, rel(factor))
    }

    /** kilo (1000) and kibi (1024) are genuinely distinct, and read back correctly. */
    @Test
    fun `kilo differs from kibi`() {
        assertEquals(1000.0, (1 of kilo.bytes).value, 1e-9)
        assertEquals(1024.0, (1 of kibi.bytes).value, 1e-9)
        assertEquals(4.0, (4096 of bytes) into kibi.bytes, 1e-9)
    }

    /** The prefixed `bits` extensions on both the SI and the binary builder. */
    @Test
    fun `prefixed bits`() {
        assertEquals(1000.0 * KStorageUnit.BIT.baseValue, (1 of kilo.bits).value, 1e-9)
        assertEquals(1024.0 * KStorageUnit.BIT.baseValue, (1 of kibi.bits).value, 1e-9)
    }
}
