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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 thermal expansion templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `12 of micro.perKelvin` (equivalent to `12 of ppmPerKelvin`).

private fun prefixedThermalExpansion(
    builder: KPrefixBuilder,
    unit: KThermalExpansionUnit,
): KThermalExpansionUnitInstance = thermalExpansionInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed per kelvin, e.g. `micro.perKelvin` (µ/K, the ppm/K spelling), `milli.perKelvin`. */
val KPrefixBuilder.perKelvin: KThermalExpansionUnitInstance
    get() = prefixedThermalExpansion(this, KThermalExpansionUnit.PER_KELVIN)

/** Prefixed per degree Fahrenheit, e.g. `micro.perFahrenheit`. */
val KPrefixBuilder.perFahrenheit: KThermalExpansionUnitInstance
    get() = prefixedThermalExpansion(this, KThermalExpansionUnit.PER_FAHRENHEIT)

/** Prefixed parts per million per kelvin, e.g. `milli.ppmPerKelvin`. */
val KPrefixBuilder.ppmPerKelvin: KThermalExpansionUnitInstance
    get() = prefixedThermalExpansion(this, KThermalExpansionUnit.PPM_PER_KELVIN)
