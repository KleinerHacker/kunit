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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

// Value-1 heat transfer coefficient templates for the named units of the group, used with `of`/`into`
// (`1.3 of wattsPerSquareMeterKelvin`, `u into btusPerHourSquareFootFahrenheit`).

/**
 * 1 watt per square meter-kelvin ([KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN]), the
 * group's base unit and the building-physics U-value unit.
 */
val wattsPerSquareMeterKelvin: KHeatTransferCoefficientUnitInstance =
    heatTransferCoefficientOfUnit(KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN)

/**
 * 1 Btu per hour-square foot-degree Fahrenheit
 * ([KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT], ≈ 5.6783 W/(m²·K)).
 */
val btusPerHourSquareFootFahrenheit: KHeatTransferCoefficientUnitInstance =
    heatTransferCoefficientOfUnit(KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT)

/**
 * 1 calorie per second-square centimeter-kelvin
 * ([KHeatTransferCoefficientUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN], 41 840 W/(m²·K)).
 */
val caloriesPerSecondSquareCentimeterKelvin: KHeatTransferCoefficientUnitInstance =
    heatTransferCoefficientOfUnit(KHeatTransferCoefficientUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN)
