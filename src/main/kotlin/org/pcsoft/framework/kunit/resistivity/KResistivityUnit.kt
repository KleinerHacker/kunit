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

package org.pcsoft.framework.kunit.resistivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electrical resistivity** (a *constructed* quantity:
 * `mass¹ · distance³ · time⁻³ · current⁻²`, i.e. `kg·m³·s⁻³·A⁻²`). [baseValue] is the factor to convert into
 * the group's base unit ([BASE], ohm meter): `1 unit = baseValue * Ω·m`.
 *
 * Resistivity is the material property behind a resistance (`ρ = R · A / l`) and the reciprocal of
 * conductivity (`ρ = 1 / σ`). Unlike length or mass it is not a "real" single-group unit - it is composed of
 * a mass, a distance, a time and a current term. Each [KResistivityUnit] therefore carries a single,
 * pre-computed factor to ohm meters (e.g. `1 Ω·cm = 0.01 Ω·m`), so it can still be used as a plain
 * [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KResistivityUnit.OHM_CENTIMETER.baseValue // 0.01 (1 Ω·cm = 0.01 Ω·m)
 * ```
 */
enum class KResistivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ohm meter ("Ω·m"): the SI base unit of the resistivity group ([baseValue] `1.0`). */
    OHM_METER("Ω·m", 1.0),

    /** Ohm centimeter ("Ω·cm"), the common semiconductor unit, 1 Ω·cm = 0.01 Ω·m. */
    OHM_CENTIMETER("Ω·cm", 0.01),

    /** Statohm centimeter ("statΩ·cm"), the resistivity unit of the CGS electrostatic system (ESU), 1 statΩ·cm = 8.98755179e9 Ω·m. */
    STATOHM_CENTIMETER("statΩ·cm", 8.98755179e9);

    companion object {
        /**
         * The base unit of the resistivity group: [OHM_METER]. All internal values of
         * [KResistivityUnitInstance] are normalized to this unit.
         */
        val BASE: KResistivityUnit = OHM_METER
    }
}
