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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **illuminance** (luminous flux per illuminated area; a *constructed*
 * quantity: luminousIntensity · solidAngle · length⁻²). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], lux): `1 unit = baseValue * lx`.
 *
 * Example:
 * ```kotlin
 * KIlluminanceUnit.FOOT_CANDLE.baseValue // ≈ 10.7639 (1 fc = 1 lm/ft²)
 * ```
 */
enum class KIlluminanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Lux ("lx"), the coherent SI unit of illuminance (1 lx = 1 lm/m²); [baseValue] = 1.0. */
    LUX("lx", 1.0),

    /** Phot ("ph"), the CGS unit (1 ph = 1 lm/cm² = 10 000 lx). */
    PHOT("ph", 1.0e4),

    /** Foot-candle ("fc"), the imperial unit (1 fc = 1 lm/ft² ≈ 10.7639 lx). */
    FOOT_CANDLE("fc", 10.763910416709722),

    /** Nox ("nx"), used for very low light levels (1 nx = 1 mlx = 0.001 lx). */
    NOX("nx", 1.0e-3);

    companion object {
        /**
         * The base unit of the illuminance group: [LUX]. All internal values of
         * [KIlluminanceUnitInstance] are normalized to lux.
         */
        val BASE: KIlluminanceUnit = LUX
    }
}
