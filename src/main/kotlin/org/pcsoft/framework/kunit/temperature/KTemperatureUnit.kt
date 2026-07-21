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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **temperature** group with base unit [KELVIN] ([baseValue] `1.0`).
 *
 * Temperature is the framework's **first and (by decision) permanent affine exception**: unlike every
 * other group, converting between temperature units is not a single multiplicative factor but an
 * offset-and-scale (affine) transform - `25 °C` is not `25 × 1 °C`. The multiplicative engine
 * ([baseValue], `N of unit` = `scaledBy`) therefore cannot express these conversions on its own.
 *
 * To keep the shared engine unchanged (no breaking change), each unit additionally carries the two
 * lambdas [toBase] (reading value → absolute kelvin) and [fromBase] (absolute kelvin → reading value).
 * Values are stored internally as **absolute kelvin** (a single `KELVIN^1` term), so `*`/`/`/`pow` keep
 * running linearly through the generic engine; only construction and reading go through the affine
 * lambdas (see `KTemperatureUnitInstance`).
 *
 * The group deliberately offers **no prefixes** (explicit exclusion, permitted by `architecture-prefix.md`).
 *
 * **Related group:** this is the *absolute* (affine) temperature. Its *linear* counterpart - the interval
 * between two temperatures - is the separate [KTemperatureDifferenceUnit] group. Subtracting two absolute
 * temperatures (`KTemperatureUnitInstance.minus`) yields a [KTemperatureDifferenceUnitInstance] (kelvin),
 * and a difference can be added to / subtracted from an absolute temperature to yield an absolute
 * temperature again. `AbsTemp + AbsTemp` is deliberately unavailable (physically meaningless).
 *
 * Conversions:
 * - `°C = K − 273.15`
 * - `°F = (K − 273.15) · 9/5 + 32`
 * - `°R = K · 9/5` (Rankine: absolute scale, zero at absolute zero, Fahrenheit-sized degrees)
 *
 * Example:
 * ```kotlin
 * (0 of celsius) into kelvin        // 273.15
 * (100 of celsius) into fahrenheit  // 212.0
 * (32 of fahrenheit) into celsius   // 0.0
 * ```
 */
enum class KTemperatureUnit(
    override val symbol: String,
    /**
     * Converts a value read in this unit into absolute kelvin (the group's base). Part of the affine
     * exception: this replaces the purely multiplicative [baseValue] path for construction.
     */
    val toBase: (Double) -> Double,
    /**
     * Converts an absolute kelvin value into a value read in this unit. Part of the affine exception:
     * this replaces the purely multiplicative [baseValue] path for reading.
     */
    val fromBase: (Double) -> Double,
) : KUnit {
    /** Kelvin ("K"): the base unit of the temperature group ([baseValue] `1.0`), identity affine transform. */
    KELVIN("K", { it }, { it }),

    /** Degree Celsius ("°C"): `K = °C + 273.15`, `°C = K − 273.15`. */
    CELSIUS("°C", { it + 273.15 }, { it - 273.15 }),

    /** Degree Fahrenheit ("°F"): `K = (°F − 32) · 5/9 + 273.15`, `°F = (K − 273.15) · 9/5 + 32`. */
    FAHRENHEIT("°F", { (it - 32.0) * 5.0 / 9.0 + 273.15 }, { (it - 273.15) * 9.0 / 5.0 + 32.0 }),

    /** Degree Rankine ("°R"): absolute scale with Fahrenheit-sized degrees, `K = °R · 5/9`, `°R = K · 9/5`. */
    RANKINE("°R", { it * 5.0 / 9.0 }, { it * 9.0 / 5.0 });

    /**
     * All temperature units share the linear base scale of kelvin ([baseValue] `1.0`); the affine
     * offset lives in [toBase]/[fromBase], never in the multiplicative engine.
     */
    override val baseValue: Double = 1.0

    companion object {
        /** The base unit of the temperature group: [KELVIN]. */
        val BASE: KTemperatureUnit = KELVIN
    }
}
