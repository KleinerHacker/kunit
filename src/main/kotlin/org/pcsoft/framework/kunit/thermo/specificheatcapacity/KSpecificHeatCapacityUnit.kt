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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of specific heat capacity - heat capacity per unit of mass (a *constructed*
 * quantity: distance² · time⁻² · temperature⁻¹). [baseValue] is the factor to convert into the group's
 * base unit ([BASE], joule per kilogram-kelvin): `1 unit = baseValue * J/(kg·K)`.
 *
 * Example:
 * ```kotlin
 * KSpecificHeatCapacityUnit.CALORIE_PER_GRAM_KELVIN.baseValue // 4184.0 (water is ≈ 1 cal/(g·K))
 * ```
 */
enum class KSpecificHeatCapacityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Joule per kilogram-kelvin ("J/(kg·K)"), the SI base unit of specific heat capacity;
     * [baseValue] = 1.0 by definition.
     */
    JOULE_PER_KILOGRAM_KELVIN("J/(kg·K)", 1.0),

    /** Thermochemical calorie per gram-kelvin ("cal/(g·K)"), 1 cal/(g·K) = 4184 J/(kg·K). */
    CALORIE_PER_GRAM_KELVIN("cal/(g·K)", 4184.0),

    /** Btu per pound-degree Fahrenheit ("Btu/(lb·°F)"), 1 Btu/(lb·°F) = 4186.8 J/(kg·K). */
    BTU_PER_POUND_FAHRENHEIT("Btu/(lb·°F)", 4186.8);

    companion object {
        /**
         * The base unit of the specific heat capacity group: [JOULE_PER_KILOGRAM_KELVIN]. All internal
         * values of [KSpecificHeatCapacityUnitInstance] are normalized to J/(kg·K).
         */
        val BASE: KSpecificHeatCapacityUnit = JOULE_PER_KILOGRAM_KELVIN
    }
}
