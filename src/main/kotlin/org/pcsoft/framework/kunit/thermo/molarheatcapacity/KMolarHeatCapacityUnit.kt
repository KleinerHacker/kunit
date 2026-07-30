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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of molar heat capacity - heat capacity per amount of substance (a
 * *constructed* quantity: mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹). [baseValue] is the
 * factor to convert into the group's base unit ([BASE], joule per mole-kelvin):
 * `1 unit = baseValue * J/(mol·K)`.
 *
 * Example:
 * ```kotlin
 * KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN.baseValue // 4.184
 * ```
 */
enum class KMolarHeatCapacityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Joule per mole-kelvin ("J/(mol·K)"), the SI base unit of molar heat capacity;
     * [baseValue] = 1.0 by definition.
     */
    JOULE_PER_MOLE_KELVIN("J/(mol·K)", 1.0),

    /** Thermochemical calorie per mole-kelvin ("cal/(mol·K)"), 1 cal/(mol·K) = 4.184 J/(mol·K). */
    CALORIE_PER_MOLE_KELVIN("cal/(mol·K)", 4.184);

    companion object {
        /**
         * The base unit of the molar heat capacity group: [JOULE_PER_MOLE_KELVIN]. All internal values of
         * [KMolarHeatCapacityUnitInstance] are normalized to J/(mol·K).
         */
        val BASE: KMolarHeatCapacityUnit = JOULE_PER_MOLE_KELVIN
    }
}

/**
 * The molar gas constant `R` = 8.31446261815324 J/(mol·K) (exact under the 2019 SI). Exposed as a plain
 * [Double] so it can be used both as a factor and as a reading; wrap it with `of joulesPerMoleKelvin` to
 * obtain a typed value.
 *
 * Example:
 * ```kotlin
 * val r = GAS_CONSTANT of joulesPerMoleKelvin // typed 8.314 J/(mol·K)
 * ```
 */
const val GAS_CONSTANT: Double = 8.31446261815324
