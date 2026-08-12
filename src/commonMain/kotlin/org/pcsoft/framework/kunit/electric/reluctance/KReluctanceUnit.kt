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

package org.pcsoft.framework.kunit.electric.reluctance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **magnetic reluctance** (a *constructed* quantity:
 * `mass⁻¹ · distance⁻² · time² · current²`, i.e. `kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`). [baseValue] is the
 * factor to convert into the group's base unit ([BASE], ampere per weber): `1 unit = baseValue * A/Wb`.
 *
 * The reluctance `Rm` is the magnetic circuit's counterpart to the electrical resistance: it relates the
 * magnetomotive force to the resulting flux (`Rm = Θ / Φ`). Its reciprocal is the **permeance** `Λ`, which is
 * measured in henries and therefore lives in the inductance group. Unlike length or mass it is not a "real"
 * single-group unit - it is composed of a mass, a distance, a time and a current term. Each
 * [KReluctanceUnit] therefore carries a single, pre-computed factor to amperes per weber, so it can still be
 * used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KReluctanceUnit.INVERSE_HENRY.baseValue // 1.0 (1 H⁻¹ = 1 A/Wb)
 * ```
 */
enum class KReluctanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ampere per weber ("A/Wb"): the SI base unit of the reluctance group ([baseValue] `1.0`). */
    AMPERE_PER_WEBER("A/Wb", 1.0),

    /**
     * Inverse henry ("H⁻¹"), the reciprocal-inductance spelling of the same quantity, 1 H⁻¹ = 1 A/Wb.
     * Dimensionally identical to [AMPERE_PER_WEBER]; the distinct symbol documents the reciprocal view.
     */
    INVERSE_HENRY("H⁻¹", 1.0),

    /**
     * Ampere turn per weber ("At/Wb"), the magnetic-circuit spelling that names the coil turns explicitly,
     * 1 At/Wb = 1 A/Wb (the number of turns is a pure count).
     */
    AMPERE_TURN_PER_WEBER("At/Wb", 1.0);

    companion object {
        /**
         * The base unit of the reluctance group: [AMPERE_PER_WEBER]. All internal values of
         * [KReluctanceUnitInstance] are normalized to this unit.
         */
        val BASE: KReluctanceUnit = AMPERE_PER_WEBER
    }
}
