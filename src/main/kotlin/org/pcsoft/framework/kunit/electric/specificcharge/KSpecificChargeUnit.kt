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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **specific charge** (charge per mass; a *constructed* quantity:
 * current · time · mass⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE],
 * coulomb per kilogram): `1 unit = baseValue * C/kg`.
 *
 * The same dimension carries a second reading: the **ionisation dose** (exposure) of radiation
 * protection, historically measured in roentgen. A single normal form maps to a single type, so both
 * readings share this group - distinguish them by naming your values.
 *
 * Example:
 * ```kotlin
 * KSpecificChargeUnit.ROENTGEN.baseValue // 2.58e-4 (1 R = 2.58e-4 C/kg)
 * ```
 */
enum class KSpecificChargeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Coulomb per kilogram ("C/kg"), the coherent SI unit; [baseValue] = 1.0. */
    COULOMB_PER_KILOGRAM("C/kg", 1.0),

    /**
     * Roentgen ("R"), the historical unit of the ionisation dose: the exposure producing one
     * electrostatic unit of charge per cubic centimetre of dry air. 1 R = 2.58 × 10⁻⁴ C/kg.
     */
    ROENTGEN("R", 2.58e-4);

    companion object {
        /**
         * The base unit of the specific-charge group: [COULOMB_PER_KILOGRAM]. All internal values of
         * [KSpecificChargeUnitInstance] are normalized to coulombs per kilogram.
         */
        val BASE: KSpecificChargeUnit = COULOMB_PER_KILOGRAM
    }
}

/**
 * The **specific charge of the electron** `|e|/mₑ` = 1.758 820 010 76 × 10¹¹ C/kg - the magnitude of the
 * electron's charge-to-mass ratio, the quantity Thomson measured to identify the electron. Exposed as a
 * plain [Double] in [KSpecificChargeUnit.BASE] units so it can be used with any construction form
 * (`ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram`).
 *
 * The sign is omitted: the electron's charge is negative, but the ratio is quoted as a magnitude.
 *
 * Example:
 * ```kotlin
 * val q = (ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram) * (1 of grams) // charge of 1 g of electrons
 * ```
 */
const val ELECTRON_SPECIFIC_CHARGE: Double = 1.75882001076e11
