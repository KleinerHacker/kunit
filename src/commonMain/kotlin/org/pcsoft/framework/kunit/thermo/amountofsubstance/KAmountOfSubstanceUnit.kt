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

package org.pcsoft.framework.kunit.thermo.amountofsubstance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of amount of substance (a *native*, measurable SI base quantity).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], mole): `1 unit = baseValue * mol`.
 *
 * Example:
 * ```kotlin
 * KAmountOfSubstanceUnit.POUND_MOLE.baseValue // 453.59237 (1 lbmol = 453.59237 mol)
 * ```
 */
enum class KAmountOfSubstanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Mole ("mol"), the SI base unit of amount of substance; [baseValue] = 1.0 by definition. */
    MOLE("mol", 1.0),

    /**
     * Pound-mole ("lbmol"), the imperial/US engineering counterpart: the amount whose mass in **pounds**
     * equals the substance's molar mass. 1 lbmol = 453.59237 mol.
     */
    POUND_MOLE("lbmol", 453.59237);

    companion object {
        /**
         * The base unit of the amount-of-substance group: [MOLE]. All internal values of
         * [KAmountOfSubstanceUnitInstance] are normalized to moles.
         */
        val BASE: KAmountOfSubstanceUnit = MOLE
    }
}

/**
 * The Avogadro constant `N_A` = 6.02214076e23 mol⁻¹ (exact, SI definition of the mole): the number of
 * elementary entities per mole. Exposed as a plain [Double] because a particle count is dimensionless.
 *
 * Example:
 * ```kotlin
 * (2 of moles).value * AVOGADRO_CONSTANT // ≈ 1.2044e24 particles
 * ```
 */
const val AVOGADRO_CONSTANT: Double = 6.02214076e23
