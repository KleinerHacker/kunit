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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **diffusivity** (a *constructed* quantity: distance² · time⁻¹). [baseValue]
 * is the factor to convert into the group's base unit ([BASE], square meter per second):
 * `1 unit = baseValue * m²/s`.
 *
 * The group lives in `common` because the same quantity belongs to two subject areas: it is the
 * **thermal diffusivity** `α = λ / (ρ · c_p)` of thermodynamics and the **kinematic viscosity**
 * `ν = η / ρ` of mechanics. Both readings share this vocabulary; the stokes ([STOKES], [CENTISTOKES]) is
 * the traditional kinematic-viscosity spelling.
 *
 * Example:
 * ```kotlin
 * KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND.baseValue // 1e-6 (the unit material tables use)
 * ```
 */
enum class KDiffusivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Square meter per second ("m²/s"), the SI base unit of diffusivity; [baseValue] = 1.0. */
    SQUARE_METER_PER_SECOND("m²/s", 1.0),

    /**
     * Square millimeter per second ("mm²/s"), 1 mm²/s = 1e-6 m²/s - the magnitude material tables use
     * (copper ≈ 111 mm²/s, water ≈ 0.14 mm²/s). Numerically identical to the centistokes.
     */
    SQUARE_MILLIMETER_PER_SECOND("mm²/s", 1.0e-6),

    /** Square foot per hour ("ft²/h"), 1 ft²/h = 0.3048²/3600 m²/s ≈ 2.58064e-5 m²/s. */
    SQUARE_FOOT_PER_HOUR("ft²/h", 0.3048 * 0.3048 / 3600.0),

    /** Stokes ("St"), the CGS kinematic-viscosity unit: 1 St = 1 cm²/s = 1e-4 m²/s. */
    STOKES("St", 1.0e-4),

    /** Centistokes ("cSt"), 1 cSt = 1e-6 m²/s = 1 mm²/s (water at 20 °C ≈ 1 cSt). */
    CENTISTOKES("cSt", 1.0e-6);

    companion object {
        /**
         * The base unit of the diffusivity group: [SQUARE_METER_PER_SECOND]. All internal values of
         * [KDiffusivityUnitInstance] are normalized to m²/s.
         */
        val BASE: KDiffusivityUnit = SQUARE_METER_PER_SECOND
    }
}
