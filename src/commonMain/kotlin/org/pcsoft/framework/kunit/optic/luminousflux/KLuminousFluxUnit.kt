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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **luminous flux** (a *constructed* quantity: luminousIntensity ·
 * solidAngle). [baseValue] is the factor to convert into the group's base unit ([BASE], lumen):
 * `1 unit = baseValue * lm`.
 *
 * Example:
 * ```kotlin
 * KLuminousFluxUnit.CANDELA_STERADIAN.baseValue // 1.0 (1 cd·sr = 1 lm, by definition)
 * ```
 */
enum class KLuminousFluxUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Lumen ("lm"), the coherent SI unit of luminous flux; [baseValue] = 1.0 by definition. */
    LUMEN("lm", 1.0),

    /**
     * Candela steradian ("cd·sr"), the explicit spelling of the lumen's definition. Numerically identical
     * to the [LUMEN]; it exists so the defining decomposition can be written out in a formula.
     */
    CANDELA_STERADIAN("cd·sr", 1.0);

    companion object {
        /**
         * The base unit of the luminous-flux group: [LUMEN]. All internal values of
         * [KLuminousFluxUnitInstance] are normalized to lumens.
         */
        val BASE: KLuminousFluxUnit = LUMEN
    }
}
