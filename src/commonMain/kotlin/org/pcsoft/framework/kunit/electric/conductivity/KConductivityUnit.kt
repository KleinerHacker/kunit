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

package org.pcsoft.framework.kunit.electric.conductivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electrical conductivity** (a *constructed* quantity:
 * `mass⁻¹ · distance⁻³ · time³ · current²`, i.e. `kg⁻¹·m⁻³·s³·A²`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], siemens per meter): `1 unit = baseValue * S/m`.
 *
 * Conductivity is the reciprocal of electrical resistivity (`σ = 1 / ρ`) and describes the material
 * property behind a conductance. Unlike length or mass it is not a "real" single-group unit - it is
 * composed of a mass, a length, a time and a current term. Each [KConductivityUnit] therefore carries a
 * single, pre-computed factor to siemens per meter (e.g. `1 S/cm = 100 S/m`), so it can still be used as a
 * plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KConductivityUnit.SIEMENS_PER_CENTIMETER.baseValue // 100.0 (1 S/cm = 100 S/m)
 * ```
 */
enum class KConductivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Siemens per meter ("S/m"): the SI base unit of the conductivity group ([baseValue] `1.0`). */
    SIEMENS_PER_METER("S/m", 1.0),

    /** Siemens per centimeter ("S/cm"), common in electrochemistry, 1 S/cm = 100 S/m. */
    SIEMENS_PER_CENTIMETER("S/cm", 100.0),

    /** Microsiemens per centimeter ("µS/cm"), the standard unit of water conductivity, 1 µS/cm = 1e-4 S/m. */
    MICROSIEMENS_PER_CENTIMETER("µS/cm", 1e-4),

    /** Megasiemens per meter ("MS/m"), used for metals (copper ≈ 58 MS/m), 1 MS/m = 10⁶ S/m. */
    MEGASIEMENS_PER_METER("MS/m", 1e6);

    companion object {
        /**
         * The base unit of the conductivity group: [SIEMENS_PER_METER]. All internal values of
         * [KConductivityUnitInstance] are normalized to this unit.
         */
        val BASE: KConductivityUnit = SIEMENS_PER_METER
    }
}
