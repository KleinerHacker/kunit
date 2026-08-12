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

// Value-1 absolute thermal resistance templates for the named units of the group, used with `of`/`into`
// (`2.5 of kelvinsPerWatt`). Prefixed forms live in KThermalResistanceUnitExtensions.kt.

/** 1 K/W ([KThermalResistanceUnit.KELVIN_PER_WATT]), the group's base unit. */
val kelvinsPerWatt: KThermalResistanceUnitInstance =
    thermalResistanceOfUnit(KThermalResistanceUnit.KELVIN_PER_WATT)

/** 1 °C/W ([KThermalResistanceUnit.DEGREE_CELSIUS_PER_WATT]), the datasheet spelling; = 1 K/W. */
val degreesCelsiusPerWatt: KThermalResistanceUnitInstance =
    thermalResistanceOfUnit(KThermalResistanceUnit.DEGREE_CELSIUS_PER_WATT)

/** 1 h·°F/Btu ([KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU], ≈ 1.8956 K/W). */
val hourFahrenheitPerBtu: KThermalResistanceUnitInstance =
    thermalResistanceOfUnit(KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU)
