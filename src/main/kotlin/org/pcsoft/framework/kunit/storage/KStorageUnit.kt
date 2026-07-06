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

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **storage** group (digital information / data amount).
 *
 * The group is one-dimensional (no exponent-specialized subtypes) and its base unit is [BYTE]
 * ([baseValue] `1.0`); a [BIT] is an eighth of a byte ([baseValue] `0.125`, since 1 byte = 8 bit).
 *
 * Unlike the other groups, storage deliberately does **not** offer the *diminishing* SI prefixes
 * (deci, centi, milli, …) - a fraction of a bit is meaningless as a data amount. It does, however,
 * offer an additional, binary prefix system ([org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix],
 * `Ki`, `Mi`, `Gi`, …) so a value can distinguish the decimal step 1000 (`kilo`) from the binary
 * step 1024 (`kibi`).
 *
 * Example:
 * ```kotlin
 * 1.bytes.valueAs(bits)                // 8.0
 * (1 kilo bytes).value                 // 1000.0 (decimal)
 * (1 kibi bytes).value                 // 1024.0 (binary)
 * ```
 */
enum class KStorageUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Bit ("bit"): the smallest unit of digital information, an eighth of a [BYTE] ([baseValue] `0.125`). */
    BIT("bit", 0.125),

    /** Byte ("B"): the base unit of the storage group ([baseValue] `1.0`), equal to 8 [BIT]s. */
    BYTE("B", 1.0);

    companion object {
        /** The base unit of the storage group: [BYTE]. */
        val BASE: KStorageUnit = BYTE
    }
}
