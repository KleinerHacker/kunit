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

/**
 * A binary (IEC 80000-13) magnitude prefix for the storage group, based on powers of **1024** (`2^10`)
 * rather than the decimal powers of 1000 used by [org.pcsoft.framework.kunit.KUnitPrefix]. This is what
 * lets a data amount distinguish a kilobyte (`kilo`, 1000 B) from a kibibyte (`kibi`, 1024 B).
 *
 * Like the SI prefixes, a binary prefix is exposed as a **builder** ([KStorageBinaryPrefixBuilder]) that
 * turns the `bytes`/`bits` tokens into a prefixed, value-1 template for use with `of`/`into`
 * (e.g. `4 of kibi.bytes`, `v into mebi.bytes`).
 *
 * Example:
 * ```kotlin
 * (1 of kibi.bytes).value // 1024.0
 * (1 of mebi.bytes).value // 1048576.0
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
 * A binary prefix **builder** (the IEC counterpart of
 * [org.pcsoft.framework.kunit.KAugmentingPrefixBuilder]): it turns the storage `bytes`/`bits` tokens
 * into a value-1 template scaled by a power of 1024, e.g. `kibi.bytes` (1024 B). Since binary prefixes
 * are inherently *supra-unity*, only the `bytes`/`bits` properties (declared in
 * `KStorageUnitExtensions.kt`) hang on it - it mirrors the augmenting SI builder exactly.
 */
class KStorageBinaryPrefixBuilder internal constructor(internal val factor: Double)

/** Kibi builder (1024^1). Use as `kibi.bytes`, `kibi.bits`. */
val kibi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.KIBI.factor)

/** Mebi builder (1024^2). */
val mebi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.MEBI.factor)

/** Gibi builder (1024^3). */
val gibi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.GIBI.factor)

/** Tebi builder (1024^4). */
val tebi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.TEBI.factor)

/** Pebi builder (1024^5). */
val pebi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.PEBI.factor)

/** Exbi builder (1024^6). */
val exbi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.EXBI.factor)

/** Zebi builder (1024^7). */
val zebi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.ZEBI.factor)

/** Yobi builder (1024^8). */
val yobi: KStorageBinaryPrefixBuilder = KStorageBinaryPrefixBuilder(KStorageBinaryPrefix.YOBI.factor)
