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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molar heat capacity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `29.1 of milli.joulesPerMoleKelvin`.

private fun prefixedMolarHeatCapacity(
    builder: KPrefixBuilder,
    unit: KMolarHeatCapacityUnit,
): KMolarHeatCapacityUnitInstance = molarHeatCapacityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per mole-kelvin, e.g. `kilo.joulesPerMoleKelvin` (kJ/(mol·K)). */
val KPrefixBuilder.joulesPerMoleKelvin: KMolarHeatCapacityUnitInstance
    get() = prefixedMolarHeatCapacity(this, KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN)

/** Prefixed calories per mole-kelvin, e.g. `kilo.caloriesPerMoleKelvin`. */
val KPrefixBuilder.caloriesPerMoleKelvin: KMolarHeatCapacityUnitInstance
    get() = prefixedMolarHeatCapacity(this, KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN)
