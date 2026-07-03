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

package org.pcsoft.framework.kunit.time

// Bare unit references, usable both as a KUnitTarget (e.g. `t.valueAs(hours)`) and as the `unit`
// argument of the time-group prefix `infix` functions (e.g. `5 milli seconds`, see
// `KTimeUnitPrefix.kt`). Only SECOND is an SI unit and therefore sensibly combinable with a prefix, but
// the others are still accepted since KUnitPrefix is a purely mathematical scale factor.

/** Bare reference to [KTimeUnit.SECOND], for use with [valueAs][KTimeUnitInstance.valueAs] or the prefix `infix` functions. */
val seconds: KTimeUnit = KTimeUnit.SECOND

/** Bare reference to [KTimeUnit.MINUTE]. */
val minutes: KTimeUnit = KTimeUnit.MINUTE

/** Bare reference to [KTimeUnit.HOUR]. */
val hours: KTimeUnit = KTimeUnit.HOUR

/** Bare reference to [KTimeUnit.DAY]. */
val days: KTimeUnit = KTimeUnit.DAY

private fun of(value: Number, unit: KTimeUnit): KTimeUnitInstance = timeUnitInstanceOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure time value in seconds from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 5.seconds().value   // 5.0
 * 5L.seconds().value  // 5.0
 * 5.0f.seconds().value // 5.0
 * ```
 */
fun Number.seconds(): KTimeUnitInstance = of(this, KTimeUnit.SECOND)

/** Creates a pure time value in minutes. Example: `2.minutes().value // 120.0` (normalized to seconds). */
fun Number.minutes(): KTimeUnitInstance = of(this, KTimeUnit.MINUTE)

/** Creates a pure time value in hours. Example: `2.hours().value // 7200.0` (normalized to seconds). */
fun Number.hours(): KTimeUnitInstance = of(this, KTimeUnit.HOUR)

/** Creates a pure time value in days. Example: `1.days().value // 86400.0` (normalized to seconds). */
fun Number.days(): KTimeUnitInstance = of(this, KTimeUnit.DAY)
