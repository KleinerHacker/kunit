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

package org.pcsoft.framework.kunit.thermo.concentration

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **amount-of-substance concentration** (amount of substance per volume; a
 * *constructed* quantity: substance · length⁻³). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], mole per cubic meter): `1 unit = baseValue * mol/m³`.
 *
 * Example:
 * ```kotlin
 * KConcentrationUnit.MOLES_PER_LITER.baseValue // 1000.0 (1 mol/l = 1000 mol/m³)
 * ```
 */
enum class KConcentrationUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Mole per cubic meter ("mol/m^3"), the coherent SI base unit of concentration; [baseValue] = 1.0. */
    MOLES_PER_CUBIC_METER("mol/m^3", 1.0),

    /**
     * Mole per liter ("mol/l"), the **molarity** of classical chemistry, usually written `M`.
     * 1 mol/l = 1000 mol/m³.
     */
    MOLES_PER_LITER("mol/l", 1000.0),

    /** Millimole per liter ("mmol/l"), the unit of clinical blood values; 1 mmol/l = 1 mol/m³. */
    MILLIMOLES_PER_LITER("mmol/l", 1.0);

    companion object {
        /**
         * The base unit of the concentration group: [MOLES_PER_CUBIC_METER]. All internal values of
         * [KConcentrationUnitInstance] are normalized to moles per cubic meter.
         */
        val BASE: KConcentrationUnit = MOLES_PER_CUBIC_METER
    }
}
