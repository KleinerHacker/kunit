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

package org.pcsoft.framework.kunit.electric.conductance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **electrical conductance** (a *constructed* quantity:
 * `mass⁻¹ · distance⁻² · time³ · current²`, i.e. `kg⁻¹·m⁻²·s³·A²`). [baseValue] is the factor to convert
 * into the group's base unit ([BASE], siemens): `1 unit = baseValue * S`.
 *
 * Conductance is the reciprocal of electrical resistance (`G = 1 / R`). Unlike length or mass it is not a
 * "real" single-group unit - it is composed of a mass, a length, a time and a current term. Each
 * [KConductanceUnit] therefore carries a single, pre-computed factor to siemens (e.g. `1 ab℧ = 10⁹ S`), so
 * it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KConductanceUnit.ABMHO.baseValue // 1.0E9 (1 ab℧ = 10⁹ S)
 * ```
 */
enum class KConductanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Siemens ("S"): the SI base unit of the conductance group ([baseValue] `1.0`). */
    SIEMENS("S", 1.0),

    /** Mho ("℧"), the traditional (pre-SI) name of the siemens, 1 ℧ = 1 S. */
    MHO("℧", 1.0),

    /** Abmho ("ab℧"), the conductance unit of the CGS electromagnetic system (EMU), 1 ab℧ = 10⁹ S. */
    ABMHO("ab℧", 1e9),

    /** Statmho ("stat℧"), the conductance unit of the CGS electrostatic system (ESU), 1 stat℧ = 1.112650e-12 S. */
    STATMHO("stat℧", 1.112650e-12);

    companion object {
        /**
         * The base unit of the conductance group: [SIEMENS]. All internal values of
         * [KConductanceUnitInstance] are normalized to this unit.
         */
        val BASE: KConductanceUnit = SIEMENS
    }
}
