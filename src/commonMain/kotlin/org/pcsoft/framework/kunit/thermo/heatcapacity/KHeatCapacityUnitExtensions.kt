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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 heat capacity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `4.184 of kilo.joulesPerKelvin`.

private fun prefixedHeatCapacity(builder: KPrefixBuilder, unit: KHeatCapacityUnit): KHeatCapacityUnitInstance =
    heatCapacityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per kelvin, e.g. `kilo.joulesPerKelvin` (kJ/K), `mega.joulesPerKelvin` (MJ/K). */
val KPrefixBuilder.joulesPerKelvin: KHeatCapacityUnitInstance
    get() = prefixedHeatCapacity(this, KHeatCapacityUnit.JOULE_PER_KELVIN)

/** Prefixed calories per kelvin, e.g. `kilo.caloriesPerKelvin` (kcal/K). */
val KPrefixBuilder.caloriesPerKelvin: KHeatCapacityUnitInstance
    get() = prefixedHeatCapacity(this, KHeatCapacityUnit.CALORIE_PER_KELVIN)

/** Prefixed Btu per degree Fahrenheit, e.g. `kilo.btusPerFahrenheit`. */
val KPrefixBuilder.btusPerFahrenheit: KHeatCapacityUnitInstance
    get() = prefixedHeatCapacity(this, KHeatCapacityUnit.BTU_PER_FAHRENHEIT)
