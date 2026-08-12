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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **dose rate** (absorbed radiation dose per time; a *constructed* quantity:
 * length² · time⁻³). [baseValue] is the factor to convert into the group's base unit ([BASE], gray per
 * second): `1 unit = baseValue * Gy/s`.
 *
 * The gray and the sievert share one dimension (`J/kg`), so their rates share one unit group as well - see
 * the [absorbed dose](../specificenergy) page for why one normal form maps to exactly one type. The
 * sievert spellings are provided so radiation-protection readings can be written directly.
 *
 * Example:
 * ```kotlin
 * KDoseRateUnit.SIEVERT_PER_HOUR.baseValue // 1/3600 ≈ 2.7778e-4
 * ```
 */
enum class KDoseRateUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Gray per second ("Gy/s"), the coherent SI unit of dose rate; [baseValue] = 1.0. */
    GRAY_PER_SECOND("Gy/s", 1.0),

    /** Gray per hour ("Gy/h"); 1 Gy/h = 1/3600 Gy/s. */
    GRAY_PER_HOUR("Gy/h", 1.0 / 3600.0),

    /** Sievert per second ("Sv/s"), the equivalent-dose spelling; numerically 1 Gy/s. */
    SIEVERT_PER_SECOND("Sv/s", 1.0),

    /** Sievert per hour ("Sv/h"), the usual radiation-protection unit; 1 Sv/h = 1/3600 Gy/s. */
    SIEVERT_PER_HOUR("Sv/h", 1.0 / 3600.0);

    companion object {
        /**
         * The base unit of the dose-rate group: [GRAY_PER_SECOND]. All internal values of
         * [KDoseRateUnitInstance] are normalized to grays per second.
         */
        val BASE: KDoseRateUnit = GRAY_PER_SECOND
    }
}
