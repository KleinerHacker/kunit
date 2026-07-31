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

package org.pcsoft.framework.kunit.thermo.molarmass

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **molar mass** (mass per amount of substance; a *constructed* quantity:
 * mass · substance⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE], gram per
 * mole): `1 unit = baseValue * g/mol`.
 *
 * The gram per mole is chosen as the base because it is the unit chemistry actually works in: the molar
 * mass of an element in g/mol is numerically its relative atomic mass.
 *
 * Example:
 * ```kotlin
 * KMolarMassUnit.KILOGRAM_PER_MOLE.baseValue // 1000.0 (1 kg/mol = 1000 g/mol)
 * ```
 */
enum class KMolarMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Gram per mole ("g/mol"), the base unit of this group; [baseValue] = 1.0 by definition. */
    GRAM_PER_MOLE("g/mol", 1.0),

    /** Kilogram per mole ("kg/mol"), the coherent SI form; 1 kg/mol = 1000 g/mol. */
    KILOGRAM_PER_MOLE("kg/mol", 1000.0),

    /**
     * Pound per pound-mole ("lb/lbmol"): the imperial engineering spelling. Because the pound-mole is
     * defined so that its mass in pounds equals the molar mass, it is numerically identical to g/mol.
     */
    POUND_PER_POUND_MOLE("lb/lbmol", 1.0),

    /**
     * Dalton per elementary entity ("Da"), the atomic mass unit view of the same quantity. Since the 2019
     * SI redefinition the molar mass constant is no longer exactly 1 g/mol: 1 Da per entity =
     * 1.00000000105 g/mol.
     */
    DALTON_PER_ENTITY("Da", 1.00000000105);

    companion object {
        /**
         * The base unit of the molar-mass group: [GRAM_PER_MOLE]. All internal values of
         * [KMolarMassUnitInstance] are normalized to grams per mole.
         */
        val BASE: KMolarMassUnit = GRAM_PER_MOLE
    }
}
