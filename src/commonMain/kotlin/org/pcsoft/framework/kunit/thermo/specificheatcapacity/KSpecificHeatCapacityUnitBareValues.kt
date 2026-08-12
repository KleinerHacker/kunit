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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

// Value-1 specific heat capacity templates for the named units of the group, used with `of`/`into`
// (`4184 of joulesPerKilogramKelvin`, `c into caloriesPerGramKelvin`).

/** 1 joule per kilogram-kelvin ([KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN]), the base unit. */
val joulesPerKilogramKelvin: KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityOfUnit(KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN)

/** 1 calorie per gram-kelvin ([KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN], 4184 J/(kg·K)). */
val caloriesPerGramKelvin: KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityOfUnit(KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN)

/** 1 Btu per pound-degree Fahrenheit ([KSpecificHeatCapacityUnit.BTU_PER_POUND_FAHRENHEIT], 4186.8 J/(kg·K)). */
val btusPerPoundFahrenheit: KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityOfUnit(KSpecificHeatCapacityUnit.BTU_PER_POUND_FAHRENHEIT)
