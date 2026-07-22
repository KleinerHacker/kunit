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

package org.pcsoft.framework.kunit.frequency

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.KDistanceUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.toLength
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.toSpeed
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.toTime

// Cross-group operators that make a frequency behave exactly **inverse to time** (`frequency = 1/time`):
// multiplying by a frequency behaves like dividing by a time, and dividing by a frequency like
// multiplying by a time. They live in the frequency package because it may depend on distance/time/speed
// (the reverse must never happen), and each delegates to the generic KMixedUnitInstance `*`/`/` engine
// via the frequency's inverse-time bridge (`toInverseTime`, tagged `[SECOND^-1]`).

// --- Scalar (count) against time / frequency -----------------------------------------------------

/**
 * Divides a scalar count by a time to obtain a typed [KFrequencyUnitInstance] (`count / time = frequency`).
 * This is the direct, strongly typed way to build a frequency from a period (`1 / (2 of seconds)` = 0.5 Hz),
 * and is more specific than the generic reciprocal `Number.div(KUnitMeasurable)`.
 *
 * Example:
 * ```kotlin
 * val f = 60 / (1 of seconds) // KFrequencyUnitInstance, 60 Hz
 * ```
 */
operator fun Number.div(time: KTimeUnitInstance): KFrequencyUnitInstance = frequencyOf(this.toDouble() / time.value)

/**
 * Divides a scalar count by a frequency to obtain the corresponding time (`count / frequency = time`) -
 * the inverse of [Number.div] against a time, e.g. `1 / (0.5 of hertz)` = 2 s (the period).
 *
 * Example:
 * ```kotlin
 * val period = 1 / (2 of hertz) // KTimeUnitInstance, 0.5 s
 * ```
 */
operator fun Number.div(frequency: KFrequencyUnitInstance): KTimeUnitInstance =
    (KMixedUnitInstance(this.toDouble(), emptyList()) / frequency.toInverseTime()).toTime()

/**
 * Multiplies a frequency by a time to obtain the **dimensionless count** of events in that time span
 * (`frequency * time = count`, `Hz · s = 1`).
 *
 * Example:
 * ```kotlin
 * (50 of hertz) * (2 of seconds) // 100.0 (events)
 * ```
 */
operator fun KFrequencyUnitInstance.times(time: KTimeUnitInstance): Double = value * time.value

/**
 * Multiplies a time by a frequency to obtain the dimensionless count (`time * frequency = count`); the
 * commutative counterpart of [KFrequencyUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(frequency: KFrequencyUnitInstance): Double = frequency.value * value

// --- Distance / speed against frequency (inverse of the time operators) --------------------------

/**
 * Multiplies a length by a frequency to obtain a [KSpeedUnitInstance] (`length * frequency = speed`),
 * the inverse-time counterpart of `length / time = speed`.
 *
 * Example:
 * ```kotlin
 * val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s
 * ```
 */
operator fun KLengthUnitInstance.times(frequency: KFrequencyUnitInstance): KSpeedUnitInstance =
    (this.toUnit() * frequency.toInverseTime()).toSpeed()

/**
 * Multiplies a frequency by a length to obtain a speed (`frequency * length = speed`); the commutative
 * counterpart of [KLengthUnitInstance.times].
 */
operator fun KFrequencyUnitInstance.times(length: KLengthUnitInstance): KSpeedUnitInstance =
    (this.toInverseTime() * length.toUnit()).toSpeed()

/**
 * Divides a speed by a frequency to obtain the travelled length (`speed / frequency = distance`), the
 * inverse-time counterpart of `speed * time = length`.
 *
 * Example:
 * ```kotlin
 * val v = (10 of meters) / (1 of seconds) // 10 m/s
 * (v / (5 of hertz)) into meters          // 2.0
 * ```
 */
operator fun KSpeedUnitInstance.div(frequency: KFrequencyUnitInstance): KLengthUnitInstance =
    (this.toUnit() / frequency.toInverseTime()).toLength()

// --- General fallback for a distance whose exponent is only known at runtime ---------------------

/**
 * `distance * frequency` fallback for a general [KDistanceUnitInstance]. Only a pure length (exponent 1)
 * yields a speed; anything else (area, volume, ...) throws.
 *
 * @throws IllegalStateException if the left operand is not a pure length (exponent 1).
 */
operator fun KDistanceUnitInstance.times(frequency: KFrequencyUnitInstance): KSpeedUnitInstance =
    (this.toUnit() * frequency.toInverseTime()).toSpeed()
