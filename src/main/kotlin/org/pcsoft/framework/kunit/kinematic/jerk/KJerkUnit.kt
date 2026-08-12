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

package org.pcsoft.framework.kunit.kinematic.jerk

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **jerk** (rate of change of acceleration; a *constructed* quantity:
 * length · time⁻³). [baseValue] is the factor to convert into the group's base unit ([BASE], meter per
 * second cubed): `1 unit = baseValue * m/s³`.
 *
 * Example:
 * ```kotlin
 * KJerkUnit.STANDARD_GRAVITY_PER_SECOND.baseValue // 9.80665 (1 g/s in m/s³)
 * ```
 */
enum class KJerkUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Meter per second cubed ("m/s^3"), the coherent SI unit of jerk; [baseValue] = 1.0. */
    METER_PER_SECOND_CUBED("m/s^3", 1.0),

    /**
     * Standard gravity per second ("g/s"), the ride-comfort spelling: how fast the acceleration changes,
     * measured in multiples of g per second. 1 g/s = 9.80665 m/s³.
     */
    STANDARD_GRAVITY_PER_SECOND("g/s", 9.80665),

    /** Foot per second cubed ("ft/s^3"), the imperial counterpart; 1 ft/s³ = 0.3048 m/s³. */
    FOOT_PER_SECOND_CUBED("ft/s^3", 0.3048);

    companion object {
        /**
         * The base unit of the jerk group: [METER_PER_SECOND_CUBED]. All internal values of
         * [KJerkUnitInstance] are normalized to meters per second cubed.
         */
        val BASE: KJerkUnit = METER_PER_SECOND_CUBED
    }
}
