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

package org.pcsoft.framework.kunit.density

import org.pcsoft.framework.kunit.KUnit

/**
 * The base-unit marker of the (mass-)density group (a *constructed* quantity: mass · length⁻³).
 *
 * Density has no genuinely single-named unit: every spelling is a ratio (kg/m³, g/cm³, …) and is
 * therefore built as an expression (`kilo.grams / (meters pow 3)`) or produced by the typed
 * `mass / volume` operator - never as a dedicated token. This enum consequently carries only the base
 * marker [KILOGRAM_PER_CUBIC_METER], used for the group's display symbol.
 *
 * Because the mass component of this library is normalized to **grams**, the kg/m³ base is 1000× the raw
 * component base (`g·m⁻³`); that fixed factor is [org.pcsoft.framework.kunit.density.KGM3_IN_BASE].
 */
enum class KDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram per cubic meter ("kg/m³"), the SI coherent unit of density; [baseValue] = 1.0 by definition. */
    KILOGRAM_PER_CUBIC_METER("kg/m³", 1.0);

    companion object {
        /** The base unit of the density group: [KILOGRAM_PER_CUBIC_METER]. */
        val BASE: KDensityUnit = KILOGRAM_PER_CUBIC_METER
    }
}
