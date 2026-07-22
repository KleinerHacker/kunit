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

package org.pcsoft.framework.kunit.resistance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electrical resistance** (a *constructed* quantity:
 * `mass¹ · distance² · time⁻³ · current⁻²`, i.e. `kg·m²·s⁻³·A⁻²`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], ohm): `1 unit = baseValue * Ω`.
 *
 * Unlike length or mass, resistance is not a "real" single-group unit - it is composed of a mass, a length,
 * a time and a current term. Each [KResistanceUnit] therefore carries a single, pre-computed factor to ohms
 * (e.g. `1 abΩ = 10⁻⁹ Ω`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KResistanceUnit.ABOHM.baseValue // 1.0E-9 (1 abΩ = 10⁻⁹ Ω)
 * ```
 */
enum class KResistanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ohm ("Ω"): the SI base unit of the resistance group ([baseValue] `1.0`). */
    OHM("Ω", 1.0),

    /** Statohm ("statΩ"), the resistance unit of the CGS electrostatic system (ESU), 1 statΩ = 8.98755179e11 Ω. */
    STATOHM("statΩ", 8.98755179e11),

    /** Abohm ("abΩ"), the resistance unit of the CGS electromagnetic system (EMU), 1 abΩ = 10⁻⁹ Ω. */
    ABOHM("abΩ", 1e-9),

    /** International ohm ("Ω_int"), a historical resistance reference, 1 Ω_int = 1.000049 Ω. */
    INTERNATIONAL_OHM("Ω_int", 1.000049),

    /** Legal ohm ("Ω_leg"), a historical resistance reference, 1 Ω_leg = 0.9972 Ω. */
    LEGAL_OHM("Ω_leg", 0.9972),

    /** Siemens mercury unit ("Ω_S"), a historical resistance reference, 1 Ω_S = 0.9534 Ω. */
    SIEMENS_UNIT("Ω_S", 0.9534);

    companion object {
        /**
         * The base unit of the resistance group: [OHM]. All internal values of [KResistanceUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KResistanceUnit = OHM
    }
}
