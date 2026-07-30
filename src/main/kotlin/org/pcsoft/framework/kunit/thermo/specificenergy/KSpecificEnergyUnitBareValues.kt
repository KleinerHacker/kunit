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

package org.pcsoft.framework.kunit.thermo.specificenergy

// Value-1 specific energy templates for the named units of the group, used with `of`/`into`
// (`334 of kilo.joulesPerKilogram`, `h into caloriesPerGram`).

/** 1 joule per kilogram ([KSpecificEnergyUnit.JOULE_PER_KILOGRAM]), the group's base unit. */
val joulesPerKilogram: KSpecificEnergyUnitInstance = specificEnergyOfUnit(KSpecificEnergyUnit.JOULE_PER_KILOGRAM)

/** 1 thermochemical calorie per gram ([KSpecificEnergyUnit.CALORIE_PER_GRAM], 4184 J/kg). */
val caloriesPerGram: KSpecificEnergyUnitInstance = specificEnergyOfUnit(KSpecificEnergyUnit.CALORIE_PER_GRAM)

/** 1 watt-hour per kilogram ([KSpecificEnergyUnit.WATT_HOUR_PER_KILOGRAM], 3600 J/kg). */
val wattHoursPerKilogram: KSpecificEnergyUnitInstance = specificEnergyOfUnit(KSpecificEnergyUnit.WATT_HOUR_PER_KILOGRAM)

/** 1 Btu per pound ([KSpecificEnergyUnit.BTU_PER_POUND], 2326 J/kg). */
val btusPerPound: KSpecificEnergyUnitInstance = specificEnergyOfUnit(KSpecificEnergyUnit.BTU_PER_POUND)
