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

package org.pcsoft.framework.kunit.thermo.molarenergy

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molar energy templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `286 of kilo.joulesPerMole`.

private fun prefixedMolarEnergy(builder: KPrefixBuilder, unit: KMolarEnergyUnit): KMolarEnergyUnitInstance =
    molarEnergyInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per mole, e.g. `kilo.joulesPerMole` (kJ/mol), `mega.joulesPerMole` (MJ/mol). */
val KPrefixBuilder.joulesPerMole: KMolarEnergyUnitInstance
    get() = prefixedMolarEnergy(this, KMolarEnergyUnit.JOULE_PER_MOLE)

/** Prefixed calories per mole, e.g. `kilo.caloriesPerMole` (kcal/mol). */
val KPrefixBuilder.caloriesPerMole: KMolarEnergyUnitInstance
    get() = prefixedMolarEnergy(this, KMolarEnergyUnit.CALORIE_PER_MOLE)

/** Prefixed electronvolts per entity, e.g. `milli.electronVoltsPerEntity` (meV per particle). */
val KPrefixBuilder.electronVoltsPerEntity: KMolarEnergyUnitInstance
    get() = prefixedMolarEnergy(this, KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY)
