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

package org.pcsoft.framework.kunit.electric.fieldstrength

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric field strength** (a *constructed* quantity:
 * `mass¹ · distance¹ · time⁻³ · current⁻¹`, i.e. `kg·m·s⁻³·A⁻¹`). [baseValue] is the factor to convert into
 * the group's base unit ([BASE], volt per meter): `1 unit = baseValue * V/m`.
 *
 * The electric field strength `E` describes the voltage drop per unit of length (`E = U / l`) and, equally,
 * the force acting on a unit charge (`E = F / Q`). Unlike length or mass it is not a "real" single-group
 * unit - it is composed of a mass, a distance, a time and a current term. Each [KElectricFieldStrengthUnit]
 * therefore carries a single, pre-computed factor to volts per meter, so it can still be used as a plain
 * [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue // 100.0 (1 V/cm = 100 V/m)
 * ```
 */
enum class KElectricFieldStrengthUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    // --- SI -------------------------------------------------------------------------------------

    /** Volt per meter ("V/m"): the SI base unit of the electric field strength group ([baseValue] `1.0`). */
    VOLT_PER_METER("V/m", 1.0),

    /** Volt per centimeter ("V/cm"), the common laboratory notation, 1 V/cm = 100 V/m. */
    VOLT_PER_CENTIMETER("V/cm", 100.0),

    // --- CGS ------------------------------------------------------------------------------------

    /**
     * Statvolt per centimeter ("statV/cm"), the field strength unit of the CGS electrostatic system (ESU),
     * 1 statV/cm = 29 979.2458 V/m (1 statV = 299.792458 V, per centimeter).
     */
    STATVOLT_PER_CENTIMETER("statV/cm", 29979.2458);

    companion object {
        /**
         * The base unit of the electric field strength group: [VOLT_PER_METER]. All internal values of
         * [KElectricFieldStrengthUnitInstance] are normalized to this unit.
         */
        val BASE: KElectricFieldStrengthUnit = VOLT_PER_METER
    }
}
