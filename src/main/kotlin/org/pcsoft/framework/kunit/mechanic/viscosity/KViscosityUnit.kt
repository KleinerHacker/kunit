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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **dynamic viscosity** (a *constructed* quantity:
 * mass · length⁻¹ · time⁻¹, i.e. pressure · time). [baseValue] is the factor to convert into the group's
 * base unit ([BASE], pascal second): `1 unit = baseValue * Pa·s`.
 *
 * The *kinematic* viscosity (`m²/s`) is a different quantity - it is the dynamic viscosity divided by the
 * density and shares its dimension with the diffusivity group.
 *
 * Because the mass component of this library is normalized to **grams**, the Pa·s is 1000× the raw
 * component base (`g·m⁻¹·s⁻¹`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.viscosity.PAS_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KViscosityUnit.POISE.baseValue // 0.1 (1 P = 0.1 Pa·s; water ≈ 1 cP = 1 mPa·s)
 * ```
 */
enum class KViscosityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Pascal second ("Pa*s"), the SI base unit of dynamic viscosity; [baseValue] = 1.0 by definition. */
    PASCAL_SECOND("Pa*s", 1.0),

    /** Poise ("P"), the CGS unit: 1 P = 0.1 Pa·s (centipoise = `centi.poises` = 1 mPa·s ≈ water at 20 °C). */
    POISE("P", 0.1),

    /** Pound-force second per square foot ("lbf*s/ft^2"), 1 lbf·s/ft² ≈ 47.8803 Pa·s. */
    POUND_FORCE_SECOND_PER_SQUARE_FOOT("lbf*s/ft^2", 4.4482216152605 / (0.3048 * 0.3048)),

    /** Reyn ("reyn"), the imperial unit lbf·s/in²: 1 reyn ≈ 6894.757 Pa·s. */
    REYN("reyn", 4.4482216152605 / (0.0254 * 0.0254));

    companion object {
        /**
         * The base unit of the dynamic-viscosity group: [PASCAL_SECOND]. All readings of
         * [KViscosityUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.viscosity.PAS_IN_BASE] factor).
         */
        val BASE: KViscosityUnit = PASCAL_SECOND
    }
}
