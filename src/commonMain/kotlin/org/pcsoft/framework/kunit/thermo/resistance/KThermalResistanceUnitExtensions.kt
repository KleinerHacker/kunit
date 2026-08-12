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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 absolute thermal resistance templates: one property per named unit on the prefix
// builder. Use with `of`/`into`, e.g. `250 of milli.kelvinsPerWatt`.

private fun prefixedThermalResistance(
    builder: KPrefixBuilder,
    unit: KThermalResistanceUnit
): KThermalResistanceUnitInstance =
    thermalResistanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed kelvins per watt, e.g. `milli.kelvinsPerWatt`, `kilo.kelvinsPerWatt`. */
val KPrefixBuilder.kelvinsPerWatt: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.KELVIN_PER_WATT)

/** Prefixed degrees Celsius per watt, e.g. `milli.degreesCelsiusPerWatt`. */
val KPrefixBuilder.degreesCelsiusPerWatt: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.DEGREE_CELSIUS_PER_WATT)

/** Prefixed hour degrees-Fahrenheit per Btu, e.g. `kilo.hourFahrenheitPerBtu`. */
val KPrefixBuilder.hourFahrenheitPerBtu: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU)
