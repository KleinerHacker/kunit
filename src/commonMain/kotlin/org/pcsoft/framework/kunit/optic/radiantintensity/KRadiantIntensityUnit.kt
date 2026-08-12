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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **radiant intensity** (radiant flux per solid angle; a *constructed*
 * quantity: mass · length² · time⁻³ · solidAngle⁻¹). It is the radiometric counterpart of the photometric
 * luminous intensity. [baseValue] is the factor to convert into the group's base unit ([BASE], watt per
 * steradian): `1 unit = baseValue * W/sr`.
 *
 * Example:
 * ```kotlin
 * KRadiantIntensityUnit.WATT_PER_STERADIAN.baseValue // 1.0
 * ```
 */
enum class KRadiantIntensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt per steradian ("W/sr"), the coherent SI unit of radiant intensity; [baseValue] = 1.0. */
    WATT_PER_STERADIAN("W/sr", 1.0);

    companion object {
        /**
         * The base unit of the radiant-intensity group: [WATT_PER_STERADIAN]. All internal values of
         * [KRadiantIntensityUnitInstance] are normalized to watts per steradian.
         */
        val BASE: KRadiantIntensityUnit = WATT_PER_STERADIAN
    }
}
