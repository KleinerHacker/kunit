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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **thermal conductance** (heat flow per temperature difference; a
 * *constructed* quantity: mass · length² · time⁻³ · temperature⁻¹). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], watt per kelvin): `1 unit = baseValue * W/K`.
 *
 * Thermal conductance is the exact reciprocal of the
 * [absolute thermal resistance][org.pcsoft.framework.kunit.thermo.resistance.KThermalResistanceUnit].
 *
 * Example:
 * ```kotlin
 * KThermalConductanceUnit.BTU_PER_HOUR_FAHRENHEIT.baseValue // ≈ 0.5275 W/K
 * ```
 */
enum class KThermalConductanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per kelvin ("W/K"), the coherent SI unit of thermal conductance; [baseValue] = 1.0. */
    WATT_PER_KELVIN("W/K", 1.0),

    /**
     * British thermal unit per hour degree-Fahrenheit ("Btu/(h*°F)"), the imperial counterpart.
     * 1 Btu/(h·°F) ≈ 0.5275 W/K.
     */
    BTU_PER_HOUR_FAHRENHEIT("Btu/(h*°F)", 0.5275279263);

    companion object {
        /**
         * The base unit of the thermal-conductance group: [WATT_PER_KELVIN]. All internal values of
         * [KThermalConductanceUnitInstance] are normalized to watts per kelvin.
         */
        val BASE: KThermalConductanceUnit = WATT_PER_KELVIN
    }
}
