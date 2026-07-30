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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 specific energy templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `334 of kilo.joulesPerKilogram`.

private fun prefixedSpecificEnergy(builder: KPrefixBuilder, unit: KSpecificEnergyUnit): KSpecificEnergyUnitInstance =
    specificEnergyInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per kilogram, e.g. `kilo.joulesPerKilogram` (kJ/kg), `mega.joulesPerKilogram` (MJ/kg). */
val KPrefixBuilder.joulesPerKilogram: KSpecificEnergyUnitInstance
    get() = prefixedSpecificEnergy(this, KSpecificEnergyUnit.JOULE_PER_KILOGRAM)

/** Prefixed calories per gram, e.g. `kilo.caloriesPerGram` (kcal/g). */
val KPrefixBuilder.caloriesPerGram: KSpecificEnergyUnitInstance
    get() = prefixedSpecificEnergy(this, KSpecificEnergyUnit.CALORIE_PER_GRAM)

/** Prefixed watt-hours per kilogram, e.g. `kilo.wattHoursPerKilogram` (kWh/kg). */
val KPrefixBuilder.wattHoursPerKilogram: KSpecificEnergyUnitInstance
    get() = prefixedSpecificEnergy(this, KSpecificEnergyUnit.WATT_HOUR_PER_KILOGRAM)

/** Prefixed Btu per pound, e.g. `kilo.btusPerPound`. */
val KPrefixBuilder.btusPerPound: KSpecificEnergyUnitInstance
    get() = prefixedSpecificEnergy(this, KSpecificEnergyUnit.BTU_PER_POUND)
