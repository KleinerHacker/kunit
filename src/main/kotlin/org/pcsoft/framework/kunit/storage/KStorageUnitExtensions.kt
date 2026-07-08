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

// Prefixed, value-1 storage templates. A fraction of a bit is not a meaningful data amount, so the
// `bytes`/`bits` properties hang **only** on the augmenting (supra-unity) SI builder and on the binary
// IEC builder - never on the diminishing builder. That makes `kilo.bytes`/`kibi.bytes` valid while
// `milli.bytes` is a **compile error** (there is no `bytes` property reachable from `milli`).

/** Decimal SI-prefixed bytes, e.g. `kilo.bytes` = 1000 B, `mega.bytes` = 1e6 B. */
val KAugmentingPrefixBuilder.bytes: KStorageUnitInstance get() = storageOf(prefix.factor * KStorageUnit.BYTE.baseValue)

/** Decimal SI-prefixed bits, e.g. `kilo.bits`. */
val KAugmentingPrefixBuilder.bits: KStorageUnitInstance get() = storageOf(prefix.factor * KStorageUnit.BIT.baseValue)

/** Binary IEC-prefixed bytes, e.g. `kibi.bytes` = 1024 B, `mebi.bytes` = 1 048 576 B. */
val KStorageBinaryPrefixBuilder.bytes: KStorageUnitInstance get() = storageOf(factor * KStorageUnit.BYTE.baseValue)

/** Binary IEC-prefixed bits, e.g. `kibi.bits`. */
val KStorageBinaryPrefixBuilder.bits: KStorageUnitInstance get() = storageOf(factor * KStorageUnit.BIT.baseValue)
