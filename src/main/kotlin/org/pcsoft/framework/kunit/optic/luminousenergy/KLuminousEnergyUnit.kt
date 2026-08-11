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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **luminous energy** (luminous flux accumulated over time; a *constructed*
 * quantity: luminousIntensity · solidAngle · time). [baseValue] is the factor to convert into the group's
 * base unit ([BASE], lumen second, also called *talbot*): `1 unit = baseValue * lm·s`.
 *
 * Example:
 * ```kotlin
 * KLuminousEnergyUnit.LUMEN_HOUR.baseValue // 3600.0 (1 lm·h = 3600 lm·s)
 * ```
 */
enum class KLuminousEnergyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Lumen second ("lm*s"), the coherent SI unit of luminous energy, also called *talbot*. */
    LUMEN_SECOND("lm*s", 1.0),

    /** Lumen hour ("lm*h"), the lamp-lifetime spelling; 1 lm·h = 3600 lm·s. */
    LUMEN_HOUR("lm*h", 3600.0);

    companion object {
        /**
         * The base unit of the luminous-energy group: [LUMEN_SECOND]. All internal values of
         * [KLuminousEnergyUnitInstance] are normalized to lumen seconds.
         */
        val BASE: KLuminousEnergyUnit = LUMEN_SECOND
    }
}
