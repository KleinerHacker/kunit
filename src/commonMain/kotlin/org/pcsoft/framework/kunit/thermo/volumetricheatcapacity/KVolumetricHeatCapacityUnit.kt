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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **volumetric heat capacity** (heat capacity per volume; a *constructed*
 * quantity: mass · length⁻¹ · time⁻² · temperature⁻¹). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], joule per cubic meter kelvin): `1 unit = baseValue * J/(m³·K)`.
 *
 * Example:
 * ```kotlin
 * KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN.baseValue // 4.184e6
 * ```
 */
enum class KVolumetricHeatCapacityUnit(
    override val symbol: String,
    override val baseValue: Double
) : KUnit {
    /** Joule per cubic meter kelvin ("J/(m^3*K)"), the coherent SI unit; [baseValue] = 1.0. */
    JOULE_PER_CUBIC_METER_KELVIN("J/(m^3*K)", 1.0),

    /**
     * Calorie per cubic centimeter kelvin ("cal/(cm^3*K)"), the thermochemical spelling.
     * 1 cal/(cm³·K) = 4.184e6 J/(m³·K).
     */
    CALORIE_PER_CUBIC_CENTIMETER_KELVIN("cal/(cm^3*K)", 4.184e6);

    companion object {
        /**
         * The base unit of the volumetric-heat-capacity group: [JOULE_PER_CUBIC_METER_KELVIN]. All internal
         * values of [KVolumetricHeatCapacityUnitInstance] are normalized to joules per cubic meter kelvin.
         */
        val BASE: KVolumetricHeatCapacityUnit = JOULE_PER_CUBIC_METER_KELVIN
    }
}
