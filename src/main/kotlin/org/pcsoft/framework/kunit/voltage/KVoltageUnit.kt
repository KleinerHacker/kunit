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

package org.pcsoft.framework.kunit.voltage

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **voltage** (electric potential difference, a *constructed* quantity:
 * `mass¹ · distance² · time⁻³ · current⁻¹`, i.e. `kg·m²·s⁻³·A⁻¹`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], volt): `1 unit = baseValue * V`.
 *
 * Unlike length or mass, voltage is not a "real" single-group unit - it is composed of a mass, a length,
 * a time and a current term. Each [KVoltageUnit] therefore carries a single, pre-computed factor to volts
 * (e.g. `1 abV = 10⁻⁸ V`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KVoltageUnit.ABVOLT.baseValue // 1.0E-8 (1 abV = 10⁻⁸ V)
 * ```
 */
enum class KVoltageUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Volt ("V"): the SI base unit of the voltage group ([baseValue] `1.0`). */
    VOLT("V", 1.0),

    /** Statvolt ("statV"), the voltage unit of the CGS electrostatic system (ESU), 1 statV = 299.792458 V. */
    STATVOLT("statV", 299.792458),

    /** Abvolt ("abV"), the voltage unit of the CGS electromagnetic system (EMU), 1 abV = 10⁻⁸ V. */
    ABVOLT("abV", 1e-8),

    /** Weston standard cell ("V_W"), a historical voltage reference, 1 V_W = 1.0183 V. */
    WESTON_CELL("V_W", 1.0183),

    /** Daniell cell ("V_Da"), a historical galvanic-cell voltage reference, 1 V_Da = 1.1 V. */
    DANIELL("V_Da", 1.1);

    companion object {
        /**
         * The base unit of the voltage group: [VOLT]. All internal values of [KVoltageUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KVoltageUnit = VOLT
    }
}
