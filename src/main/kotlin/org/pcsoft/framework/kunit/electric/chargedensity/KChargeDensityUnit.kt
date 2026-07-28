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

package org.pcsoft.framework.kunit.electric.chargedensity

import org.pcsoft.framework.kunit.KUnit

/**
 * The base-unit marker of the **charge density** group (a *constructed* quantity:
 * `current¹ · time¹ · length⁻³`, i.e. `C/m³`).
 *
 * Charge density has no genuinely single-named unit: every spelling is a ratio (C/m³, mC/cm³, …) and is
 * therefore built as an expression (`coulombs / (meters pow 3)`) or produced by the typed
 * `charge / volume` operator - never as a dedicated token. This enum consequently carries only the base
 * marker [COULOMB_PER_CUBIC_METER], used for the group's display symbol.
 */
enum class KChargeDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Coulomb per cubic meter ("C/m³"), the SI coherent unit of charge density; [baseValue] = 1.0 by definition. */
    COULOMB_PER_CUBIC_METER("C/m³", 1.0);

    companion object {
        /**
         * The base unit of the charge density group: [COULOMB_PER_CUBIC_METER]. All internal values of
         * [KChargeDensityUnitInstance] are normalized to this unit.
         */
        val BASE: KChargeDensityUnit = COULOMB_PER_CUBIC_METER
    }
}
