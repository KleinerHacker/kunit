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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

// Value-1 molar heat capacity templates for the named units of the group, used with `of`/`into`
// (`29.1 of joulesPerMoleKelvin`, `cp into caloriesPerMoleKelvin`).

/** 1 joule per mole-kelvin ([KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN]), the group's base unit. */
val joulesPerMoleKelvin: KMolarHeatCapacityUnitInstance =
    molarHeatCapacityOfUnit(KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN)

/** 1 calorie per mole-kelvin ([KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN], 4.184 J/(mol·K)). */
val caloriesPerMoleKelvin: KMolarHeatCapacityUnitInstance =
    molarHeatCapacityOfUnit(KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN)
