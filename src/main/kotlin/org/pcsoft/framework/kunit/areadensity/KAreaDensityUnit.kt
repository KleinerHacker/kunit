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

package org.pcsoft.framework.kunit.areadensity

import org.pcsoft.framework.kunit.KUnit

/**
 * The base-unit marker of the area-density (areal density / surface mass) group - a *constructed*
 * quantity: mass · length⁻², e.g. the surface load in construction statics.
 *
 * Like density, it has no genuinely single-named unit: every spelling is a ratio (kg/m², g/mm², …) and
 * is therefore built as an expression (`kilo.grams / (meters pow 2)`) or produced by the typed
 * `mass / area` operator - never as a dedicated token. This enum consequently carries only the base
 * marker [KILOGRAM_PER_SQUARE_METER], used for the group's display symbol.
 *
 * Because the mass component of this library is normalized to **grams**, the kg/m² base is 1000× the raw
 * component base (`g·m⁻²`); that fixed factor is
 * [org.pcsoft.framework.kunit.areadensity.KGM2_IN_BASE].
 */
enum class KAreaDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram per square meter ("kg/m²"), the coherent SI unit of areal density; [baseValue] = 1.0 by definition. */
    KILOGRAM_PER_SQUARE_METER("kg/m²", 1.0);

    companion object {
        /** The base unit of the area-density group: [KILOGRAM_PER_SQUARE_METER]. */
        val BASE: KAreaDensityUnit = KILOGRAM_PER_SQUARE_METER
    }
}
