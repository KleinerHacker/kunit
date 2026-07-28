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

package org.pcsoft.framework.kunit.electricdipolemoment

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electric dipole moment** (a *constructed* quantity:
 * `current¹ · time¹ · distance¹`, i.e. `A·s·m` = `C·m`). [baseValue] is the factor to convert into the
 * group's base unit ([BASE], coulomb meter): `1 unit = baseValue * C·m`.
 *
 * The electric dipole moment `p = Q · d` measures the separation of a positive and a negative charge. Unlike
 * length or mass it is not a "real" single-group unit - it is composed of a current, a time and a distance
 * term. Each [KElectricDipoleMomentUnit] therefore carries a single, pre-computed factor to coulomb meters,
 * so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KElectricDipoleMomentUnit.DEBYE.baseValue // 3.335640952e-30 (1 D = 3.335 640 952e-30 C·m)
 * ```
 */
enum class KElectricDipoleMomentUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Coulomb meter ("C·m"): the SI base unit of the electric dipole moment group ([baseValue] `1.0`). */
    COULOMB_METER("C·m", 1.0),

    /**
     * Debye ("D"), the CGS unit dominating molecular physics and chemistry,
     * 1 D = 3.335 640 952 × 10⁻³⁰ C·m (= 10⁻¹⁸ statC·cm).
     */
    DEBYE("D", 3.335640952e-30);

    companion object {
        /**
         * The base unit of the electric dipole moment group: [COULOMB_METER]. All internal values of
         * [KElectricDipoleMomentUnitInstance] are normalized to this unit.
         */
        val BASE: KElectricDipoleMomentUnit = COULOMB_METER
    }
}
