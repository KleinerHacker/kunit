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

// Data-rate creator extension properties (bare unit references live in `KDataRateUnitBareValues.kt`).

private fun of(value: Number, unit: KDataRateUnit): KDataRateUnitInstance = dataRateUnitInstanceOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure data-rate value in bytes per second from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 10.bytesPerSecond.value   // 10.0
 * 10L.bytesPerSecond.value  // 10.0
 * 10.0f.bytesPerSecond.value // 10.0
 * ```
 */
val Number.bytesPerSecond: KDataRateUnitInstance get() = of(this, KDataRateUnit.BYTES_PER_SECOND)

/** Creates a pure data-rate value in bits per second. Example: `8.bitsPerSecond.value // 1.0` (normalized to B/s). */
val Number.bitsPerSecond: KDataRateUnitInstance get() = of(this, KDataRateUnit.BITS_PER_SECOND)
