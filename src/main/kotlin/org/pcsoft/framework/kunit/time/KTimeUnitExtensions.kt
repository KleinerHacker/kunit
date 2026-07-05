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

// Time creator extension properties (bare unit references live in `KTimeUnitBareValues.kt`).

private fun of(value: Number, unit: KTimeUnit): KTimeUnitInstance = timeUnitInstanceOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure time value in seconds from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 5.seconds.value   // 5.0
 * 5L.seconds.value  // 5.0
 * 5.0f.seconds.value // 5.0
 * ```
 */
val Number.seconds: KTimeUnitInstance get() = of(this, KTimeUnit.SECOND)

/** Creates a pure time value in minutes. Example: `2.minutes.value // 120.0` (normalized to seconds). */
val Number.minutes: KTimeUnitInstance get() = of(this, KTimeUnit.MINUTE)

/** Creates a pure time value in hours. Example: `2.hours.value // 7200.0` (normalized to seconds). */
val Number.hours: KTimeUnitInstance get() = of(this, KTimeUnit.HOUR)

/** Creates a pure time value in days. Example: `1.days.value // 86400.0` (normalized to seconds). */
val Number.days: KTimeUnitInstance get() = of(this, KTimeUnit.DAY)
