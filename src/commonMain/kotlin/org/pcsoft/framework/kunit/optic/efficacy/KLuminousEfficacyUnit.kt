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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **luminous efficacy** (luminous flux per electrical power; a *constructed*
 * quantity: luminousIntensity · solidAngle · mass⁻¹ · length⁻² · time³). [baseValue] is the factor to
 * convert into the group's base unit ([BASE], lumen per watt): `1 unit = baseValue * lm/W`.
 *
 * Example:
 * ```kotlin
 * KLuminousEfficacyUnit.LUMEN_PER_WATT.baseValue // 1.0
 * ```
 */
enum class KLuminousEfficacyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Lumen per watt ("lm/W"), the coherent SI unit of luminous efficacy; [baseValue] = 1.0. */
    LUMEN_PER_WATT("lm/W", 1.0);

    companion object {
        /**
         * The base unit of the luminous-efficacy group: [LUMEN_PER_WATT]. All internal values of
         * [KLuminousEfficacyUnitInstance] are normalized to lumens per watt.
         */
        val BASE: KLuminousEfficacyUnit = LUMEN_PER_WATT
    }
}

/**
 * The **maximum possible luminous efficacy of radiation** at 555 nm, the peak of the photopic luminosity
 * function: 683 lm/W. It follows directly from the SI definition of the candela and is the hard physical
 * ceiling every lamp is measured against. Exposed as a plain [Double] in [KLuminousEfficacyUnit.BASE]
 * units so it can be used with any construction form (`MAX_LUMINOUS_EFFICACY of lumensPerWatt`).
 *
 * Example:
 * ```kotlin
 * val ratio = (120 of lumensPerWatt).value / MAX_LUMINOUS_EFFICACY // LED lamp: ≈ 0.176
 * ```
 */
const val MAX_LUMINOUS_EFFICACY: Double = 683.0
