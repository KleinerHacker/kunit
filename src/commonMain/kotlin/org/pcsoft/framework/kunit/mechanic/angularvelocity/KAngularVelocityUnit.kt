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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

import kotlin.math.PI
import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of angular velocity (a *constructed* quantity: angle · time⁻¹). [baseValue]
 * is the factor to convert into the group's base unit ([BASE], radian per second):
 * `1 unit = baseValue * rad/s`.
 *
 * Like speed, angular velocity is not a single-group unit - it is composed of an angle and a time term
 * (`rad·s⁻¹`). Each entry therefore carries a single, pre-computed factor to rad/s (e.g.
 * `1 rpm = 2π/60 rad/s`).
 *
 * Example:
 * ```kotlin
 * KAngularVelocityUnit.REVOLUTIONS_PER_MINUTE.baseValue // ≈ 0.10472 (1 rpm = 2π/60 rad/s)
 * ```
 */
enum class KAngularVelocityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Radian per second, the SI base unit of angular velocity; [baseValue] = 1.0 by definition. */
    RADIANS_PER_SECOND("rad/s", 1.0),

    /** Degree per second, 1 °/s = π/180 rad/s. */
    DEGREES_PER_SECOND("°/s", PI / 180.0),

    /** Revolution per minute ("rpm"), 1 rpm = 2π/60 rad/s (the everyday machine-speed unit). */
    REVOLUTIONS_PER_MINUTE("rpm", 2.0 * PI / 60.0),

    /** Revolution per second ("rps"), 1 rps = 2π rad/s. */
    REVOLUTIONS_PER_SECOND("rps", 2.0 * PI);

    companion object {
        /**
         * The base unit of the angular-velocity group: [RADIANS_PER_SECOND]. All internal values of
         * [KAngularVelocityUnitInstance] are normalized to this unit.
         */
        val BASE: KAngularVelocityUnit = RADIANS_PER_SECOND
    }
}
