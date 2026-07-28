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

package org.pcsoft.framework.kunit.electricfluxdensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric flux density** (a *constructed* quantity:
 * `current¹ · time¹ · distance⁻²`, i.e. `A·s·m⁻²` = `C/m²`). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], coulomb per square meter): `1 unit = baseValue * C/m²`.
 *
 * The electric flux density `D` (also called the electric displacement) is the charge per unit of area
 * (`D = Q / A`). The **surface charge density** `σ` is the same quantity dimensionally and is therefore
 * represented by this very group rather than by a separate one.
 *
 * Unlike length or mass it is not a "real" single-group unit - it is composed of a current, a time and a
 * distance term. Each [KElectricFluxDensityUnit] therefore carries a single, pre-computed factor to coulombs
 * per square meter, so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KElectricFluxDensityUnit.COULOMB_PER_SQUARE_CENTIMETER.baseValue // 10000.0 (1 C/cm² = 1e4 C/m²)
 * ```
 */
enum class KElectricFluxDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Coulomb per square meter ("C/m²"): the SI base unit of the electric flux density group
     * ([baseValue] `1.0`).
     */
    COULOMB_PER_SQUARE_METER("C/m²", 1.0),

    /** Coulomb per square centimeter ("C/cm²"), the common laboratory notation, 1 C/cm² = 10⁴ C/m². */
    COULOMB_PER_SQUARE_CENTIMETER("C/cm²", 1.0e4);

    companion object {
        /**
         * The base unit of the electric flux density group: [COULOMB_PER_SQUARE_METER]. All internal values
         * of [KElectricFluxDensityUnitInstance] are normalized to this unit.
         */
        val BASE: KElectricFluxDensityUnit = COULOMB_PER_SQUARE_METER
    }
}
