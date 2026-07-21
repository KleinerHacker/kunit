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

package org.pcsoft.framework.kunit.acceleration

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of acceleration (a *constructed* quantity: length · time⁻²). [baseValue] is
 * the factor to convert into the group's base unit ([BASE], meter per second squared):
 * `1 unit = baseValue * m/s²`.
 *
 * Like speed, acceleration is not a "real" single-group unit - it is composed of a length and a time
 * term (`m·s⁻²`). Each [KAccelerationUnit] therefore carries a single, pre-computed factor to m/s², so
 * it can still be used as a plain [KUnit]/target. Since the group's base unit coincides with the
 * component base units (meter, second), there is no extra scaling factor between them (unlike the
 * gram-based force/pressure/density groups).
 *
 * Example:
 * ```kotlin
 * KAccelerationUnit.STANDARD_GRAVITY.baseValue // 9.80665 (1 g₀ = 9.80665 m/s²)
 * ```
 */
enum class KAccelerationUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Meter per second squared, the SI base unit of acceleration; [baseValue] = 1.0 by definition. */
    METERS_PER_SECOND_SQUARED("m/s²", 1.0),

    /** Gal (Galileo), the CGS unit of acceleration, 1 Gal = 1 cm/s² = 0.01 m/s² (used in geodesy/gravimetry). */
    GAL("Gal", 0.01),

    /** Standard gravity, 1 g₀ = 9.806 65 m/s² (the conventional standard acceleration of free fall). */
    STANDARD_GRAVITY("g₀", 9.80665);

    companion object {
        /**
         * The base unit of the acceleration group; all internal values of [KAccelerationUnitInstance]
         * are normalized to this unit (meter per second squared).
         */
        val BASE: KAccelerationUnit = METERS_PER_SECOND_SQUARED
    }
}
