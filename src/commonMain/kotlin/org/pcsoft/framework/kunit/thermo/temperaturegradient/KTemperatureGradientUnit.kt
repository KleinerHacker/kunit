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

package org.pcsoft.framework.kunit.thermo.temperaturegradient

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of temperature gradient - temperature change per unit of length (a
 * *constructed* quantity: temperature · distance⁻¹). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], kelvin per meter): `1 unit = baseValue * K/m`.
 *
 * Example:
 * ```kotlin
 * KTemperatureGradientUnit.KELVIN_PER_KILOMETER.baseValue // 0.001 (the geothermal gradient's unit)
 * ```
 */
enum class KTemperatureGradientUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kelvin per meter ("K/m"), the SI base unit of temperature gradient; [baseValue] = 1.0. */
    KELVIN_PER_METER("K/m", 1.0),

    /** Kelvin per kilometer ("K/km"), 1 K/km = 0.001 K/m (the unit of geothermal/atmospheric gradients). */
    KELVIN_PER_KILOMETER("K/km", 1.0e-3),

    /**
     * Degree Fahrenheit per foot ("°F/ft"), 1 °F/ft = (5/9) K / 0.3048 m ≈ 1.822689 K/m. Note that a
     * Fahrenheit *interval* is 5/9 K - the offset of the absolute scale plays no role in a gradient.
     */
    FAHRENHEIT_PER_FOOT("°F/ft", (5.0 / 9.0) / 0.3048);

    companion object {
        /**
         * The base unit of the temperature gradient group: [KELVIN_PER_METER]. All internal values of
         * [KTemperatureGradientUnitInstance] are normalized to K/m.
         */
        val BASE: KTemperatureGradientUnit = KELVIN_PER_METER
    }
}
