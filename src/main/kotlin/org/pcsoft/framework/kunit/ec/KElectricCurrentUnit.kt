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

package org.pcsoft.framework.kunit.ec

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **electric current** group.
 *
 * The group is one-dimensional (no exponent-specialized subtypes) and its base unit is [AMPERE]
 * ([baseValue] `1.0`). Every [baseValue] is the factor to convert into the group's base unit
 * ([BASE], ampere): `1 unit = baseValue * ampere`.
 *
 * Besides the SI ampere the group offers the two classic CGS current units: the [BIOT] (abampere) of
 * the electromagnetic system (`1 Bi = 10 A`) and the [STATAMPERE] of the electrostatic system
 * (`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`).
 *
 * Example:
 * ```kotlin
 * KElectricCurrentUnit.BIOT.baseValue // 10.0 (1 Bi = 10 A)
 * ```
 */
enum class KElectricCurrentUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    // --- SI -------------------------------------------------------------------------------------

    /** Ampere ("A"): the SI base unit of the electric current group ([baseValue] `1.0`). */
    AMPERE("A", 1.0),

    // --- CGS ------------------------------------------------------------------------------------

    /**
     * Biot / abampere ("Bi", also written "abA"), the current unit of the CGS electromagnetic system
     * (EMU), 1 Bi = 10 A.
     */
    BIOT("Bi", 10.0),

    /**
     * Statampere ("statA"), the current unit of the CGS electrostatic system (ESU),
     * 1 statA ≈ 3.335 641 × 10⁻¹⁰ A.
     */
    STATAMPERE("statA", 3.335641e-10);

    companion object {
        /**
         * The base unit of the electric current group: [AMPERE]. All internal values of
         * [KElectricCurrentUnitInstance] are normalized to this unit.
         */
        val BASE: KElectricCurrentUnit = AMPERE
    }
}
