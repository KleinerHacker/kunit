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

package org.pcsoft.framework.kunit.permittivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **permittivity** (a *constructed* quantity:
 * `mass⁻¹ · distance⁻³ · time⁴ · current²`, i.e. `kg⁻¹·m⁻³·s⁴·A²` = `F/m`). [baseValue] is the factor to
 * convert into the group's base unit ([BASE], farad per meter): `1 unit = baseValue * F/m`.
 *
 * The permittivity `ε` is the electric constant of a material: it links the electric flux density to the
 * field strength (`ε = D / E`) and the capacitance to the geometry (`ε = C · d / A`). Unlike length or mass
 * it is not a "real" single-group unit - it is composed of a mass, a distance, a time and a current term.
 * Each [KPermittivityUnit] therefore carries a single, pre-computed factor to farads per meter, so it can
 * still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KPermittivityUnit.FARAD_PER_CENTIMETER.baseValue // 100.0 (1 F/cm = 100 F/m)
 * ```
 */
enum class KPermittivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Farad per meter ("F/m"): the SI base unit of the permittivity group ([baseValue] `1.0`). */
    FARAD_PER_METER("F/m", 1.0),

    /** Farad per centimeter ("F/cm"), the common laboratory notation, 1 F/cm = 100 F/m. */
    FARAD_PER_CENTIMETER("F/cm", 100.0);

    companion object {
        /**
         * The base unit of the permittivity group: [FARAD_PER_METER]. All internal values of
         * [KPermittivityUnitInstance] are normalized to this unit.
         */
        val BASE: KPermittivityUnit = FARAD_PER_METER

        /**
         * The **vacuum permittivity** `ε₀` (electric constant) in farads per meter,
         * 8.854 187 8188 × 10⁻¹² F/m (CODATA 2022).
         */
        const val VACUUM_PERMITTIVITY: Double = 8.8541878188e-12
    }
}
