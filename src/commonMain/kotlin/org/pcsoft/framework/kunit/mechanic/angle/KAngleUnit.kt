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

package org.pcsoft.framework.kunit.mechanic.angle

import kotlin.math.PI
import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of plane angle (a *native*, measurable quantity). [baseValue] is the factor
 * to convert into the group's base unit ([BASE], radian): `1 unit = baseValue * rad`.
 *
 * Example:
 * ```kotlin
 * KAngleUnit.DEGREE.baseValue // ≈ 0.017453 (1° = π/180 rad)
 * ```
 */
enum class KAngleUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Radian ("rad"), the SI unit of plane angle; [baseValue] = 1.0 by definition. */
    RADIAN("rad", 1.0),

    /** Degree ("°"), 1° = π/180 rad. */
    DEGREE("°", PI / 180.0),

    /** Arcminute ("'"), 1' = 1/60 ° = π/10800 rad. */
    ARCMINUTE("'", PI / 10800.0),

    /** Arcsecond ("\""), 1" = 1/3600 ° = π/648000 rad. */
    ARCSECOND("\"", PI / 648000.0),

    /** Gradian / gon ("gon"), a right angle divided into 100 parts: 1 gon = π/200 rad. */
    GRADIAN("gon", PI / 200.0),

    /** Full turn / revolution ("tr"), 1 tr = 2π rad = 360°. */
    TURN("tr", 2.0 * PI);

    companion object {
        /**
         * The base unit of the angle group: [RADIAN]. All internal values of [KAngleUnitInstance] are
         * normalized to radians.
         */
        val BASE: KAngleUnit = RADIAN
    }
}
