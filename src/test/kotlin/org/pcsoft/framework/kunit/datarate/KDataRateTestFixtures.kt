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

import kotlin.math.abs

// Shared test fixtures for the data-rate group: the construction matrix and the small builder/tolerance
// helpers used by the data-rate test classes (KDataRateUnitInstanceTest, KDataRatePrefixTest,
// KDataRateMixedUnitTest).
//
//   * `dataRateUnitGenerators` — (creator-property lambda, unit) pairs; the lambda `{ n -> n.bytesPerSecond }`
//     exercises the number-extension **creator properties** (`5.bytesPerSecond`, `2.bitsPerSecond`).
//   * `dataRateBareValues` — the public bare-value alias tokens (`bytesPerSecond`, `bitsPerSecond`), the
//     argument the prefix `infix` functions take, so `5 mega bytesPerSecond` genuinely runs through the alias.
//
// The paired `KDataRateUnit` is used ONLY to compute expected values (`unit.baseValue`), never to build
// the instance under test — see the test-construction policy in CLAUDE.md.

/** All data-rate creator properties paired with the [KDataRateUnit] they construct. */
internal val dataRateUnitGenerators: List<Pair<(Number) -> KDataRateUnitInstance, KDataRateUnit>> = listOf(
    ({ n: Number -> n.bytesPerSecond }) to KDataRateUnit.BYTES_PER_SECOND,
    ({ n: Number -> n.bitsPerSecond }) to KDataRateUnit.BITS_PER_SECOND
)

/** All data-rate bare-value aliases, in the same order as [dataRateUnitGenerators]. */
internal val dataRateBareValues: List<KDataRateUnit> = listOf(bytesPerSecond, bitsPerSecond)

/** Builds a data-rate value of [n] in [unit] via that unit's creator property. */
internal fun dataRateOf(unit: KDataRateUnit, n: Number): KDataRateUnitInstance =
    dataRateUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the wide magnitude span (bit/s … yobibyte/s). */
internal fun dataRateDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)
