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

// Value-1 volumetric flow templates for the named units of the group, used with `of`/`into`
// (`5 of litersPerSecond`, `q into cubicMetersPerHour`).

/** 1 cubic meter per second ([KVolumeFlowUnit.CUBIC_METER_PER_SECOND]), the group's base unit. */
val cubicMetersPerSecond: KVolumeFlowUnitInstance = volumeFlowOfUnit(KVolumeFlowUnit.CUBIC_METER_PER_SECOND)

/** 1 cubic meter per hour ([KVolumeFlowUnit.CUBIC_METER_PER_HOUR], 1/3600 m³/s). */
val cubicMetersPerHour: KVolumeFlowUnitInstance = volumeFlowOfUnit(KVolumeFlowUnit.CUBIC_METER_PER_HOUR)

/** 1 liter per second ([KVolumeFlowUnit.LITER_PER_SECOND], 0.001 m³/s). */
val litersPerSecond: KVolumeFlowUnitInstance = volumeFlowOfUnit(KVolumeFlowUnit.LITER_PER_SECOND)

/** 1 liter per minute ([KVolumeFlowUnit.LITER_PER_MINUTE], 0.001/60 m³/s). */
val litersPerMinute: KVolumeFlowUnitInstance = volumeFlowOfUnit(KVolumeFlowUnit.LITER_PER_MINUTE)

/** 1 US gallon per minute ([KVolumeFlowUnit.US_GALLON_PER_MINUTE], ≈ 6.309e-5 m³/s). */
val usGallonsPerMinute: KVolumeFlowUnitInstance = volumeFlowOfUnit(KVolumeFlowUnit.US_GALLON_PER_MINUTE)
