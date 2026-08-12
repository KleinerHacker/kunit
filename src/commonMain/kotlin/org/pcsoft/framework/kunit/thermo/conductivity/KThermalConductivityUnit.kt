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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of thermal conductivity (a *constructed* quantity:
 * mass · distance · time⁻³ · temperature⁻¹). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], watt per meter-kelvin): `1 unit = baseValue * W/(m·K)`.
 *
 * Note the package is named `conductivity` (not `thermalconductivity`): a unit package must not repeat
 * its field package's name. The types keep the full technical term, distinguishing them from
 * `electric.conductivity`.
 *
 * Example:
 * ```kotlin
 * KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT.baseValue // ≈ 1.730735
 * ```
 */
enum class KThermalConductivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per meter-kelvin ("W/(m·K)"), the SI base unit of thermal conductivity; [baseValue] = 1.0. */
    WATT_PER_METER_KELVIN("W/(m·K)", 1.0),

    /** Btu per hour-foot-degree Fahrenheit ("Btu/(h·ft·°F)"), ≈ 1.730735 W/(m·K). */
    BTU_PER_HOUR_FOOT_FAHRENHEIT("Btu/(h·ft·°F)", 1.7307346501925406),

    /** Calorie per second-centimeter-kelvin ("cal/(s·cm·K)"), 1 cal/(s·cm·K) = 418.4 W/(m·K). */
    CALORIE_PER_SECOND_CENTIMETER_KELVIN("cal/(s·cm·K)", 418.4);

    companion object {
        /**
         * The base unit of the thermal conductivity group: [WATT_PER_METER_KELVIN]. All internal values of
         * [KThermalConductivityUnitInstance] are normalized to W/(m·K).
         */
        val BASE: KThermalConductivityUnit = WATT_PER_METER_KELVIN
    }
}
