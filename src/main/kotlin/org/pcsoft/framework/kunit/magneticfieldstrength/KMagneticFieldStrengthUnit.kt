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

package org.pcsoft.framework.kunit.magneticfieldstrength

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **magnetic field strength** (a *constructed* quantity:
 * `current¹ · distance⁻¹`, i.e. `A/m`). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], ampere per meter): `1 unit = baseValue * A/m`.
 *
 * Unlike length or mass, magnetic field strength is not a "real" single-group unit - it is composed of a
 * current and a length term. Each [KMagneticFieldStrengthUnit] therefore carries a single, pre-computed
 * factor to amperes per meter (e.g. `1 Oe = 1000/4π A/m`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KMagneticFieldStrengthUnit.OERSTED.baseValue // 79.57747154594767 (1 Oe = 1000/4π A/m)
 * ```
 */
enum class KMagneticFieldStrengthUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ampere per meter ("A/m"): the SI base unit of the magnetic field strength group ([baseValue] `1.0`). */
    AMPERE_PER_METER("A/m", 1.0),

    /** Oersted ("Oe"), the field strength unit of the CGS electromagnetic system, 1 Oe = 1000/4π A/m. */
    OERSTED("Oe", 79.57747154594767),

    /** Gilbert per centimeter ("Gb/cm"), the CGS magnetomotive-force-per-length form, numerically the oersted. */
    GILBERT_PER_CENTIMETER("Gb/cm", 79.57747154594767),

    /** Ampere-turn per inch ("At/in"), the imperial form, 1 At/in = 39.37007874015748 A/m. */
    AMPERE_TURN_PER_INCH("At/in", 39.37007874015748);

    companion object {
        /**
         * The base unit of the magnetic field strength group: [AMPERE_PER_METER]. All internal values of
         * [KMagneticFieldStrengthUnitInstance] are normalized to this unit.
         */
        val BASE: KMagneticFieldStrengthUnit = AMPERE_PER_METER
    }
}
