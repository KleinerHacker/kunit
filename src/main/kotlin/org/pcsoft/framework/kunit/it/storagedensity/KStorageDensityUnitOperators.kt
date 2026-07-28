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

package org.pcsoft.framework.kunit.it.storagedensity

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.toArea
import org.pcsoft.framework.kunit.it.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.it.storage.toStorage

// Cross-group operators that let the core units (storage, area) combine *directly* into a strongly typed
// storage density, and back, without the caller ever handling a raw KMixedUnitInstance. They live in the
// storagedensity package because it may depend on storage/distance (the reverse must never happen), and
// each simply delegates to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Divides a storage amount by an area to obtain a [KStorageDensityUnitInstance]
 * (`storage / area = storage density`).
 *
 * Example:
 * ```kotlin
 * val d = 256.bytes / (2.meters pow 2)          // KStorageDensityUnitInstance, 64 B/m²
 * ```
 */
operator fun KStorageUnitInstance.div(other: KAreaUnitInstance): KStorageDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toStorageDensity()

/**
 * Multiplies a storage density by an area to obtain the stored storage amount
 * (`storage density * area = storage`).
 *
 * Example:
 * ```kotlin
 * val d = 256.bytes / (2.meters pow 2)
 * (d * (2.meters pow 2)).valueAs(KStorageUnit.BYTE) // 256.0
 * ```
 */
operator fun KStorageDensityUnitInstance.times(other: KAreaUnitInstance): KStorageUnitInstance =
    (this.toUnit() * other.toUnit()).toStorage()

/**
 * Multiplies an area by a storage density to obtain the stored storage amount
 * (`area * storage density = storage`); the commutative counterpart of [KStorageDensityUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KStorageDensityUnitInstance): KStorageUnitInstance =
    (this.toUnit() * other.toUnit()).toStorage()

/**
 * Divides a storage amount by a storage density to obtain the required area
 * (`storage / storage density = area`).
 *
 * Example:
 * ```kotlin
 * val d = 256.bytes / (2.meters pow 2)
 * (256.bytes / d).valueAs(KDistanceDerivedUnit.SQUARE_METER) // area that holds 256 bytes at density d
 * ```
 */
operator fun KStorageUnitInstance.div(other: KStorageDensityUnitInstance): KAreaUnitInstance =
    (this.toUnit() / other.toUnit()).toArea()
