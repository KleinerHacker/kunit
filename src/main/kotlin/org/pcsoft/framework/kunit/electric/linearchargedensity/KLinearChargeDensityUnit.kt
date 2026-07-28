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

package org.pcsoft.framework.kunit.electric.linearchargedensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **linear charge density** (a *constructed* quantity:
 * `current¹ · time¹ · distance⁻¹`, i.e. `A·s·m⁻¹` = `C/m`). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], coulomb per meter): `1 unit = baseValue * C/m`.
 *
 * The linear charge density `λ` is the charge carried per unit of length (`λ = Q / l`), e.g. along a wire or
 * a charged filament. It has **no named unit of its own**: every spelling is a ratio (C/m, µC/cm), so the
 * enum carries a single display marker and values are built from expressions
 * (`coulombs / meters`) or through the typed operators.
 *
 * Example:
 * ```kotlin
 * val lambda = (5 of micro.coulombs) / (2 of meters) // 2.5e-6 C/m
 * ```
 */
enum class KLinearChargeDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /**
     * Coulomb per meter ("C/m"): the SI base unit of the linear charge density group ([baseValue] `1.0`) and
     * its only entry - every other spelling is expressed as a prefixed ratio instead.
     */
    COULOMB_PER_METER("C/m", 1.0);

    companion object {
        /**
         * The base unit of the linear charge density group: [COULOMB_PER_METER]. All internal values of
         * [KLinearChargeDensityUnitInstance] are normalized to this unit.
         */
        val BASE: KLinearChargeDensityUnit = COULOMB_PER_METER
    }
}
