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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 specific heat capacity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `4.184 of kilo.joulesPerKilogramKelvin`.

private fun prefixedSpecificHeatCapacity(
    builder: KPrefixBuilder,
    unit: KSpecificHeatCapacityUnit,
): KSpecificHeatCapacityUnitInstance = specificHeatCapacityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per kilogram-kelvin, e.g. `kilo.joulesPerKilogramKelvin` (kJ/(kg·K)). */
val KPrefixBuilder.joulesPerKilogramKelvin: KSpecificHeatCapacityUnitInstance
    get() = prefixedSpecificHeatCapacity(this, KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN)

/** Prefixed calories per gram-kelvin, e.g. `kilo.caloriesPerGramKelvin`. */
val KPrefixBuilder.caloriesPerGramKelvin: KSpecificHeatCapacityUnitInstance
    get() = prefixedSpecificHeatCapacity(this, KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN)

/** Prefixed Btu per pound-degree Fahrenheit, e.g. `kilo.btusPerPoundFahrenheit`. */
val KPrefixBuilder.btusPerPoundFahrenheit: KSpecificHeatCapacityUnitInstance
    get() = prefixedSpecificHeatCapacity(this, KSpecificHeatCapacityUnit.BTU_PER_POUND_FAHRENHEIT)
