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

import org.pcsoft.framework.kunit.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.storage.toStorage
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.toTime

// Cross-group operators that let the core units (storage, time) combine *directly* into a strongly
// typed data rate, and back, without the caller ever handling a raw KMixedUnitInstance. They live in
// the datarate package because it may depend on storage/time (the reverse must never happen), and each
// simply delegates to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.
//
// Storage is a plain one-dimensional wrapper (no exponent-specialized subtype like distance's
// length/area/volume), so - unlike speed - there is no general "base type" fallback to provide:
// `KStorageUnitInstance` is the only storage receiver.

/**
 * Divides a storage amount by a time to obtain a [KDataRateUnitInstance] (`storage / time = data rate`).
 *
 * Example:
 * ```kotlin
 * val r = 100.bytes / 10.seconds             // KDataRateUnitInstance, 10 B/s
 * r.valueAs(KDataRateUnit.BITS_PER_SECOND)   // 80.0
 * ```
 */
operator fun KStorageUnitInstance.div(other: KTimeUnitInstance): KDataRateUnitInstance =
    (this.toUnit() / other.toUnit()).toDataRate()

/**
 * Multiplies a data rate by a time to obtain the transferred storage amount (`data rate * time = storage`).
 *
 * Example:
 * ```kotlin
 * val r = 10.bytesPerSecond
 * (r * 60.seconds).valueAs(KStorageUnit.BYTE) // 600.0
 * ```
 */
operator fun KDataRateUnitInstance.times(other: KTimeUnitInstance): KStorageUnitInstance =
    (this.toUnit() * other.toUnit()).toStorage()

/**
 * Multiplies a time by a data rate to obtain the transferred storage amount (`time * data rate = storage`);
 * the commutative counterpart of [KDataRateUnitInstance.times].
 *
 * Example:
 * ```kotlin
 * (60.seconds * 10.bytesPerSecond).valueAs(KStorageUnit.BYTE) // 600.0
 * ```
 */
operator fun KTimeUnitInstance.times(other: KDataRateUnitInstance): KStorageUnitInstance =
    (this.toUnit() * other.toUnit()).toStorage()

/**
 * Divides a storage amount by a data rate to obtain the required time (`storage / data rate = time`).
 *
 * Example:
 * ```kotlin
 * val r = 10.bytesPerSecond
 * (600.bytes / r).valueAs(KTimeUnit.SECOND) // 60.0
 * ```
 */
operator fun KStorageUnitInstance.div(other: KDataRateUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
