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

package org.pcsoft.framework.kunit.electric.capacitance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electrical capacitance** (a *constructed* quantity:
 * `mass⁻¹ · distance⁻² · time⁴ · current²`, i.e. `kg⁻¹·m⁻²·s⁴·A²`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], farad): `1 unit = baseValue * F`.
 *
 * Unlike length or mass, capacitance is not a "real" single-group unit - it is composed of a mass, a
 * distance, a time and a current term. Each [KCapacitanceUnit] therefore carries a single, pre-computed
 * factor to farads (e.g. `1 abF = 1e9 F`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KCapacitanceUnit.ABFARAD.baseValue // 1.0e9 (1 abF = 1e9 F)
 * ```
 */
enum class KCapacitanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Farad ("F"): the SI base unit of the capacitance group ([baseValue] `1.0`). */
    FARAD("F", 1.0),

    /** Abfarad ("abF"), the capacitance unit of the CGS electromagnetic system (EMU), 1 abF = 1e9 F. */
    ABFARAD("abF", 1.0e9),

    /** Statfarad ("statF"), the capacitance unit of the CGS electrostatic system (ESU), 1 statF = 1.112650056e-12 F. */
    STATFARAD("statF", 1.112650056e-12),

    /** Jar ("jar"), the historic Leyden-jar capacitance of British naval telegraphy, 1 jar = 1.11265e-9 F. */
    JAR("jar", 1.11265e-9);

    companion object {
        /**
         * The base unit of the capacitance group: [FARAD]. All internal values of
         * [KCapacitanceUnitInstance] are normalized to this unit.
         */
        val BASE: KCapacitanceUnit = FARAD
    }
}
