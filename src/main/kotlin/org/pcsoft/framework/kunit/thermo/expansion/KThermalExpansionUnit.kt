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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of the thermal expansion coefficient (a *constructed* quantity:
 * temperature⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE], per kelvin):
 * `1 unit = baseValue * 1/K`.
 *
 * Note the package is named `expansion` (not `thermalexpansion`): a unit package must not repeat its
 * field package's name. The types keep the full technical term.
 *
 * Example:
 * ```kotlin
 * KThermalExpansionUnit.PPM_PER_KELVIN.baseValue // 1e-6 (material data is tabulated in ppm/K)
 * ```
 */
enum class KThermalExpansionUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Per kelvin ("1/K"), the SI base unit of thermal expansion; [baseValue] = 1.0 by definition. */
    PER_KELVIN("1/K", 1.0),

    /**
     * Per degree Fahrenheit ("1/°F"). A Fahrenheit *interval* is 5/9 K, so a change per °F is 9/5 = 1.8
     * times the change per kelvin.
     */
    PER_FAHRENHEIT("1/°F", 9.0 / 5.0),

    /**
     * Parts per million per kelvin ("ppm/K"), 1 ppm/K = 1e-6 1/K - the form material tables use
     * (steel ≈ 12 ppm/K, aluminium ≈ 23 ppm/K).
     */
    PPM_PER_KELVIN("ppm/K", 1.0e-6);

    companion object {
        /**
         * The base unit of the thermal expansion group: [PER_KELVIN]. All internal values of
         * [KThermalExpansionUnitInstance] are normalized to 1/K.
         */
        val BASE: KThermalExpansionUnit = PER_KELVIN
    }
}
