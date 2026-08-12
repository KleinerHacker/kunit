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

package org.pcsoft.framework.kunit.mechanic.angularacceleration

import kotlin.math.PI
import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of angular acceleration (a *constructed* quantity: angle · time⁻²).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], radian per second squared):
 * `1 unit = baseValue * rad/s²`.
 *
 * Example:
 * ```kotlin
 * KAngularAccelerationUnit.DEGREES_PER_SECOND_SQUARED.baseValue // ≈ 0.017453 (1 °/s² = π/180 rad/s²)
 * ```
 */
enum class KAngularAccelerationUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Radian per second squared, the SI base unit of angular acceleration; [baseValue] = 1.0 by definition. */
    RADIANS_PER_SECOND_SQUARED("rad/s^2", 1.0),

    /** Degree per second squared, 1 °/s² = π/180 rad/s². */
    DEGREES_PER_SECOND_SQUARED("°/s^2", PI / 180.0),

    /** Revolution per second squared, 1 rps² = 2π rad/s². */
    REVOLUTIONS_PER_SECOND_SQUARED("rps^2", 2.0 * PI),

    /** Revolution per minute per second ("rpm/s"), 1 rpm/s = 2π/60 rad/s² (run-up rate of machines). */
    REVOLUTIONS_PER_MINUTE_PER_SECOND("rpm/s", 2.0 * PI / 60.0);

    companion object {
        /**
         * The base unit of the angular-acceleration group: [RADIANS_PER_SECOND_SQUARED]. All internal
         * values of [KAngularAccelerationUnitInstance] are normalized to this unit.
         */
        val BASE: KAngularAccelerationUnit = RADIANS_PER_SECOND_SQUARED
    }
}
