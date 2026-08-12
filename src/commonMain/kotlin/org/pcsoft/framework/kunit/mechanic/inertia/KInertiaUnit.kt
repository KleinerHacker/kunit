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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of the **moment of inertia** (a *constructed* quantity: mass · length²).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], kilogram meter squared):
 * `1 unit = baseValue * kg·m²`.
 *
 * Because the mass component of this library is normalized to **grams**, the kg·m² is 1000× the raw
 * component base (`g·m²`); that fixed factor is
 * [org.pcsoft.framework.kunit.mechanic.inertia.KGM2_IN_BASE] and is applied by the
 * [KInertiaUnitInstance] factory, not by these [baseValue]s.
 *
 * Example:
 * ```kotlin
 * KInertiaUnit.GRAM_CENTIMETERS_SQUARED.baseValue // 1.0e-7 (1 g·cm² = 1e-7 kg·m²)
 * ```
 */
enum class KInertiaUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram meter squared ("kg*m^2"), the SI base unit of the moment of inertia; [baseValue] = 1.0. */
    KILOGRAM_METERS_SQUARED("kg*m^2", 1.0),

    /** Gram centimeter squared ("g*cm^2"), the CGS unit: 1 g·cm² = 1e-7 kg·m². */
    GRAM_CENTIMETERS_SQUARED("g*cm^2", 1.0e-7),

    /** Pound-foot squared ("lb*ft^2"), 1 lb·ft² ≈ 0.0421401 kg·m². */
    POUND_FEET_SQUARED("lb*ft^2", 0.45359237 * 0.3048 * 0.3048);

    companion object {
        /**
         * The base unit of the moment-of-inertia group: [KILOGRAM_METERS_SQUARED]. All readings of
         * [KInertiaUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.inertia.KGM2_IN_BASE] factor).
         */
        val BASE: KInertiaUnit = KILOGRAM_METERS_SQUARED
    }
}
