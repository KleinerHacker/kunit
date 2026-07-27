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

package org.pcsoft.framework.kunit.energy

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 energy templates: one property per energy unit on the prefix builder (e.g. `kilo.joules`
// = 1000 J, `mega.joules` = 1e6 J). Energy accepts *any* magnitude, so the properties hang on the common base
// [KPrefixBuilder]. Use with `of`/`into`, e.g. `3 of kilo.joules`, `w into mega.joules`.

private fun prefixedEnergy(builder: KPrefixBuilder, unit: KEnergyUnit): KEnergyUnitInstance =
    energyInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules, e.g. `kilo.joules` = 1000 J, `mega.joules` = 1e6 J. */
val KPrefixBuilder.joules: KEnergyUnitInstance get() = prefixedEnergy(this, KEnergyUnit.JOULE)

/** Prefixed ergs, e.g. `mega.ergs`. */
val KPrefixBuilder.ergs: KEnergyUnitInstance get() = prefixedEnergy(this, KEnergyUnit.ERG)

/** Prefixed calories, e.g. `kilo.calories` (the "food calorie", 4184 J). */
val KPrefixBuilder.calories: KEnergyUnitInstance get() = prefixedEnergy(this, KEnergyUnit.CALORIE)

/** Prefixed electron volts, e.g. `mega.electronVolts` (MeV). */
val KPrefixBuilder.electronVolts: KEnergyUnitInstance get() = prefixedEnergy(this, KEnergyUnit.ELECTRON_VOLT)

/** Prefixed British thermal units, e.g. `kilo.britishThermalUnits`. */
val KPrefixBuilder.britishThermalUnits: KEnergyUnitInstance
    get() = prefixedEnergy(this, KEnergyUnit.BRITISH_THERMAL_UNIT)
