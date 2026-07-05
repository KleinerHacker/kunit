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

// Speed creator extension properties (bare unit references live in `KSpeedUnitBareValues.kt`).

private fun of(value: Number, unit: KSpeedUnit): KSpeedUnitInstance = speedUnitInstanceOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure speed value in meters per second from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 10.metersPerSecond.value   // 10.0
 * 10L.metersPerSecond.value  // 10.0
 * 10.0f.metersPerSecond.value // 10.0
 * ```
 */
val Number.metersPerSecond: KSpeedUnitInstance get() = of(this, KSpeedUnit.METERS_PER_SECOND)

/** Creates a pure speed value in kilometers per hour. Example: `36.kilometersPerHour.value // 10.0` (normalized to m/s). */
val Number.kilometersPerHour: KSpeedUnitInstance get() = of(this, KSpeedUnit.KILOMETERS_PER_HOUR)

/** Creates a pure speed value in miles per hour. Example: `1.milesPerHour.value // 0.44704` (normalized to m/s). */
val Number.milesPerHour: KSpeedUnitInstance get() = of(this, KSpeedUnit.MILES_PER_HOUR)

/** Creates a pure speed value in knots. Example: `1.knots.value // 0.514444...` (normalized to m/s). */
val Number.knots: KSpeedUnitInstance get() = of(this, KSpeedUnit.KNOT)

/** Creates a pure speed value in feet per second. Example: `1.feetPerSecond.value // 0.3048` (normalized to m/s). */
val Number.feetPerSecond: KSpeedUnitInstance get() = of(this, KSpeedUnit.FEET_PER_SECOND)

/** Creates a pure speed value in Mach (ISA sea-level speed of sound). Example: `1.mach.value // 340.29` (normalized to m/s). */
val Number.mach: KSpeedUnitInstance get() = of(this, KSpeedUnit.MACH)

/** Creates a pure speed value as a multiple of the speed of light. Example: `1.speedOfLight.value // 299792458.0`. */
val Number.speedOfLight: KSpeedUnitInstance get() = of(this, KSpeedUnit.LIGHT_SPEED)
