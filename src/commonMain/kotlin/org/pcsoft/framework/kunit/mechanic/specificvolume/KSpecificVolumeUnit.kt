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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **specific volume** (volume per mass, the reciprocal of density; a
 * *constructed* quantity: length³ · mass⁻¹). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], cubic meter per kilogram): `1 unit = baseValue * m³/kg`.
 *
 * Because the mass component of this library is normalized to **grams**, the raw component base
 * (`m³·g⁻¹`) is 1/1000 of the m³/kg; that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.specificvolume.M3KG_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KSpecificVolumeUnit.LITERS_PER_KILOGRAM.baseValue // 1.0e-3 (1 l/kg = 0.001 m³/kg)
 * ```
 */
enum class KSpecificVolumeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Cubic meter per kilogram ("m^3/kg"), the SI base unit of specific volume; [baseValue] = 1.0. */
    CUBIC_METERS_PER_KILOGRAM("m^3/kg", 1.0),

    /** Liter per kilogram ("l/kg"), 1 l/kg = 0.001 m³/kg. */
    LITERS_PER_KILOGRAM("l/kg", 1.0e-3),

    /** Cubic centimeter per gram ("cm^3/g"), 1 cm³/g = 0.001 m³/kg (numerically equal to l/kg). */
    CUBIC_CENTIMETERS_PER_GRAM("cm^3/g", 1.0e-3),

    /** Cubic foot per pound ("ft^3/lb"), 1 ft³/lb ≈ 0.0624280 m³/kg. */
    CUBIC_FEET_PER_POUND("ft^3/lb", 0.028316846592 / 0.45359237);

    companion object {
        /**
         * The base unit of the specific-volume group: [CUBIC_METERS_PER_KILOGRAM]. All readings of
         * [KSpecificVolumeUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.specificvolume.M3KG_IN_BASE] factor).
         */
        val BASE: KSpecificVolumeUnit = CUBIC_METERS_PER_KILOGRAM
    }
}
