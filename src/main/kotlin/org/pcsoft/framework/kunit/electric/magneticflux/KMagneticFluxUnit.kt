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

package org.pcsoft.framework.kunit.electric.magneticflux

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **magnetic flux** (a *constructed* quantity:
 * `mass¹ · distance² · time⁻² · current⁻¹`, i.e. `kg·m²·s⁻²·A⁻¹`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], weber): `1 unit = baseValue * Wb`.
 *
 * Unlike length or mass, magnetic flux is not a "real" single-group unit - it is composed of a mass, a
 * distance, a time and a current term. Each [KMagneticFluxUnit] therefore carries a single, pre-computed
 * factor to webers (e.g. `1 Mx = 1e-8 Wb`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KMagneticFluxUnit.MAXWELL.baseValue // 1.0e-8 (1 Mx = 1e-8 Wb)
 * ```
 */
enum class KMagneticFluxUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Weber ("Wb"): the SI base unit of the magnetic flux group ([baseValue] `1.0`). */
    WEBER("Wb", 1.0),

    /** Maxwell ("Mx"), the flux unit of the CGS electromagnetic system (EMU), 1 Mx = 1e-8 Wb. */
    MAXWELL("Mx", 1.0e-8),

    /** Unit pole ("pole"), the historic flux of a CGS unit magnetic pole, 1 pole = 4π·1e-8 Wb. */
    UNIT_POLE("pole", 1.2566370614359173e-7);

    companion object {
        /**
         * The base unit of the magnetic flux group: [WEBER]. All internal values of
         * [KMagneticFluxUnitInstance] are normalized to this unit.
         */
        val BASE: KMagneticFluxUnit = WEBER
    }
}
