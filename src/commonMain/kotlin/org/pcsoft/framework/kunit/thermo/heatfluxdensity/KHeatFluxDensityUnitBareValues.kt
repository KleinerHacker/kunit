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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

// Value-1 heat flux density templates for the named units of the group, used with `of`/`into`
// (`1361 of wattsPerSquareMeter`, `q into btusPerHourSquareFoot`).

/** 1 watt per square meter ([KHeatFluxDensityUnit.WATT_PER_SQUARE_METER]), the group's base unit. */
val wattsPerSquareMeter: KHeatFluxDensityUnitInstance =
    heatFluxDensityOfUnit(KHeatFluxDensityUnit.WATT_PER_SQUARE_METER)

/** 1 Btu per hour-square foot ([KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT], ≈ 3.1546 W/m²). */
val btusPerHourSquareFoot: KHeatFluxDensityUnitInstance =
    heatFluxDensityOfUnit(KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT)

/**
 * 1 calorie per second-square centimeter
 * ([KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER], 41 840 W/m²).
 */
val caloriesPerSecondSquareCentimeter: KHeatFluxDensityUnitInstance =
    heatFluxDensityOfUnit(KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER)
