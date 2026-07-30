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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 heat flux density templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `1.361 of kilo.wattsPerSquareMeter`.

private fun prefixedHeatFluxDensity(
    builder: KPrefixBuilder,
    unit: KHeatFluxDensityUnit,
): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per square meter, e.g. `kilo.wattsPerSquareMeter` (kW/m²), `milli.wattsPerSquareMeter`. */
val KPrefixBuilder.wattsPerSquareMeter: KHeatFluxDensityUnitInstance
    get() = prefixedHeatFluxDensity(this, KHeatFluxDensityUnit.WATT_PER_SQUARE_METER)

/** Prefixed Btu per hour-square foot, e.g. `kilo.btusPerHourSquareFoot`. */
val KPrefixBuilder.btusPerHourSquareFoot: KHeatFluxDensityUnitInstance
    get() = prefixedHeatFluxDensity(this, KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT)

/** Prefixed calories per second-square centimeter, e.g. `milli.caloriesPerSecondSquareCentimeter`. */
val KPrefixBuilder.caloriesPerSecondSquareCentimeter: KHeatFluxDensityUnitInstance
    get() = prefixedHeatFluxDensity(this, KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER)
