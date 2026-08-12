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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **mass flow rate** (a *constructed* quantity: mass · time⁻¹). [baseValue]
 * is the factor to convert into the group's base unit ([BASE], kilogram per second):
 * `1 unit = baseValue * kg/s`.
 *
 * Because the mass component of this library is normalized to **grams**, the kg/s is 1000× the raw
 * component base (`g·s⁻¹`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.massflow.KGS_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KMassFlowUnit.TONNES_PER_HOUR.baseValue // ≈ 0.2778 (1 t/h = 1000 kg / 3600 s)
 * ```
 */
enum class KMassFlowUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram per second ("kg/s"), the SI base unit of mass flow; [baseValue] = 1.0 by definition. */
    KILOGRAMS_PER_SECOND("kg/s", 1.0),

    /** Gram per second ("g/s"), 1 g/s = 0.001 kg/s. */
    GRAMS_PER_SECOND("g/s", 1.0e-3),

    /** Kilogram per hour ("kg/h"), 1 kg/h = 1/3600 kg/s. */
    KILOGRAMS_PER_HOUR("kg/h", 1.0 / 3600.0),

    /** Tonne per hour ("t/h"), 1 t/h = 1000/3600 kg/s (industrial throughput). */
    TONNES_PER_HOUR("t/h", 1000.0 / 3600.0),

    /** Pound per second ("lb/s"), 1 lb/s = 0.45359237 kg/s. */
    POUNDS_PER_SECOND("lb/s", 0.45359237),

    /** Pound per hour ("lb/h"), 1 lb/h = 0.45359237/3600 kg/s. */
    POUNDS_PER_HOUR("lb/h", 0.45359237 / 3600.0);

    companion object {
        /**
         * The base unit of the mass-flow group: [KILOGRAMS_PER_SECOND]. All readings of
         * [KMassFlowUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.massflow.KGS_IN_BASE] factor).
         */
        val BASE: KMassFlowUnit = KILOGRAMS_PER_SECOND
    }
}
