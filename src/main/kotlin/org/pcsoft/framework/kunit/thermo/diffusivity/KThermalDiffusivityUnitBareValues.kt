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

package org.pcsoft.framework.kunit.thermo.diffusivity

// Value-1 thermal diffusivity templates for the named units of the group, used with `of`/`into`
// (`111 of squareMillimetersPerSecond`, `alpha into squareMetersPerSecond`).

/** 1 square meter per second ([KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND]), the group's base unit. */
val squareMetersPerSecond: KThermalDiffusivityUnitInstance =
    thermalDiffusivityOfUnit(KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND)

/**
 * 1 square millimeter per second ([KThermalDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND], 1e-6 m²/s) -
 * the magnitude material tables use.
 */
val squareMillimetersPerSecond: KThermalDiffusivityUnitInstance =
    thermalDiffusivityOfUnit(KThermalDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND)

/** 1 square foot per hour ([KThermalDiffusivityUnit.SQUARE_FOOT_PER_HOUR], ≈ 2.58064e-5 m²/s). */
val squareFeetPerHour: KThermalDiffusivityUnitInstance =
    thermalDiffusivityOfUnit(KThermalDiffusivityUnit.SQUARE_FOOT_PER_HOUR)
