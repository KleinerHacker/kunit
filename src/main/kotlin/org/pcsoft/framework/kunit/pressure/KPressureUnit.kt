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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of pressure (a *constructed* quantity: mass · length⁻¹ · time⁻²).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], pascal):
 * `1 unit = baseValue * Pa`.
 *
 * Pressure is a *constructed*, three-term unit (`kg·m⁻¹·s⁻²`). Because the mass component of this
 * library is normalized to **grams**, the pascal is 1000× the raw component base (`g·m⁻¹·s⁻²`); that
 * fixed factor is [org.pcsoft.framework.kunit.pressure.PA_IN_BASE] and is applied by the
 * [KPressureUnitInstance] factory, not by these [baseValue]s (which stay relative to the pascal).
 *
 * Prefix-derivable spellings (kPa, MPa = N/mm², hPa) are intentionally **not** dedicated entries: they
 * are reached via the prefix builders (`kilo.pascals`, `mega.pascals`, `hecto.pascals`) or as force per
 * area (`newtons / (milli.meters pow 2)`).
 *
 * Example:
 * ```kotlin
 * KPressureUnit.BAR.baseValue // 100000.0 (1 bar = 100 000 Pa)
 * ```
 */
enum class KPressureUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Pascal ("Pa"), the SI base unit of pressure; [baseValue] = 1.0 by definition (1 Pa = 1 N/m²). */
    PASCAL("Pa", 1.0),

    /** Bar ("bar"), 1 bar = 100 000 Pa (close to mean atmospheric pressure). */
    BAR("bar", 1.0e5),

    /** Standard atmosphere ("atm"), 1 atm = 101 325 Pa. */
    ATMOSPHERE("atm", 101325.0),

    /** Pound-force per square inch ("psi"), 1 psi = 6894.757 Pa. */
    PSI("psi", 6894.757),

    /** Torr / millimeter of mercury ("Torr"), 1 Torr = 101 325 / 760 ≈ 133.322 Pa. */
    TORR("Torr", 133.322);

    companion object {
        /**
         * The base unit of the pressure group: [PASCAL]. All internal values of [KPressureUnitInstance]
         * are normalized to pascals (via the [org.pcsoft.framework.kunit.pressure.PA_IN_BASE] factor).
         */
        val BASE: KPressureUnit = PASCAL
    }
}
