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

package org.pcsoft.framework.kunit.electric.elastance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **elastance** (voltage per charge; a *constructed* quantity:
 * mass · length² · time⁻⁴ · current⁻²). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], reciprocal farad, also called the *daraf*): `1 unit = baseValue * F⁻¹`.
 *
 * Elastance `S = 1 / C` is the exact reciprocal of the
 * [capacitance][org.pcsoft.framework.kunit.electric.capacitance.KCapacitanceUnit]. It is the convenient
 * form for capacitors in **series**, whose elastances simply add.
 *
 * Example:
 * ```kotlin
 * KElastanceUnit.DARAF.baseValue // 1.0 (the daraf is the reciprocal farad)
 * ```
 */
enum class KElastanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Reciprocal farad ("1/F"), the coherent SI unit of elastance; [baseValue] = 1.0. */
    RECIPROCAL_FARAD("1/F", 1.0),

    /** Daraf ("daraf"), the classical name for the reciprocal farad; identical value. */
    DARAF("daraf", 1.0);

    companion object {
        /**
         * The base unit of the elastance group: [RECIPROCAL_FARAD]. All internal values of
         * [KElastanceUnitInstance] are normalized to reciprocal farads.
         */
        val BASE: KElastanceUnit = RECIPROCAL_FARAD
    }
}
