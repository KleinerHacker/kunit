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

// Bare unit references, usable both as a KUnitTarget (e.g. `v.valueAs(kilometersPerHour)`) and as the
// `unit` argument of the speed-group prefix `infix` functions (e.g. `5 kilo metersPerSecond`, see
// `KSpeedUnitPrefix.kt`). Only m/s is an SI unit and therefore sensibly combinable with a prefix, but
// the others are still accepted since KUnitPrefix is a purely mathematical scale factor.

/** Bare reference to [KSpeedUnit.METERS_PER_SECOND], for use with [valueAs][KSpeedUnitInstance.valueAs] or the prefix `infix` functions. */
val metersPerSecond: KSpeedUnit = KSpeedUnit.METERS_PER_SECOND

/** Bare reference to [KSpeedUnit.KILOMETERS_PER_HOUR]. */
val kilometersPerHour: KSpeedUnit = KSpeedUnit.KILOMETERS_PER_HOUR

/** Bare reference to [KSpeedUnit.MILES_PER_HOUR]. */
val milesPerHour: KSpeedUnit = KSpeedUnit.MILES_PER_HOUR

/** Bare reference to [KSpeedUnit.KNOT]. */
val knots: KSpeedUnit = KSpeedUnit.KNOT

/** Bare reference to [KSpeedUnit.FEET_PER_SECOND]. */
val feetPerSecond: KSpeedUnit = KSpeedUnit.FEET_PER_SECOND

/** Bare reference to [KSpeedUnit.MACH]. */
val mach: KSpeedUnit = KSpeedUnit.MACH

/** Bare reference to [KSpeedUnit.LIGHT_SPEED]. */
val speedOfLight: KSpeedUnit = KSpeedUnit.LIGHT_SPEED
