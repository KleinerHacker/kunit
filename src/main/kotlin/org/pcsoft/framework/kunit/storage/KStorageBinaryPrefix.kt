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
import org.pcsoft.framework.kunit.KUnitTarget

/**
 * A binary (IEC 80000-13) magnitude prefix for the storage group, based on powers of **1024**
 * (`2^10`) rather than the decimal powers of 1000 used by
 * [org.pcsoft.framework.kunit.KUnitPrefix]. This is what lets a data amount distinguish e.g. a
 * kilobyte (`kilo`, 1000 B) from a kibibyte (`kibi`, 1024 B).
 *
 * Like the SI prefixes, a binary prefix is **not** stored as part of a value - it only scales a raw
 * value at the input/output boundary (via the `infix` constructors in `KStorageUnitPrefix.kt`, and as
 * a [KBinaryScaledUnit] target for `valueAs`/`toString`).
 *
 * Example:
 * ```kotlin
 * (1 kibi bytes).value // 1024.0
 * (1 mebi bytes).value // 1048576.0
 * ```
 */
enum class KStorageBinaryPrefix(val symbol: String, val factor: Double) {
    /** Kibi: factor 1024^1 = 1024 (e.g. kibibyte = 1024 bytes). */
    KIBI("Ki", 1024.0),

    /** Mebi: factor 1024^2 = 1 048 576. */
    MEBI("Mi", 1_048_576.0),

    /** Gibi: factor 1024^3. */
    GIBI("Gi", 1_073_741_824.0),

    /** Tebi: factor 1024^4. */
    TEBI("Ti", 1_099_511_627_776.0),

    /** Pebi: factor 1024^5. */
    PEBI("Pi", 1_125_899_906_842_624.0),

    /** Exbi: factor 1024^6. */
    EXBI("Ei", 1_152_921_504_606_846_976.0),

    /** Zebi: factor 1024^7. */
    ZEBI("Zi", 1_180_591_620_717_411_303_424.0),

    /** Yobi: factor 1024^8. */
    YOBI("Yi", 1_208_925_819_614_629_174_706_176.0)
}

/**
 * A [KUnit] combined with a [KStorageBinaryPrefix], e.g. "KiB" = `KIBI with bytes`.
 *
 * Used as a [KUnitTarget] wherever a plain [KUnit]/[org.pcsoft.framework.kunit.KScaledUnit] would be
 * accepted (e.g. `KStorageUnitInstance.valueAs`, `KMixedUnitInstance.valueAs`/`toString`), so a storage
 * value can be read/formatted in a binary-scaled unit. It is the binary counterpart of
 * [org.pcsoft.framework.kunit.KScaledUnit] and is resolved identically (linear per dimension).
 *
 * Example:
 * ```kotlin
 * val kib = KStorageBinaryPrefix.KIBI with bytes
 * kib.baseValue // 1024.0
 * kib.symbol    // "KiB"
 * ```
 */
data class KBinaryScaledUnit(val prefix: KStorageBinaryPrefix, val unit: KUnit) : KUnitTarget {
    /** Combined conversion factor to the group's base unit: [prefix].factor * [unit].baseValue. */
    val baseValue: Double get() = prefix.factor * unit.baseValue

    /** The combined display symbol, e.g. `"KiB"` for `KIBI with BYTE`. */
    val symbol: String get() = prefix.symbol + unit.symbol
}

/**
 * Combines a binary prefix and a unit into a [KBinaryScaledUnit], e.g. `KStorageBinaryPrefix.KIBI with bytes`.
 *
 * Example:
 * ```kotlin
 * (1 mebi bytes).valueAs(KStorageBinaryPrefix.KIBI with bytes) // 1024.0 (1 MiB = 1024 KiB)
 * ```
 */
infix fun KStorageBinaryPrefix.with(unit: KUnit): KBinaryScaledUnit = KBinaryScaledUnit(this, unit)
