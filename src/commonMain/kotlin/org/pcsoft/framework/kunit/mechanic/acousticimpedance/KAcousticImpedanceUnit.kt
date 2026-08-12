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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **specific acoustic impedance** (sound pressure per particle velocity; a
 * *constructed* quantity: mass · length⁻² · time⁻¹). [baseValue] is the factor to convert into the group's
 * base unit ([BASE], pascal second per meter, also called the *rayl*): `1 unit = baseValue * Pa·s/m`.
 *
 * The acoustic impedance `Z = ρ · c` decides how much sound is reflected at a boundary: air is ≈ 413
 * Pa·s/m, water ≈ 1.48e6 Pa·s/m - which is why so little airborne sound enters water.
 *
 * Example:
 * ```kotlin
 * KAcousticImpedanceUnit.CGS_RAYL.baseValue // 10.0 (1 CGS rayl = 10 Pa·s/m)
 * ```
 */
enum class KAcousticImpedanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Pascal second per meter ("Pa*s/m"), the coherent SI unit; [baseValue] = 1.0. */
    PASCAL_SECOND_PER_METER("Pa*s/m", 1.0),

    /** SI rayl ("rayl"), the named spelling of the pascal second per meter; identical value. */
    RAYL("rayl", 1.0),

    /** CGS rayl ("rayl (CGS)"), 1 dyn·s/cm³ = 10 Pa·s/m. */
    CGS_RAYL("rayl (CGS)", 10.0);

    companion object {
        /**
         * The base unit of the acoustic-impedance group: [PASCAL_SECOND_PER_METER]. All internal values of
         * [KAcousticImpedanceUnitInstance] are normalized to the group's raw component base.
         */
        val BASE: KAcousticImpedanceUnit = PASCAL_SECOND_PER_METER
    }
}
