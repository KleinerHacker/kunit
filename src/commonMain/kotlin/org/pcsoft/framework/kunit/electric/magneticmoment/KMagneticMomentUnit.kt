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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **magnetic dipole moment** (current times enclosed area; a *constructed*
 * quantity: current · length²). [baseValue] is the factor to convert into the group's base unit ([BASE],
 * ampere square meter): `1 unit = baseValue * A·m²`.
 *
 * Example:
 * ```kotlin
 * KMagneticMomentUnit.BOHR_MAGNETON.baseValue // 9.2740100783e-24
 * ```
 */
enum class KMagneticMomentUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ampere square meter ("A*m^2"), the coherent SI unit of magnetic moment; [baseValue] = 1.0. */
    AMPERE_SQUARE_METER("A*m^2", 1.0),

    /**
     * Joule per tesla ("J/T"), the energy-based spelling of the same quantity - the energy a dipole gains
     * per unit of magnetic flux density. Numerically identical to [AMPERE_SQUARE_METER].
     */
    JOULE_PER_TESLA("J/T", 1.0),

    /**
     * Bohr magneton ("μB"), the natural atomic unit of magnetic moment:
     * 9.274 010 0783 × 10⁻²⁴ A·m².
     */
    BOHR_MAGNETON("μB", 9.2740100783e-24),

    /**
     * Nuclear magneton ("μN"), the nuclear counterpart, smaller by the proton-to-electron mass ratio:
     * 5.050 783 7461 × 10⁻²⁷ A·m².
     */
    NUCLEAR_MAGNETON("μN", 5.0507837461e-27);

    companion object {
        /**
         * The base unit of the magnetic-moment group: [AMPERE_SQUARE_METER]. All internal values of
         * [KMagneticMomentUnitInstance] are normalized to ampere square meters.
         */
        val BASE: KMagneticMomentUnit = AMPERE_SQUARE_METER
    }
}
