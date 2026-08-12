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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of volumetric flow rate (a *constructed* quantity: distance³ · time⁻¹).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], cubic meter per second):
 * `1 unit = baseValue * m³/s`.
 *
 * Unlike energy or power, volumetric flow has **no** mass dimension, so its [KVolumeFlowUnitInstance.value]
 * is directly the reading in cubic meters per second - no gram/kilogram bridge is involved.
 *
 * Example:
 * ```kotlin
 * KVolumeFlowUnit.LITER_PER_SECOND.baseValue // 0.001 (1 l/s = 0.001 m³/s)
 * ```
 */
enum class KVolumeFlowUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Cubic meter per second ("m³/s"), the SI base unit of volumetric flow; [baseValue] = 1.0 by definition. */
    CUBIC_METER_PER_SECOND("m³/s", 1.0),

    /** Cubic meter per hour ("m³/h"), 1 m³/h = 1/3600 m³/s (the common HVAC unit). */
    CUBIC_METER_PER_HOUR("m³/h", 1.0 / 3600.0),

    /** Liter per second ("l/s"), 1 l/s = 0.001 m³/s. */
    LITER_PER_SECOND("l/s", 1.0e-3),

    /** Liter per minute ("l/min"), 1 l/min = 0.001/60 m³/s. */
    LITER_PER_MINUTE("l/min", 1.0e-3 / 60.0),

    /** US gallon per minute ("gpm"), 1 gpm = 0.003785411784/60 m³/s. */
    US_GALLON_PER_MINUTE("gpm", 0.003785411784 / 60.0);

    companion object {
        /**
         * The base unit of the volumetric flow group: [CUBIC_METER_PER_SECOND]. All internal values of
         * [KVolumeFlowUnitInstance] are normalized to cubic meters per second.
         */
        val BASE: KVolumeFlowUnit = CUBIC_METER_PER_SECOND
    }
}
