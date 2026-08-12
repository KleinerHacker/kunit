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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **angular momentum** (a *constructed* quantity:
 * mass · length² · time⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE],
 * kilogram meter squared per second): `1 unit = baseValue * kg·m²/s`.
 *
 * The **action** (energy · time) shares this dimension exactly, which is why the joule second
 * ([JOULE_SECOND], the unit of Planck's constant) is an entry of this group rather than a group of its
 * own: `1 J·s = 1 kg·m²/s`.
 *
 * Because the mass component of this library is normalized to **grams**, the kg·m²/s is 1000× the raw
 * component base (`g·m²·s⁻¹`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.angularmomentum.KGM2S_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KAngularMomentumUnit.JOULE_SECOND.baseValue // 1.0 (1 J·s = 1 kg·m²/s)
 * ```
 */
enum class KAngularMomentumUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram meter squared per second ("kg*m^2/s"), the SI base unit; [baseValue] = 1.0 by definition. */
    KILOGRAM_METERS_SQUARED_PER_SECOND("kg*m^2/s", 1.0),

    /** Newton meter second ("N*m*s"), the torque-impulse spelling: 1 N·m·s = 1 kg·m²/s. */
    NEWTON_METER_SECOND("N*m*s", 1.0),

    /** Joule second ("J*s"), the action spelling (unit of Planck's constant): 1 J·s = 1 kg·m²/s. */
    JOULE_SECOND("J*s", 1.0),

    /** Gram centimeter squared per second ("g*cm^2/s"), the CGS unit: 1 g·cm²/s = 1e-7 kg·m²/s. */
    GRAM_CENTIMETERS_SQUARED_PER_SECOND("g*cm^2/s", 1.0e-7);

    companion object {
        /**
         * The base unit of the angular-momentum group: [KILOGRAM_METERS_SQUARED_PER_SECOND]. All readings
         * of [KAngularMomentumUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.angularmomentum.KGM2S_IN_BASE] factor).
         */
        val BASE: KAngularMomentumUnit = KILOGRAM_METERS_SQUARED_PER_SECOND
    }
}
