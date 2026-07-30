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

package org.pcsoft.framework.kunit.thermo.specificenergy

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of specific energy - energy per unit of mass (a *constructed* quantity:
 * distance² · time⁻²). [baseValue] is the factor to convert into the group's base unit ([BASE], joule per
 * kilogram): `1 unit = baseValue * J/kg`.
 *
 * The same quantity is also called *specific enthalpy*, *specific latent heat* or *calorific value*
 * depending on context; they all share this unit group.
 *
 * Example:
 * ```kotlin
 * KSpecificEnergyUnit.CALORIE_PER_GRAM.baseValue // 4184.0 (1 cal/g = 4184 J/kg)
 * ```
 */
enum class KSpecificEnergyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Joule per kilogram ("J/kg"), the SI base unit of specific energy; [baseValue] = 1.0 by definition. */
    JOULE_PER_KILOGRAM("J/kg", 1.0),

    /** Thermochemical calorie per gram ("cal/g"), 1 cal/g = 4184 J/kg. */
    CALORIE_PER_GRAM("cal/g", 4184.0),

    /** Watt-hour per kilogram ("Wh/kg"), 1 Wh/kg = 3600 J/kg (the battery energy-density unit). */
    WATT_HOUR_PER_KILOGRAM("Wh/kg", 3600.0),

    /** British thermal unit per pound ("Btu/lb"), 1 Btu/lb = 2326 J/kg. */
    BTU_PER_POUND("Btu/lb", 2326.0);

    companion object {
        /**
         * The base unit of the specific energy group: [JOULE_PER_KILOGRAM]. All internal values of
         * [KSpecificEnergyUnitInstance] are normalized to joules per kilogram.
         */
        val BASE: KSpecificEnergyUnit = JOULE_PER_KILOGRAM
    }
}
