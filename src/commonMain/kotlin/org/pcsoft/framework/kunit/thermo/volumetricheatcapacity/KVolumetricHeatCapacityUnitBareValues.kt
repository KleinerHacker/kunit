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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

// Value-1 volumetric heat capacity templates for the named units of the group, used with `of`/`into`
// (`4.18 of mega.joulesPerCubicMeterKelvin`). Prefixed forms live in
// KVolumetricHeatCapacityUnitExtensions.kt.

/** 1 J/(m³·K) ([KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN]), the group's base unit. */
val joulesPerCubicMeterKelvin: KVolumetricHeatCapacityUnitInstance =
    volumetricHeatCapacityOfUnit(KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN)

/** 1 cal/(cm³·K) ([KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN], 4.184e6 J/(m³·K)). */
val caloriesPerCubicCentimeterKelvin: KVolumetricHeatCapacityUnitInstance =
    volumetricHeatCapacityOfUnit(KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN)
