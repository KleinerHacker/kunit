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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **linear density** (mass per length, a *constructed* quantity:
 * mass · length⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE], kilogram per
 * meter): `1 unit = baseValue * kg/m`.
 *
 * Because the mass component of this library is normalized to **grams**, the kg/m is 1000× the raw
 * component base (`g·m⁻¹`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.lineardensity.KGM_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KLinearDensityUnit.TEX.baseValue // 1.0e-6 (1 tex = 1 g/km)
 * ```
 */
enum class KLinearDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram per meter ("kg/m"), the SI base unit of linear density; [baseValue] = 1.0 by definition. */
    KILOGRAMS_PER_METER("kg/m", 1.0),

    /** Gram per meter ("g/m"), 1 g/m = 0.001 kg/m. */
    GRAMS_PER_METER("g/m", 1.0e-3),

    /** Gram per centimeter ("g/cm"), 1 g/cm = 0.1 kg/m. */
    GRAMS_PER_CENTIMETER("g/cm", 0.1),

    /** Tex ("tex"), the textile unit: 1 tex = 1 g/km = 1e-6 kg/m. */
    TEX("tex", 1.0e-6),

    /** Denier ("den"), the textile unit: 1 den = 1 g / 9000 m ≈ 1.1111e-7 kg/m. */
    DENIER("den", 1.0e-3 / 9000.0),

    /** Pound per foot ("lb/ft"), 1 lb/ft ≈ 1.48816 kg/m. */
    POUNDS_PER_FOOT("lb/ft", 0.45359237 / 0.3048);

    companion object {
        /**
         * The base unit of the linear-density group: [KILOGRAMS_PER_METER]. All readings of
         * [KLinearDensityUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.lineardensity.KGM_IN_BASE] factor).
         */
        val BASE: KLinearDensityUnit = KILOGRAMS_PER_METER
    }
}
