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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **molar volume** (volume per amount of substance; a *constructed* quantity:
 * length³ · substance⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE], cubic
 * meter per mole): `1 unit = baseValue * m³/mol`.
 *
 * Example:
 * ```kotlin
 * KMolarVolumeUnit.LITERS_PER_MOLE.baseValue // 1.0e-3 (1 l/mol = 0.001 m³/mol)
 * ```
 */
enum class KMolarVolumeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Cubic meter per mole ("m^3/mol"), the coherent SI base unit of molar volume; [baseValue] = 1.0. */
    CUBIC_METERS_PER_MOLE("m^3/mol", 1.0),

    /** Liter per mole ("l/mol"), the customary chemistry spelling; 1 l/mol = 0.001 m³/mol. */
    LITERS_PER_MOLE("l/mol", 1.0e-3),

    /** Cubic centimeter per mole ("cm^3/mol"), used for condensed phases; 1 cm³/mol = 1e-6 m³/mol. */
    CUBIC_CENTIMETERS_PER_MOLE("cm^3/mol", 1.0e-6);

    companion object {
        /**
         * The base unit of the molar-volume group: [CUBIC_METERS_PER_MOLE]. All internal values of
         * [KMolarVolumeUnitInstance] are normalized to cubic meters per mole.
         */
        val BASE: KMolarVolumeUnit = CUBIC_METERS_PER_MOLE
    }
}

/**
 * The **molar volume of an ideal gas at standard conditions** (STP: 0 °C, 100 kPa) in cubic meters per
 * mole: 0.022711 m³/mol = 22.711 l/mol. Exposed as a plain [Double] in [KMolarVolumeUnit.BASE] units so it
 * can be used with any construction form (`MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole`).
 *
 * Example:
 * ```kotlin
 * val v = (2 of moles) * (MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole) // ≈ 0.04542 m³
 * ```
 */
const val MOLAR_VOLUME_IDEAL_GAS_STP: Double = 0.02271095464
