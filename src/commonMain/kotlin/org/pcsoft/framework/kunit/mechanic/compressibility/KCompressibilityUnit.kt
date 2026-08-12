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

package org.pcsoft.framework.kunit.mechanic.compressibility

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **compressibility** (relative volume change per pressure; a *constructed*
 * quantity: mass⁻¹ · length · time²). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], reciprocal pascal): `1 unit = baseValue * 1/Pa`.
 *
 * Compressibility `κ = −(1/V)·(∂V/∂p)` is the reciprocal of the bulk modulus. Water is ≈ 4.6e-10 Pa⁻¹.
 *
 * Example:
 * ```kotlin
 * KCompressibilityUnit.RECIPROCAL_BAR.baseValue // 1e-5 (1/bar in 1/Pa)
 * ```
 */
enum class KCompressibilityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Reciprocal pascal ("1/Pa"), the coherent SI unit of compressibility; [baseValue] = 1.0. */
    RECIPROCAL_PASCAL("1/Pa", 1.0),

    /** Reciprocal bar ("1/bar"); 1 bar = 1e5 Pa, so 1/bar = 1e-5 1/Pa. */
    RECIPROCAL_BAR("1/bar", 1.0e-5),

    /** Reciprocal standard atmosphere ("1/atm"); 1 atm = 101325 Pa. */
    RECIPROCAL_ATMOSPHERE("1/atm", 1.0 / 101325.0);

    companion object {
        /**
         * The base unit of the compressibility group: [RECIPROCAL_PASCAL]. All internal values of
         * [KCompressibilityUnitInstance] are normalized to the group's raw component base.
         */
        val BASE: KCompressibilityUnit = RECIPROCAL_PASCAL
    }
}
