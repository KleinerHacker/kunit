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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **molar conductivity** (electrolytic conductivity per concentration; a
 * *constructed* quantity: mass⁻¹ · time³ · current² · substance⁻¹). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], siemens square meter per mole):
 * `1 unit = baseValue * S·m²/mol`.
 *
 * Example:
 * ```kotlin
 * KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE.baseValue // 1e-4
 * ```
 */
enum class KMolarConductivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Siemens square meter per mole ("S*m^2/mol"), the coherent SI unit; [baseValue] = 1.0. */
    SIEMENS_SQUARE_METER_PER_MOLE("S*m^2/mol", 1.0),

    /**
     * Siemens square centimeter per mole ("S*cm^2/mol"), the customary electrochemistry spelling;
     * 1 S·cm²/mol = 1e-4 S·m²/mol.
     */
    SIEMENS_SQUARE_CENTIMETER_PER_MOLE("S*cm^2/mol", 1.0e-4);

    companion object {
        /**
         * The base unit of the molar-conductivity group: [SIEMENS_SQUARE_METER_PER_MOLE]. All internal
         * values of [KMolarConductivityUnitInstance] are normalized to siemens square meters per mole.
         */
        val BASE: KMolarConductivityUnit = SIEMENS_SQUARE_METER_PER_MOLE
    }
}
