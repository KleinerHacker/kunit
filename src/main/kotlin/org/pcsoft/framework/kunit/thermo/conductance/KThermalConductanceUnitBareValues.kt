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

// Value-1 thermal conductance templates for the named units of the group, used with `of`/`into`
// (`0.4 of wattsPerKelvin`). Prefixed forms live in KThermalConductanceUnitExtensions.kt.

/** 1 W/K ([KThermalConductanceUnit.WATT_PER_KELVIN]), the group's base unit. */
val wattsPerKelvin: KThermalConductanceUnitInstance =
    thermalConductanceOfUnit(KThermalConductanceUnit.WATT_PER_KELVIN)

/** 1 Btu/(h·°F) ([KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT], ≈ 0.5275 W/K). */
val btusPerHourFahrenheit: KThermalConductanceUnitInstance =
    thermalConductanceOfUnit(KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT)
