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

package org.pcsoft.framework.kunit.thermo.conductivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 thermal conductivity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `40 of milli.wattsPerMeterKelvin` (a typical insulation material).

private fun prefixedThermalConductivity(
    builder: KPrefixBuilder,
    unit: KThermalConductivityUnit,
): KThermalConductivityUnitInstance = thermalConductivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per meter-kelvin, e.g. `milli.wattsPerMeterKelvin` (mW/(m·K)), `kilo.wattsPerMeterKelvin`. */
val KPrefixBuilder.wattsPerMeterKelvin: KThermalConductivityUnitInstance
    get() = prefixedThermalConductivity(this, KThermalConductivityUnit.WATT_PER_METER_KELVIN)

/** Prefixed Btu per hour-foot-degree Fahrenheit, e.g. `milli.btusPerHourFootFahrenheit`. */
val KPrefixBuilder.btusPerHourFootFahrenheit: KThermalConductivityUnitInstance
    get() = prefixedThermalConductivity(this, KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT)

/** Prefixed calories per second-centimeter-kelvin, e.g. `milli.caloriesPerSecondCentimeterKelvin`. */
val KPrefixBuilder.caloriesPerSecondCentimeterKelvin: KThermalConductivityUnitInstance
    get() = prefixedThermalConductivity(this, KThermalConductivityUnit.CALORIE_PER_SECOND_CENTIMETER_KELVIN)
