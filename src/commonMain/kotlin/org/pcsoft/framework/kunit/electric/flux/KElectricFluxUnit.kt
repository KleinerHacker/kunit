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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric flux** (electric field strength through an area; a *constructed*
 * quantity: mass · length³ · time⁻³ · current⁻¹). [baseValue] is the factor to convert into the group's
 * base unit ([BASE], volt meter): `1 unit = baseValue * V·m`.
 *
 * Example:
 * ```kotlin
 * KElectricFluxUnit.VOLT_METER.baseValue // 1.0
 * ```
 */
enum class KElectricFluxUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Volt meter ("V*m"), the coherent SI unit of electric flux; [baseValue] = 1.0. */
    VOLT_METER("V*m", 1.0),

    /** Volt centimeter ("V*cm"), the small-geometry spelling; 1 V·cm = 0.01 V·m. */
    VOLT_CENTIMETER("V*cm", 1.0e-2);

    companion object {
        /**
         * The base unit of the electric-flux group: [VOLT_METER]. All internal values of
         * [KElectricFluxUnitInstance] are normalized to volt meters.
         */
        val BASE: KElectricFluxUnit = VOLT_METER
    }
}
