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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.seconds
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

/** `KStorageUnitInstance` surface: `of`/`into` construction and round-trip, incompatible-unit read failure. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KStorageUnitSystemTest {

    private val tokens: List<Pair<KStorageUnitInstance, Double>> =
        listOf(bytes to KStorageUnit.BYTE.baseValue, bits to KStorageUnit.BIT.baseValue)

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of bytes`/`n of bits` normalizes to bytes and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KStorageUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Reading storage in an incompatible unit fails. */
    @Test
    fun `into incompatible fails`() {
        assertFailsWith<IllegalStateException> { (1 of bytes) into ((1 of bytes).toUnit() * (1 of bytes).toUnit()) }
    }

    /** Equality/hash by normalized byte value (`1 B == 8 bit`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of bytes, 8 of bits)
        assertEquals((1 of bytes).hashCode(), (8 of bits).hashCode())
        assertNotEquals(1 of bytes, 2 of bytes)
        assertFalse((1 of bytes).equals(1.0)) // not a KStorageUnitInstance
    }

    /** A mixed unit that is not a single storage term cannot be converted to a storage value. */
    @Test
    fun `toStorage on non-storage fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toStorage() }
        // a dimensionless (no-term) mixed unit also fails (the term is null)
        assertFailsWith<IllegalStateException> { ((1 of bytes).toUnit() / (1 of bytes).toUnit()).toStorage() }
    }

    /** `toString` (bytes) and `format` into binary/decimal prefixes and bytes. */
    @Test
    fun `toString and format compositions`() {
        assertEquals("1024.0 B", (1 of kibi.bytes).toString())
        assertEquals("1.0 KiB", (1024 of bytes) format kibi.bytes)
        assertEquals("1.0 kB", (1000 of bytes) format kilo.bytes)
        assertEquals("1.0 B", (8 of bits) format bytes)
    }
}
