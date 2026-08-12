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

package org.pcsoft.framework.kunit.thermo.molarenergy

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of molar energy - energy per amount of substance (a *constructed* quantity:
 * mass · distance² · time⁻² · substance⁻¹). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], joule per mole): `1 unit = baseValue * J/mol`.
 *
 * The same quantity is also called *molar enthalpy* or *reaction enthalpy* depending on context.
 *
 * Example:
 * ```kotlin
 * KMolarEnergyUnit.CALORIE_PER_MOLE.baseValue // 4.184 (1 cal/mol = 4.184 J/mol)
 * ```
 */
enum class KMolarEnergyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Joule per mole ("J/mol"), the SI base unit of molar energy; [baseValue] = 1.0 by definition. */
    JOULE_PER_MOLE("J/mol", 1.0),

    /** Thermochemical calorie per mole ("cal/mol"), 1 cal/mol = 4.184 J/mol. */
    CALORIE_PER_MOLE("cal/mol", 4.184),

    /**
     * Electronvolt per elementary entity ("eV/entity"), the unit chemists use for per-particle energies:
     * 1 eV per particle = 96485.33212 J/mol (the Faraday constant in J/(V·mol)).
     */
    ELECTRONVOLT_PER_ENTITY("eV/entity", 96485.33212);

    companion object {
        /**
         * The base unit of the molar energy group: [JOULE_PER_MOLE]. All internal values of
         * [KMolarEnergyUnitInstance] are normalized to joules per mole.
         */
        val BASE: KMolarEnergyUnit = JOULE_PER_MOLE
    }
}
