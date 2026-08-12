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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **absolute thermal resistance** (temperature difference sustained per heat
 * flow; a *constructed* quantity: mass⁻¹ · length⁻² · time³ · temperature). [baseValue] is the factor to
 * convert into the group's base unit ([BASE], kelvin per watt): `1 unit = baseValue * K/W`.
 *
 * This describes a **whole component** - a heat sink, a transistor package, a wall of a given size. Do not
 * confuse it with the
 * [thermal insulance][org.pcsoft.framework.kunit.thermo.insulance.KThermalInsulanceUnit] (`m²·K/W`), which
 * is the same idea normalized per unit of area.
 *
 * Example:
 * ```kotlin
 * KThermalResistanceUnit.HOUR_FAHRENHEIT_PER_BTU.baseValue // ≈ 1.8956 (1 h·°F/Btu in K/W)
 * ```
 */
enum class KThermalResistanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kelvin per watt ("K/W"), the coherent SI unit of absolute thermal resistance; [baseValue] = 1.0. */
    KELVIN_PER_WATT("K/W", 1.0),

    /**
     * Degree Celsius per watt ("°C/W"), the spelling used on heat-sink and semiconductor datasheets.
     * A temperature *difference* of 1 °C is 1 K, so this is numerically identical to [KELVIN_PER_WATT].
     */
    DEGREE_CELSIUS_PER_WATT("°C/W", 1.0),

    /**
     * Hour degree-Fahrenheit per British thermal unit ("h*°F/Btu"), the imperial thermal ohm.
     * 1 h·°F/Btu ≈ 1.8956 K/W.
     */
    HOUR_FAHRENHEIT_PER_BTU("h*°F/Btu", 1.8956342406);

    companion object {
        /**
         * The base unit of the absolute thermal-resistance group: [KELVIN_PER_WATT]. All internal values of
         * [KThermalResistanceUnitInstance] are normalized to kelvins per watt.
         */
        val BASE: KThermalResistanceUnit = KELVIN_PER_WATT
    }
}
