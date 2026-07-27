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

package org.pcsoft.framework.kunit.currentdensity

import org.pcsoft.framework.kunit.KUnit

/**
 * The base-unit marker of the **current density** group - a *constructed* quantity: `current · length⁻²`
 * (`A/m²`), the electric current per conductor cross-section.
 *
 * Like density or area density it has no genuinely single-named unit: every spelling is a ratio (A/m²,
 * A/mm², …) and is therefore built as an expression (`amperes / (milli.meters pow 2)`) or produced by the
 * typed `current / area` operator - never as a dedicated token. This enum consequently carries only the
 * base marker [AMPERE_PER_SQUARE_METER], used for the group's display symbol.
 *
 * Example:
 * ```kotlin
 * KCurrentDensityUnit.BASE.symbol // "A/m²"
 * ```
 */
enum class KCurrentDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Ampere per square meter ("A/m²"), the coherent SI unit of current density; [baseValue] = 1.0 by definition. */
    AMPERE_PER_SQUARE_METER("A/m²", 1.0);

    companion object {
        /**
         * The base unit of the current-density group: [AMPERE_PER_SQUARE_METER]. All internal values of
         * [KCurrentDensityUnitInstance] are normalized to this unit.
         */
        val BASE: KCurrentDensityUnit = AMPERE_PER_SQUARE_METER
    }
}
