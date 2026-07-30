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

// Value-1 thermal conductivity templates for the named units of the group, used with `of`/`into`
// (`401 of wattsPerMeterKelvin`, `k into btusPerHourFootFahrenheit`).

/** 1 watt per meter-kelvin ([KThermalConductivityUnit.WATT_PER_METER_KELVIN]), the group's base unit. */
val wattsPerMeterKelvin: KThermalConductivityUnitInstance =
    thermalConductivityOfUnit(KThermalConductivityUnit.WATT_PER_METER_KELVIN)

/**
 * 1 Btu per hour-foot-degree Fahrenheit
 * ([KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT], ≈ 1.7307 W/(m·K)).
 */
val btusPerHourFootFahrenheit: KThermalConductivityUnitInstance =
    thermalConductivityOfUnit(KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT)

/**
 * 1 calorie per second-centimeter-kelvin
 * ([KThermalConductivityUnit.CALORIE_PER_SECOND_CENTIMETER_KELVIN], 418.4 W/(m·K)).
 */
val caloriesPerSecondCentimeterKelvin: KThermalConductivityUnitInstance =
    thermalConductivityOfUnit(KThermalConductivityUnit.CALORIE_PER_SECOND_CENTIMETER_KELVIN)
