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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of heat transfer coefficient (a *constructed* quantity:
 * mass · time⁻³ · temperature⁻¹). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], watt per square meter-kelvin): `1 unit = baseValue * W/(m²·K)`.
 *
 * In building physics this quantity is the **U-value**; its reciprocal is the R-value
 * (see the thermal insulance group).
 *
 * Example:
 * ```kotlin
 * KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT.baseValue // ≈ 5.678263
 * ```
 */
enum class KHeatTransferCoefficientUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per square meter-kelvin ("W/(m²·K)"), the SI base unit (the U-value); [baseValue] = 1.0. */
    WATT_PER_SQUARE_METER_KELVIN("W/(m²·K)", 1.0),

    /** Btu per hour-square foot-degree Fahrenheit ("Btu/(h·ft²·°F)"), ≈ 5.678263 W/(m²·K). */
    BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT("Btu/(h·ft²·°F)", 5.678263341113489),

    /** Calorie per second-square centimeter-kelvin ("cal/(s·cm²·K)"), 41 840 W/(m²·K). */
    CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN("cal/(s·cm²·K)", 41840.0);

    companion object {
        /**
         * The base unit of the heat transfer coefficient group: [WATT_PER_SQUARE_METER_KELVIN]. All
         * internal values of [KHeatTransferCoefficientUnitInstance] are normalized to W/(m²·K).
         */
        val BASE: KHeatTransferCoefficientUnit = WATT_PER_SQUARE_METER_KELVIN
    }
}
