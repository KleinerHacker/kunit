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

package org.pcsoft.framework.kunit.optic.luminousintensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of luminous intensity (a *native*, measurable SI base quantity - the seventh
 * SI base unit). [baseValue] is the factor to convert into the group's base unit ([BASE], candela):
 * `1 unit = baseValue * cd`.
 *
 * Example:
 * ```kotlin
 * KLuminousIntensityUnit.HEFNER_CANDLE.baseValue // 0.903 (1 HK = 0.903 cd)
 * ```
 */
enum class KLuminousIntensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Candela ("cd"), the SI base unit of luminous intensity; [baseValue] = 1.0 by definition. */
    CANDELA("cd", 1.0),

    /**
     * Hefner candle ("HK"), the historical German standard based on the Hefner lamp.
     * 1 HK = 0.903 cd.
     */
    HEFNER_CANDLE("HK", 0.903),

    /**
     * Candlepower ("cp"), the historical British standard (international candle).
     * 1 cp = 0.981 cd.
     */
    CANDLEPOWER("cp", 0.981),

    /**
     * Carcel ("carcel"), the historical French standard based on the Carcel oil lamp.
     * 1 carcel = 9.74 cd.
     */
    CARCEL("carcel", 9.74);

    companion object {
        /**
         * The base unit of the luminous-intensity group: [CANDELA]. All internal values of
         * [KLuminousIntensityUnitInstance] are normalized to candelas.
         */
        val BASE: KLuminousIntensityUnit = CANDELA
    }
}
