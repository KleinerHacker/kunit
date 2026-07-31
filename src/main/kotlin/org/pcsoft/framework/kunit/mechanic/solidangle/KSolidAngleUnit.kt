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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of solid angle (the two-dimensional angle, `rad²`). [baseValue] is the factor
 * to convert into the group's base unit ([BASE], steradian): `1 unit = baseValue * sr`.
 *
 * Example:
 * ```kotlin
 * KSolidAngleUnit.SPAT.baseValue // ≈ 12.566 (a full sphere = 4π sr)
 * ```
 */
enum class KSolidAngleUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Steradian ("sr"), the SI unit of solid angle; [baseValue] = 1.0 by definition (1 sr = 1 rad²). */
    STERADIAN("sr", 1.0),

    /** Square degree ("deg²"), 1 deg² = (π/180)² sr ≈ 3.0462e-4 sr. */
    SQUARE_DEGREE("deg²", (Math.PI / 180.0) * (Math.PI / 180.0)),

    /** Spat ("sp"), the solid angle of a full sphere: 1 sp = 4π sr. */
    SPAT("sp", 4.0 * Math.PI);

    companion object {
        /**
         * The base unit of the solid-angle group: [STERADIAN]. All internal values of
         * [KSolidAngleUnitInstance] are normalized to steradians.
         */
        val BASE: KSolidAngleUnit = STERADIAN
    }
}
