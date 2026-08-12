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

package org.pcsoft.framework.kunit.thermo.insulance

// Value-1 thermal insulance templates for the named units of the group, used with `of`/`into`
// (`5 of squareMeterKelvinPerWatt`, `r into hourSquareFootFahrenheitPerBtu`).

/**
 * 1 square meter-kelvin per watt ([KThermalInsulanceUnit.SQUARE_METER_KELVIN_PER_WATT]), the group's
 * base unit (the metric R-value / RSI).
 */
val squareMeterKelvinPerWatt: KThermalInsulanceUnitInstance =
    thermalInsulanceOfUnit(KThermalInsulanceUnit.SQUARE_METER_KELVIN_PER_WATT)

/**
 * 1 imperial R-value unit ([KThermalInsulanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU],
 * ≈ 0.176110 m²·K/W). A US "R-30" batt is `30 of hourSquareFootFahrenheitPerBtu`.
 */
val hourSquareFootFahrenheitPerBtu: KThermalInsulanceUnitInstance =
    thermalInsulanceOfUnit(KThermalInsulanceUnit.HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU)

/** 1 clo ([KThermalInsulanceUnit.CLO], 0.155 m²·K/W), the clothing insulation unit. */
val clo: KThermalInsulanceUnitInstance = thermalInsulanceOfUnit(KThermalInsulanceUnit.CLO)

/** 1 tog ([KThermalInsulanceUnit.TOG], 0.1 m²·K/W), the textile/duvet insulation unit. */
val tog: KThermalInsulanceUnitInstance = thermalInsulanceOfUnit(KThermalInsulanceUnit.TOG)
