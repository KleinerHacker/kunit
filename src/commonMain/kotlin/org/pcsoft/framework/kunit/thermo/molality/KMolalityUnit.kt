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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **molality** (amount of substance per mass of solvent; a *constructed*
 * quantity: substance · mass⁻¹). [baseValue] is the factor to convert into the group's base unit ([BASE],
 * mole per kilogram): `1 unit = baseValue * mol/kg`.
 *
 * Unlike the [concentration][org.pcsoft.framework.kunit.thermo.concentration.KConcentrationUnit], the
 * molality refers to the **mass** of the solvent, not its volume - which makes it independent of thermal
 * expansion and therefore the quantity of choice for colligative properties.
 *
 * Example:
 * ```kotlin
 * KMolalityUnit.MILLIMOLES_PER_KILOGRAM.baseValue // 0.001 (1 mmol/kg = 0.001 mol/kg)
 * ```
 */
enum class KMolalityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Mole per kilogram ("mol/kg"), the coherent SI unit of molality; [baseValue] = 1.0. */
    MOLES_PER_KILOGRAM("mol/kg", 1.0),

    /** Millimole per kilogram ("mmol/kg"), used for trace amounts; 1 mmol/kg = 0.001 mol/kg. */
    MILLIMOLES_PER_KILOGRAM("mmol/kg", 1.0e-3);

    companion object {
        /**
         * The base unit of the molality group: [MOLES_PER_KILOGRAM]. All internal values of
         * [KMolalityUnitInstance] are normalized to moles per kilogram.
         */
        val BASE: KMolalityUnit = MOLES_PER_KILOGRAM
    }
}
