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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of momentum (a *constructed* quantity: mass · length · time⁻¹). [baseValue]
 * is the factor to convert into the group's base unit ([BASE], kilogram meter per second):
 * `1 unit = baseValue * kg·m/s`.
 *
 * The **impulse** (force · time) has exactly the same dimension, which is why the newton second
 * ([NEWTON_SECOND]) is an entry of this very group rather than a group of its own: `1 N·s = 1 kg·m/s`.
 *
 * Because the mass component of this library is normalized to **grams**, the kg·m/s is 1000× the raw
 * component base (`g·m·s⁻¹`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.momentum.KGMS_IN_BASE] and is applied by the
 * [KMomentumUnitInstance] factory, not by these [baseValue]s.
 *
 * Example:
 * ```kotlin
 * KMomentumUnit.NEWTON_SECOND.baseValue // 1.0 (1 N·s = 1 kg·m/s)
 * ```
 */
enum class KMomentumUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram meter per second ("kg*m/s"), the SI base unit of momentum; [baseValue] = 1.0 by definition. */
    KILOGRAM_METERS_PER_SECOND("kg*m/s", 1.0),

    /** Newton second ("N*s"), the impulse spelling of the same dimension: 1 N·s = 1 kg·m/s. */
    NEWTON_SECOND("N*s", 1.0),

    /** Gram centimeter per second ("g*cm/s"), the CGS unit: 1 g·cm/s = 1e-5 kg·m/s. */
    GRAM_CENTIMETERS_PER_SECOND("g*cm/s", 1.0e-5),

    /** Pound-foot per second ("lb*ft/s"), 1 lb·ft/s ≈ 0.138255 kg·m/s. */
    POUND_FEET_PER_SECOND("lb*ft/s", 0.45359237 * 0.3048);

    companion object {
        /**
         * The base unit of the momentum group: [KILOGRAM_METERS_PER_SECOND]. All readings of
         * [KMomentumUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.momentum.KGMS_IN_BASE] factor).
         */
        val BASE: KMomentumUnit = KILOGRAM_METERS_PER_SECOND
    }
}
