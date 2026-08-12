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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **radiance** (radiant intensity per emitting area; a *constructed*
 * quantity: mass · time⁻³ · solidAngle⁻¹). It is the radiometric counterpart of the photometric luminance.
 * [baseValue] is the factor to convert into the group's base unit ([BASE], watt per steradian square
 * meter): `1 unit = baseValue * W/(sr·m²)`.
 *
 * Example:
 * ```kotlin
 * KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER.baseValue // 1.0
 * ```
 */
enum class KRadianceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per steradian square meter ("W/(sr*m^2)"), the coherent SI unit; [baseValue] = 1.0. */
    WATT_PER_STERADIAN_SQUARE_METER("W/(sr*m^2)", 1.0);

    companion object {
        /**
         * The base unit of the radiance group: [WATT_PER_STERADIAN_SQUARE_METER]. All internal values of
         * [KRadianceUnitInstance] are normalized to watts per steradian square meter.
         */
        val BASE: KRadianceUnit = WATT_PER_STERADIAN_SQUARE_METER
    }
}
