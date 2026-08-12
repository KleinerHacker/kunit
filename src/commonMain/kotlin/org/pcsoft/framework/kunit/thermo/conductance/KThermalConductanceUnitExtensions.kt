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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 thermal conductance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `400 of milli.wattsPerKelvin`.

private fun prefixedThermalConductance(
    builder: KPrefixBuilder,
    unit: KThermalConductanceUnit
): KThermalConductanceUnitInstance =
    thermalConductanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per kelvin, e.g. `milli.wattsPerKelvin`, `kilo.wattsPerKelvin`. */
val KPrefixBuilder.wattsPerKelvin: KThermalConductanceUnitInstance
    get() = prefixedThermalConductance(this, KThermalConductanceUnit.WATT_PER_KELVIN)

/** Prefixed Btu per hour degree-Fahrenheit, e.g. `kilo.btusPerHourFahrenheit`. */
val KPrefixBuilder.btusPerHourFahrenheit: KThermalConductanceUnitInstance
    get() = prefixedThermalConductance(this, KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT)
