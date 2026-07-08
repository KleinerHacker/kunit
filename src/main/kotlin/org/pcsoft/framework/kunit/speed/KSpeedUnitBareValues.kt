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

// A speed is built as a length-per-time expression, e.g. `10 of kilo.meters / hours` or
// `100 of meters / (10 of seconds)`. There are therefore deliberately **no** spelled-out composite tokens
// (`metersPerSecond`, `kilometersPerHour`, `milesPerHour`, `feetPerSecond` = plain `meters / seconds`,
// `kilo.meters / hours`, … and thus redundant).
//
// Only speeds with a genuinely single, conventional name and their own factor survive as value-1 tokens,
// used with `of`/`into` (`50 of knots`, `v into mach`).

/** 1 knot ([KSpeedUnit.KNOT], 1 nautical mile per hour). */
val knots: KSpeedUnitInstance = speedUnitInstanceOf(KSpeedUnit.KNOT.baseValue)

/** 1 Mach ([KSpeedUnit.MACH], ISA sea-level speed of sound, 340.29 m/s). */
val mach: KSpeedUnitInstance = speedUnitInstanceOf(KSpeedUnit.MACH.baseValue)

/** 1 c, the speed of light ([KSpeedUnit.LIGHT_SPEED], 299 792 458 m/s). */
val speedOfLight: KSpeedUnitInstance = speedUnitInstanceOf(KSpeedUnit.LIGHT_SPEED.baseValue)
