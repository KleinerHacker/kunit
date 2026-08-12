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

package org.pcsoft.framework.kunit.optic.luminance

import kotlin.math.PI
import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **luminance** (luminous intensity per emitting area; a *constructed*
 * quantity: luminousIntensity · length⁻²). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], candela per square meter, commonly called *nit*): `1 unit = baseValue * cd/m²`.
 *
 * Example:
 * ```kotlin
 * KLuminanceUnit.STILB.baseValue // 10 000.0 (1 sb = 1 cd/cm²)
 * ```
 */
enum class KLuminanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Candela per square meter ("cd/m^2"), the coherent SI unit, commonly called *nit*; [baseValue] = 1.0. */
    CANDELA_PER_SQUARE_METER("cd/m^2", 1.0),

    /** Stilb ("sb"), the CGS unit (1 sb = 1 cd/cm² = 10 000 cd/m²). */
    STILB("sb", 1.0e4),

    /** Apostilb ("asb"), a photometric unit of the lambertian family: 1 asb = 1/π cd/m². */
    APOSTILB("asb", 1.0 / PI),

    /** Lambert ("L"), the CGS lambertian unit: 1 L = 1/π cd/cm² = 10⁴/π cd/m². */
    LAMBERT("L", 1.0e4 / PI),

    /** Foot-lambert ("fL"), the imperial lambertian unit: 1 fL = 1/π cd/ft² ≈ 3.4263 cd/m². */
    FOOT_LAMBERT("fL", 10.763910416709722 / PI);

    companion object {
        /**
         * The base unit of the luminance group: [CANDELA_PER_SQUARE_METER]. All internal values of
         * [KLuminanceUnitInstance] are normalized to candelas per square meter.
         */
        val BASE: KLuminanceUnit = CANDELA_PER_SQUARE_METER
    }
}
