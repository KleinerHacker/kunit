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

package org.pcsoft.framework.kunit.electric.charge

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric charge** (a *constructed* quantity: `current¹ · time¹`, i.e.
 * `A·s`). [baseValue] is the factor to convert into the group's base unit ([BASE], coulomb):
 * `1 unit = baseValue * C`.
 *
 * Unlike length or mass, charge is not a "real" single-group unit - it is composed of a current and a time
 * term. Each [KChargeUnit] therefore carries a single, pre-computed factor to coulombs (e.g.
 * `1 Ah = 3600 C`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KChargeUnit.AMPERE_HOUR.baseValue // 3600.0 (1 Ah = 3600 C)
 * ```
 */
enum class KChargeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Coulomb ("C"): the SI base unit of the charge group ([baseValue] `1.0`). */
    COULOMB("C", 1.0),

    /** Ampere second ("As"), the explicit `A·s` spelling of the coulomb, 1 As = 1 C. */
    AMPERE_SECOND("As", 1.0),

    /** Ampere hour ("Ah"), the common battery capacity unit, 1 Ah = 3600 C. */
    AMPERE_HOUR("Ah", 3600.0),

    /** Abcoulomb ("abC"), the charge unit of the CGS electromagnetic system (EMU), 1 abC = 10 C. */
    ABCOULOMB("abC", 10.0),

    /** Statcoulomb ("statC"), the charge unit of the CGS electrostatic system (ESU), 1 statC = 3.335641e-10 C. */
    STATCOULOMB("statC", 3.335641e-10),

    /** Faraday ("F_c"), the charge of one mole of elementary charges, 1 F_c = 96485.332 C. */
    FARADAY("F_c", 96485.332),

    /** Elementary charge ("e"), the charge of a single proton, 1 e = 1.602176634e-19 C. */
    ELEMENTARY_CHARGE("e", 1.602176634e-19);

    companion object {
        /**
         * The base unit of the charge group: [COULOMB]. All internal values of [KChargeUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KChargeUnit = COULOMB
    }
}
