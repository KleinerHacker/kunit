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

package org.pcsoft.framework.kunit.speed

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of speed (a *constructed* quantity: length · time⁻¹). [baseValue] is the
 * factor to convert into the group's base unit ([BASE], meter per second): `1 unit = baseValue * m/s`.
 *
 * Unlike length or time, speed is not a "real" single-group unit - it is composed of a length and a
 * time term (`m·s⁻¹`). Each [KSpeedUnit] therefore carries a single, pre-computed factor to m/s (e.g.
 * `1 km/h = 1000 m / 3600 s ≈ 0.2778 m/s`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KSpeedUnit.KILOMETERS_PER_HOUR.baseValue // 0.2777... (1 km/h = 1000/3600 m/s)
 * ```
 */
enum class KSpeedUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Meter per second, the SI base unit of speed; [baseValue] = 1.0 by definition. */
    METERS_PER_SECOND("m/s", 1.0),

    /** Kilometer per hour, 1 km/h = 1000 m / 3600 s ≈ 0.277778 m/s (everyday metric road speed). */
    KILOMETERS_PER_HOUR("km/h", 1000.0 / 3600.0),

    /** Mile per hour, 1 mph = 1609.344 m / 3600 s ≈ 0.44704 m/s (imperial road speed). */
    MILES_PER_HOUR("mph", 1609.344 / 3600.0),

    /** Knot (nautical mile per hour), 1 kn = 1852 m / 3600 s ≈ 0.514444 m/s (maritime/aviation). */
    KNOT("kn", 1852.0 / 3600.0),

    /** Foot per second, 1 ft/s = 0.3048 m/s. */
    FEET_PER_SECOND("ft/s", 0.3048),

    /**
     * Mach, the speed of sound at ISA sea level (15 °C): 1 Ma = 340.29 m/s. A convenience reference
     * point for aeronautics; the value is the standard-atmosphere speed of sound, not a physical
     * constant (the real speed of sound varies with temperature and altitude).
     */
    MACH("Ma", 340.29),

    /** Speed of light in vacuum, 1 c = 299 792 458 m/s (the physical constant *c*). */
    LIGHT_SPEED("c", 299792458.0);

    companion object {
        /**
         * The base unit of the speed group; all internal values of [KSpeedUnitInstance] are normalized
         * to this unit (meter per second).
         */
        val BASE: KSpeedUnit = METERS_PER_SECOND
    }
}
