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

package org.pcsoft.framework.kunit.energy

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **energy** (a *constructed* quantity: `mass¹ · distance² · time⁻²`, i.e.
 * `kg·m²·s⁻²`). [baseValue] is the factor to convert into the group's base unit ([BASE], joule):
 * `1 unit = baseValue * J`.
 *
 * Energy is the shared quantity of several subject areas (electrical `W = Q · U`, mechanical work
 * `W = F · s`, thermodynamic heat); one Kotlin group serves all of them. Unlike length or mass it is not a
 * "real" single-group unit - it is composed of a mass, a distance and a time term. Each [KEnergyUnit]
 * therefore carries a single, pre-computed factor to joules (e.g. `1 cal = 4.184 J`), so it can still be
 * used as a plain [KUnit]/target.
 *
 * The kilowatt hour has deliberately *no* token of its own: it is not a genuinely named unit but the
 * product `kilo.watts * hours` and is built that way.
 *
 * Example:
 * ```kotlin
 * KEnergyUnit.CALORIE.baseValue // 4.184 (1 cal = 4.184 J)
 * ```
 */
enum class KEnergyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Joule ("J"): the SI base unit of the energy group ([baseValue] `1.0`). */
    JOULE("J", 1.0),

    /** Erg ("erg"), the energy unit of the CGS system, 1 erg = 1e-7 J. */
    ERG("erg", 1.0e-7),

    /** Thermochemical calorie ("cal"), 1 cal = 4.184 J. */
    CALORIE("cal", 4.184),

    /** Electron volt ("eV"), the energy of one elementary charge at 1 V, 1 eV = 1.602176634e-19 J. */
    ELECTRON_VOLT("eV", 1.602176634e-19),

    /** British thermal unit ("BTU"), the ISO thermal unit, 1 BTU = 1055.05585262 J. */
    BRITISH_THERMAL_UNIT("BTU", 1055.05585262);

    companion object {
        /**
         * The base unit of the energy group: [JOULE]. All internal values of [KEnergyUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KEnergyUnit = JOULE
    }
}
