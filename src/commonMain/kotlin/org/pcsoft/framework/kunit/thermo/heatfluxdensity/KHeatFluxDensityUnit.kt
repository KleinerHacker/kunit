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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of heat flux density - power per unit of area (a *constructed* quantity:
 * mass · time⁻³). [baseValue] is the factor to convert into the group's base unit ([BASE], watt per
 * square meter): `1 unit = baseValue * W/m²`.
 *
 * The same unit also measures irradiance and radiant exitance.
 *
 * Example:
 * ```kotlin
 * KHeatFluxDensityUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER.baseValue // 41840.0
 * ```
 */
enum class KHeatFluxDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per square meter ("W/m²"), the SI base unit of heat flux density; [baseValue] = 1.0. */
    WATT_PER_SQUARE_METER("W/m²", 1.0),

    /** Btu per hour-square foot ("Btu/(h·ft²)"), 1 Btu/(h·ft²) ≈ 3.154591 W/m². */
    BTU_PER_HOUR_SQUARE_FOOT("Btu/(h·ft²)", 3.154590745063563),

    /** Calorie per second-square centimeter ("cal/(s·cm²)"), 1 cal/(s·cm²) = 41 840 W/m². */
    CALORIE_PER_SECOND_SQUARE_CENTIMETER("cal/(s·cm²)", 41840.0);

    companion object {
        /**
         * The base unit of the heat flux density group: [WATT_PER_SQUARE_METER]. All internal values of
         * [KHeatFluxDensityUnitInstance] are normalized to W/m².
         */
        val BASE: KHeatFluxDensityUnit = WATT_PER_SQUARE_METER
    }
}

/**
 * The solar constant: the mean irradiance of sunlight above Earth's atmosphere, 1361 W/m². Exposed as a
 * plain [Double]; wrap it with `of wattsPerSquareMeter` to obtain a typed value.
 */
const val SOLAR_CONSTANT: Double = 1361.0
