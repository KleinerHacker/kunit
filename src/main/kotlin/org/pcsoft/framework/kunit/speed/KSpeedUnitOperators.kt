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

import org.pcsoft.framework.kunit.distance.KDistanceUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.toLength
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.toTime

// Cross-group operators that let the core units (length, time) combine *directly* into a strongly
// typed speed, and back, without the caller ever handling a raw KMixedUnitInstance. They live in the
// speed package because it may depend on distance/time (the reverse must never happen), and each simply
// delegates to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.
//
// Because a length is now its own type ([KLengthUnitInstance], exponent 1), `length / time = speed` is
// compile-time safe - the operand can no longer be an area or volume. The general
// [KDistanceUnitInstance] fallbacks below cover the rare case of a distance whose exponent is only
// known at runtime (e.g. from `toDistance()`); they throw if it is not actually a length.

/**
 * Divides a length by a time to obtain a [KSpeedUnitInstance] (`length / time = speed`). Since the
 * receiver is a [KLengthUnitInstance] (exponent 1 by construction), this is compile-time safe.
 *
 * Example:
 * ```kotlin
 * val v = 100.meters / 10.seconds            // KSpeedUnitInstance, 10 m/s
 * v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)  // 36.0
 * ```
 */
operator fun KLengthUnitInstance.div(other: KTimeUnitInstance): KSpeedUnitInstance =
    (this.toUnit() / other.toUnit()).toSpeed()

/**
 * Multiplies a speed by a time to obtain the travelled length (`speed * time = length`).
 *
 * Example:
 * ```kotlin
 * val v = 10.metersPerSecond
 * (v * 60.seconds).valueAs(KDistanceUnit.METER) // 600.0
 * ```
 */
operator fun KSpeedUnitInstance.times(other: KTimeUnitInstance): KLengthUnitInstance =
    (this.toUnit() * other.toUnit()).toLength()

/**
 * Multiplies a time by a speed to obtain the travelled length (`time * speed = length`); the
 * commutative counterpart of [KSpeedUnitInstance.times].
 *
 * Example:
 * ```kotlin
 * (60.seconds * 10.metersPerSecond).valueAs(KDistanceUnit.METER) // 600.0
 * ```
 */
operator fun KTimeUnitInstance.times(other: KSpeedUnitInstance): KLengthUnitInstance =
    (this.toUnit() * other.toUnit()).toLength()

/**
 * Divides a length by a speed to obtain the required time (`length / speed = time`).
 *
 * Example:
 * ```kotlin
 * val v = 10.metersPerSecond
 * (600.meters / v).valueAs(KTimeUnit.SECOND) // 60.0
 * ```
 */
operator fun KLengthUnitInstance.div(other: KSpeedUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()

// --- General fallbacks for a distance whose exponent is only known at runtime -------------------

/**
 * `distance / time` fallback for a general [KDistanceUnitInstance]. Only a pure length (exponent 1)
 * yields a speed; anything else (area, volume, ...) throws.
 *
 * @throws IllegalStateException if the left operand is not a pure length (exponent 1).
 */
operator fun KDistanceUnitInstance.div(other: KTimeUnitInstance): KSpeedUnitInstance =
    (this.toUnit() / other.toUnit()).toSpeed()

/**
 * `distance / speed` fallback for a general [KDistanceUnitInstance]. Only a pure length (exponent 1)
 * yields a time; anything else throws.
 *
 * @throws IllegalStateException if the left operand is not a pure length (exponent 1).
 */
operator fun KDistanceUnitInstance.div(other: KSpeedUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
