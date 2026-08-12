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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **strain** (relative deformation, `ΔL/L`). Strain is *dimensionless*, so all
 * of its units are pure ratios; [baseValue] is the factor to convert into the group's base unit ([BASE], the
 * plain ratio 1): `1 unit = baseValue * 1`.
 *
 * Example:
 * ```kotlin
 * KStrainUnit.PERCENT.baseValue // 0.01 (1 % = 0.01)
 * ```
 */
enum class KStrainUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** The plain ratio ("1"), i.e. m/m; the group's base unit, [baseValue] = 1.0 by definition. */
    RATIO("1", 1.0),

    /** Percent ("%"), 1 % = 0.01. */
    PERCENT("%", 0.01),

    /** Per mille ("‰"), 1 ‰ = 0.001. */
    PER_MILLE("‰", 1.0e-3),

    /** Microstrain ("µe"), 1 µe = 1 µm/m = 1e-6 (the usual strain-gauge reading). */
    MICROSTRAIN("µe", 1.0e-6);

    companion object {
        /**
         * The base unit of the strain group: [RATIO]. All internal values of [KStrainUnitInstance] are
         * normalized to the plain ratio.
         */
        val BASE: KStrainUnit = RATIO
    }
}
