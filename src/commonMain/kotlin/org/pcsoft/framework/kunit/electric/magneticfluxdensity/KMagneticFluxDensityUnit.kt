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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **magnetic flux density** (a *constructed* quantity: `mass¹ · time⁻² ·
 * current⁻¹`, i.e. `kg·s⁻²·A⁻¹`). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], tesla): `1 unit = baseValue * T`.
 *
 * Magnetic flux density is not a "real" single-group unit - it is composed of a mass, a time and a current
 * term. Each [KMagneticFluxDensityUnit] therefore carries a single, pre-computed factor to teslas (e.g.
 * `1 G = 1e-4 T`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KMagneticFluxDensityUnit.GAUSS.baseValue // 1.0e-4 (1 G = 0.0001 T)
 * ```
 */
enum class KMagneticFluxDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Tesla ("T"): the SI base unit of the magnetic flux density group ([baseValue] `1.0`). */
    TESLA("T", 1.0),

    /** Weber per square meter ("Wb/m²"), the explicit `Wb/m²` spelling of the tesla, 1 Wb/m² = 1 T. */
    WEBER_PER_SQUARE_METER("Wb/m²", 1.0),

    /** Gauss ("G"), the flux density unit of the CGS electromagnetic system (EMU), 1 G = 1e-4 T. */
    GAUSS("G", 1e-4),

    /** Gamma ("γ"), the geophysical sub-unit of the gauss, 1 γ = 1e-9 T (= 1 nT). */
    GAMMA("γ", 1e-9);

    companion object {
        /**
         * The base unit of the magnetic flux density group: [TESLA]. All internal values of
         * [KMagneticFluxDensityUnitInstance] are normalized to this unit.
         */
        val BASE: KMagneticFluxDensityUnit = TESLA
    }
}
