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

import kotlin.math.abs

// Shared test fixtures for the storage group: the construction matrix and the small builder/tolerance
// helpers used by the storage test classes (KStorageUnitInstanceTest, KStoragePrefixTest,
// KStorageMixedUnitTest).
//
//   * `storageUnitGenerators` — (creator-property lambda, unit) pairs; the lambda `{ n -> n.bytes }`
//     exercises the number-extension **creator properties** (`5.bytes`, `2.bits`).
//   * `storageBareValues` — the public bare-value alias tokens (`bytes`, `bits`), the argument the
//     prefix `infix` functions take, so `5 kilo bytes` genuinely runs through the alias.
//
// The paired `KStorageUnit` is used ONLY to compute expected values (`unit.baseValue`), never to build
// the instance under test — see the test-construction policy in CLAUDE.md.

/** All storage creator properties paired with the [KStorageUnit] they construct. */
internal val storageUnitGenerators: List<Pair<(Number) -> KStorageUnitInstance, KStorageUnit>> = listOf(
    ({ n: Number -> n.bytes }) to KStorageUnit.BYTE,
    ({ n: Number -> n.bits }) to KStorageUnit.BIT
)

/** All storage bare-value aliases, in the same order as [storageUnitGenerators]. */
internal val storageBareValues: List<KStorageUnit> = listOf(bytes, bits)

/** Builds a storage value of [n] in [unit] via that unit's creator property. */
internal fun mkStorage(unit: KStorageUnit, n: Number): KStorageUnitInstance =
    storageUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the wide magnitude span (bit … yobibyte). */
internal fun storageDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)
