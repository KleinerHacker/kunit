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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of thermal insulance - the *R-value* (a *constructed* quantity:
 * mass⁻¹ · time³ · temperature). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], square meter-kelvin per watt): `1 unit = baseValue * m²·K/W`.
 *
 * Note the package is named `resistance` (not `thermalresistance`): a unit package must not repeat its
 * field package's name. The types keep the full technical term, distinguishing them from
 * `electric.resistance`.
 *
 * Example:
 * ```kotlin
 * KThermalInsulanceUnit.CLO.baseValue // 0.155 (the clothing insulation unit)
 * ```
 */
enum class KThermalInsulanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Square meter-kelvin per watt ("m²·K/W"), the SI base unit of thermal insulance - the metric
     * R-value, often written RSI; [baseValue] = 1.0 by definition.
     */
    SQUARE_METER_KELVIN_PER_WATT("m²·K/W", 1.0),

    /**
     * Imperial R-value ("h·ft²·°F/Btu"), the reciprocal of the imperial U-value:
     * 1 h·ft²·°F/Btu ≈ 0.176110 m²·K/W. A US "R-30" batt is 30 of these.
     */
    HOUR_SQUARE_FOOT_FAHRENHEIT_PER_BTU("h·ft²·°F/Btu", 1.0 / 5.678263341113489),

    /** Clo ("clo"), the clothing insulation unit: 1 clo = 0.155 m²·K/W (a typical business suit). */
    CLO("clo", 0.155),

    /** Tog ("tog"), the textile insulation unit: 1 tog = 0.1 m²·K/W (duvet ratings). */
    TOG("tog", 0.1);

    companion object {
        /**
         * The base unit of the thermal insulance group: [SQUARE_METER_KELVIN_PER_WATT]. All internal
         * values of [KThermalInsulanceUnitInstance] are normalized to m²·K/W.
         */
        val BASE: KThermalInsulanceUnit = SQUARE_METER_KELVIN_PER_WATT
    }
}
