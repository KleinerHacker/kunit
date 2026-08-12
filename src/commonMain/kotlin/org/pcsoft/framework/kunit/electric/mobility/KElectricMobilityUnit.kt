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

package org.pcsoft.framework.kunit.electric.mobility

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric mobility** (a *constructed* quantity:
 * `mass⁻¹ · time² · current¹`, i.e. `kg⁻¹·s²·A` = `m²/(V·s)`). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], square meter per volt second): `1 unit = baseValue * m²/(V·s)`.
 *
 * The electric mobility `μ` describes how fast a charge carrier drifts in an electric field
 * (`v = μ · E`). It has **no named unit of its own**: every spelling is a ratio, so the enum carries the SI
 * ratio plus the semiconductor community's centimeter form, and values are typically built through the typed
 * operator (`speed / electricFieldStrength`).
 *
 * Example:
 * ```kotlin
 * KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND.baseValue // 1.0E-4
 * ```
 */
enum class KElectricMobilityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Square meter per volt second ("m²/(V·s)"): the SI base unit of the electric mobility group
     * ([baseValue] `1.0`).
     */
    SQUARE_METER_PER_VOLT_SECOND("m²/(V·s)", 1.0),

    /**
     * Square centimeter per volt second ("cm²/(V·s)"), the notation used throughout semiconductor physics,
     * 1 cm²/(V·s) = 10⁻⁴ m²/(V·s).
     */
    SQUARE_CENTIMETER_PER_VOLT_SECOND("cm²/(V·s)", 1.0e-4);

    companion object {
        /**
         * The base unit of the electric mobility group: [SQUARE_METER_PER_VOLT_SECOND]. All internal values
         * of [KElectricMobilityUnitInstance] are normalized to this unit.
         */
        val BASE: KElectricMobilityUnit = SQUARE_METER_PER_VOLT_SECOND
    }
}
