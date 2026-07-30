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

package org.pcsoft.framework.kunit.thermo.heatcapacity

// Value-1 heat capacity templates for the named units of the group, used with `of`/`into`
// (`5 of joulesPerKelvin`, `c into caloriesPerKelvin`).

/** 1 joule per kelvin ([KHeatCapacityUnit.JOULE_PER_KELVIN]), the group's base unit. */
val joulesPerKelvin: KHeatCapacityUnitInstance = heatCapacityOfUnit(KHeatCapacityUnit.JOULE_PER_KELVIN)

/** 1 thermochemical calorie per kelvin ([KHeatCapacityUnit.CALORIE_PER_KELVIN], 4.184 J/K). */
val caloriesPerKelvin: KHeatCapacityUnitInstance = heatCapacityOfUnit(KHeatCapacityUnit.CALORIE_PER_KELVIN)

/** 1 Btu per degree Fahrenheit ([KHeatCapacityUnit.BTU_PER_FAHRENHEIT], ≈ 1899.1005 J/K). */
val btusPerFahrenheit: KHeatCapacityUnitInstance = heatCapacityOfUnit(KHeatCapacityUnit.BTU_PER_FAHRENHEIT)
