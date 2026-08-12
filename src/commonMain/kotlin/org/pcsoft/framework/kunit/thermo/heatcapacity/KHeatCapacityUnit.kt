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

package org.pcsoft.framework.kunit.thermo.heatcapacity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of heat capacity (a *constructed* quantity:
 * mass · distance² · time⁻² · temperature⁻¹). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], joule per kelvin): `1 unit = baseValue * J/K`.
 *
 * The same dimension also describes **entropy**, which deliberately shares this group rather than getting
 * a type of its own (one canonical normal form must map to exactly one type, so that `toHeatCapacity()`
 * stays unambiguous).
 *
 * Example:
 * ```kotlin
 * KHeatCapacityUnit.CALORIE_PER_KELVIN.baseValue // 4.184 (1 cal/K = 4.184 J/K)
 * ```
 */
enum class KHeatCapacityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Joule per kelvin ("J/K"), the SI base unit of heat capacity; [baseValue] = 1.0 by definition. */
    JOULE_PER_KELVIN("J/K", 1.0),

    /** Thermochemical calorie per kelvin ("cal/K"), 1 cal/K = 4.184 J/K. */
    CALORIE_PER_KELVIN("cal/K", 4.184),

    /**
     * British thermal unit per degree Fahrenheit ("Btu/°F"), 1 Btu/°F = 1055.05585262 J / (5/9 K)
     * ≈ 1899.1005 J/K.
     */
    BTU_PER_FAHRENHEIT("Btu/°F", 1055.05585262 / (5.0 / 9.0));

    companion object {
        /**
         * The base unit of the heat capacity group: [JOULE_PER_KELVIN]. All internal values of
         * [KHeatCapacityUnitInstance] are normalized to joules per kelvin.
         */
        val BASE: KHeatCapacityUnit = JOULE_PER_KELVIN
    }
}
