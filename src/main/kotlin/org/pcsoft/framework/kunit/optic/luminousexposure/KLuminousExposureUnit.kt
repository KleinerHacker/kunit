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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **luminous exposure** (illuminance accumulated over time; a *constructed*
 * quantity: luminousIntensity · solidAngle · length⁻² · time). [baseValue] is the factor to convert into
 * the group's base unit ([BASE], lux second): `1 unit = baseValue * lx·s`.
 *
 * Example:
 * ```kotlin
 * KLuminousExposureUnit.LUX_HOUR.baseValue // 3600.0 (1 lx·h = 3600 lx·s)
 * ```
 */
enum class KLuminousExposureUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Lux second ("lx*s"), the coherent SI unit of luminous exposure; [baseValue] = 1.0. */
    LUX_SECOND("lx*s", 1.0),

    /** Lux hour ("lx*h"), used for light-dose budgets in conservation; 1 lx·h = 3600 lx·s. */
    LUX_HOUR("lx*h", 3600.0);

    companion object {
        /**
         * The base unit of the luminous-exposure group: [LUX_SECOND]. All internal values of
         * [KLuminousExposureUnitInstance] are normalized to lux seconds.
         */
        val BASE: KLuminousExposureUnit = LUX_SECOND
    }
}
