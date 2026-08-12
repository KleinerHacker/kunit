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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf

// Cross-group operators that let volume and time combine *directly* into a strongly typed volumetric
// flow, and back, without the caller ever handling a raw KMixedUnitInstance. They live in the volume
// flow package because it may depend on distance/time (the reverse must never happen). Each computes on
// the already normalized values (m³, s), which is exact and needs no dimension bridge.

/**
 * Divides a volume by a time to obtain a [KVolumeFlowUnitInstance] (`volume / time = volumetric flow`).
 *
 * Example:
 * ```kotlin
 * val q = (600 of liters) / (2 of minutes) // KVolumeFlowUnitInstance, 0.005 m³/s
 * ```
 */
operator fun KVolumeUnitInstance.div(other: KTimeUnitInstance): KVolumeFlowUnitInstance =
    volumeFlowInstanceOf(value / other.value)

/**
 * Multiplies a volumetric flow by a time to obtain the volume that flowed
 * (`volumetric flow * time = volume`).
 */
operator fun KVolumeFlowUnitInstance.times(other: KTimeUnitInstance): KVolumeUnitInstance =
    volumeOf(value * other.value)

/**
 * Multiplies a time by a volumetric flow to obtain the volume; the commutative counterpart of
 * [KVolumeFlowUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KVolumeFlowUnitInstance): KVolumeUnitInstance =
    volumeOf(value * other.value)

/**
 * Divides a volume by a volumetric flow to obtain the time it takes to move that volume
 * (`volume / volumetric flow = time`).
 */
operator fun KVolumeUnitInstance.div(other: KVolumeFlowUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / other.value)
