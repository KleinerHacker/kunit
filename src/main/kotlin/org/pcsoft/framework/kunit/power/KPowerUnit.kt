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

package org.pcsoft.framework.kunit.power

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **power** (a *constructed* quantity: `mass¹ · distance² · time⁻³`, i.e.
 * `kg·m²·s⁻³`). [baseValue] is the factor to convert into the group's base unit ([BASE], watt):
 * `1 unit = baseValue * W`.
 *
 * Power is the shared quantity of several subject areas (electrical `P = U · I`, mechanics `P = F · v`,
 * thermodynamics as heat flow); one Kotlin group serves all of them. Unlike length or mass it is not a
 * "real" single-group unit - it is composed of a mass, a distance and a time term. Each [KPowerUnit]
 * therefore carries a single, pre-computed factor to watts (e.g. `1 PS = 735.49875 W`), so it can still be
 * used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KPowerUnit.METRIC_HORSE_POWER.baseValue // 735.49875 (1 PS = 735.49875 W)
 * ```
 */
enum class KPowerUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Watt ("W"): the SI base unit of the power group ([baseValue] `1.0`). */
    WATT("W", 1.0),

    /** Metric horsepower ("PS"), the continental European horsepower, 1 PS = 735.49875 W. */
    METRIC_HORSE_POWER("PS", 735.49875),

    /** Mechanical horsepower ("hp"), the imperial horsepower, 1 hp = 745.6998715822702 W. */
    MECHANICAL_HORSE_POWER("hp", 745.6998715822702),

    /** Erg per second ("erg/s"), the power unit of the CGS system, 1 erg/s = 1e-7 W. */
    ERG_PER_SECOND("erg/s", 1.0e-7);

    companion object {
        /**
         * The base unit of the power group: [WATT]. All internal values of [KPowerUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KPowerUnit = WATT
    }
}
