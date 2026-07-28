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

package org.pcsoft.framework.kunit.permeability

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **permeability** (a *constructed* quantity:
 * `mass¹ · distance¹ · time⁻² · current⁻²`, i.e. `kg·m·s⁻²·A⁻²` = `H/m`). [baseValue] is the factor to
 * convert into the group's base unit ([BASE], henry per meter): `1 unit = baseValue * H/m`.
 *
 * The permeability `μ` is the magnetic constant of a material: it links the magnetic flux density to the
 * field strength (`μ = B / H`) and the inductance to the coil geometry (`μ = L · l / (N² · A)`). Unlike
 * length or mass it is not a "real" single-group unit - it is composed of a mass, a distance, a time and a
 * current term. Each [KPermeabilityUnit] therefore carries a single, pre-computed factor to henries per
 * meter, so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KPermeabilityUnit.HENRY_PER_CENTIMETER.baseValue // 100.0 (1 H/cm = 100 H/m)
 * ```
 */
enum class KPermeabilityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Henry per meter ("H/m"): the SI base unit of the permeability group ([baseValue] `1.0`). */
    HENRY_PER_METER("H/m", 1.0),

    /** Henry per centimeter ("H/cm"), the common laboratory notation, 1 H/cm = 100 H/m. */
    HENRY_PER_CENTIMETER("H/cm", 100.0);

    companion object {
        /**
         * The base unit of the permeability group: [HENRY_PER_METER]. All internal values of
         * [KPermeabilityUnitInstance] are normalized to this unit.
         */
        val BASE: KPermeabilityUnit = HENRY_PER_METER

        /**
         * The **vacuum permeability** `μ₀` (magnetic constant) in henries per meter,
         * 1.256 637 061 27 × 10⁻⁶ H/m (CODATA 2022).
         */
        const val VACUUM_PERMEABILITY: Double = 1.25663706127e-6
    }
}
