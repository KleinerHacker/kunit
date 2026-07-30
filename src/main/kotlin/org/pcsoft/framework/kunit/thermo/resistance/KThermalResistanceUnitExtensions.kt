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

// Prefixed, value-1 thermal resistance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `500 of milli.squareMeterKelvinPerWatt`.

private fun prefixedThermalResistance(
    builder: KPrefixBuilder,
    unit: KThermalResistanceUnit,
): KThermalResistanceUnitInstance = thermalResistanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed square meter-kelvin per watt, e.g. `milli.squareMeterKelvinPerWatt`, `kilo.squareMeterKelvinPerWatt`. */
val KPrefixBuilder.squareMeterKelvinPerWatt: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT)

/** Prefixed imperial R-value units, e.g. `milli.hourSquareFootFahrenheitPerBtu`. */
val KPrefixBuilder.hourSquareFootFahrenheitPerBtu: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU)

/** Prefixed clo, e.g. `milli.clo`. */
val KPrefixBuilder.clo: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.CLO)

/** Prefixed tog, e.g. `milli.tog`. */
val KPrefixBuilder.tog: KThermalResistanceUnitInstance
    get() = prefixedThermalResistance(this, KThermalResistanceUnit.TOG)
