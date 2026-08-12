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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **specific weight** (weight force per volume; a *constructed* quantity:
 * mass · length⁻² · time⁻²). [baseValue] is the factor to convert into the group's base unit ([BASE],
 * newton per cubic meter): `1 unit = baseValue * N/m³`.
 *
 * Specific weight is what hydrostatics works in: the pressure at a depth is `p = γ · h`, with `γ` the
 * specific weight of the fluid. Water is ≈ 9807 N/m³.
 *
 * Example:
 * ```kotlin
 * KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER.baseValue // 1000.0
 * ```
 */
enum class KSpecificWeightUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Newton per cubic meter ("N/m^3"), the coherent SI unit of specific weight; [baseValue] = 1.0. */
    NEWTON_PER_CUBIC_METER("N/m^3", 1.0),

    /** Kilonewton per cubic meter ("kN/m^3"), the usual civil-engineering spelling; = 1000 N/m³. */
    KILONEWTON_PER_CUBIC_METER("kN/m^3", 1000.0),

    /** Pound-force per cubic foot ("lbf/ft^3"), the imperial counterpart; ≈ 157.087 N/m³. */
    POUND_FORCE_PER_CUBIC_FOOT("lbf/ft^3", 157.08746384);

    companion object {
        /**
         * The base unit of the specific-weight group: [NEWTON_PER_CUBIC_METER]. All internal values of
         * [KSpecificWeightUnitInstance] are normalized to the group's raw component base.
         */
        val BASE: KSpecificWeightUnit = NEWTON_PER_CUBIC_METER
    }
}
