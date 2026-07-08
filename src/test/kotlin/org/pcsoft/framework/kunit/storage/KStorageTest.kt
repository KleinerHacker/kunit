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
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Behaviour matrix for the storage group under the `of`/`into`/builder DSL, including the SI vs binary
 * (IEC) prefixes and the compile-time guarantee that diminishing prefixes are rejected for `bytes`.
 *
 * Compile-time note: `milli.bytes` does **not** compile - the `bytes` property is declared only on the
 * augmenting SI builder ([KAugmentingPrefixBuilder]) and the binary builder, never on the diminishing
 * builder. This cannot be asserted at runtime; it is guaranteed by the type hierarchy.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KStorageTest {

    private val tokens: List<Pair<KStorageUnitInstance, Double>> =
        listOf(bytes to KStorageUnit.BYTE.baseValue, bits to KStorageUnit.BIT.baseValue)

    private val siPrefixes: List<Pair<KAugmentingPrefixBuilder, Double>> =
        listOf(kilo to 1e3, mega to 1e6, giga to 1e9)

    private val binaryPrefixes: List<Pair<KStorageBinaryPrefixBuilder, Double>> =
        listOf(kibi to 1024.0, mebi to 1_048_576.0, gibi to 1_073_741_824.0)

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }
    private fun siArgs(): List<Array<Any>> = siPrefixes.map { arrayOf<Any>(it.first, it.second) }
    private fun binaryArgs(): List<Array<Any>> = binaryPrefixes.map { arrayOf<Any>(it.first, it.second) }

    /** `n of bytes`/`n of bits` normalizes to bytes and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KStorageUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** 1 byte == 8 bits, both directions. */
    @Test
    fun `byte bit conversion`() {
        assertEquals(8.0, (1 of bytes) into bits, 1e-9)
        assertEquals(0.125, (1 of bits) into bytes, 1e-12)
    }

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

    /** Add/subtract normalize to bytes; comparison uses the byte value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(2.0, ((1 of bytes) + (8 of bits)).value, 1e-9)
        assertEquals(0.0, ((1 of bytes) - (8 of bits)).value, 1e-9)
        assertTrue((1 of kibi.bytes) > (1 of kilo.bytes))
    }

    /** Reading storage in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of bytes) into ((1 of bytes).toUnit() * (1 of bytes).toUnit()) }
    }
}
