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

import org.pcsoft.framework.kunit.length.KLengthUnitInstance
import org.pcsoft.framework.kunit.length.toKLengthUnit
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.toKTimeUnit

// Cross-group operators that let the core units (length, time) combine *directly* into a strongly
// typed speed, and back, without the caller ever handling a raw KMixedUnitInstance. They live in the
// speed package because it may depend on length/time (the reverse must never happen), and each simply
// delegates to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Divides a length by a time to obtain a [KSpeedUnitInstance] (`length / time = speed`).
 *
 * The left operand must be a **pure length** (exponent 1). If it is an area (exponent 2), a volume
 * (exponent 3), or any other non-1 exponent, the result (e.g. `m²·s⁻¹`) is not a speed and this
 * operator throws [IllegalStateException] rather than silently producing a wrong value - the same
 * guard [toKSpeedUnit] applies. Use the raw [org.pcsoft.framework.kunit.KMixedUnitInstance] `/`
 * operator if you deliberately want such a non-speed quantity.
 *
 * @throws IllegalStateException if the left operand is not a pure length (exponent 1).
 *
 * Example:
 * ```kotlin
 * val v = 100.meters / 10.seconds       // KSpeedUnitInstance, 10 m/s
 * v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR) // 36.0
 *
 * (200.meters * 50.meters) // an area (m²) - `area / time` is not a speed
 * // (2.hectares as a length) / 10.seconds -> throws IllegalStateException
 * ```
 */
operator fun KLengthUnitInstance.div(other: KTimeUnitInstance): KSpeedUnitInstance =
    (this.toKMixedUnitInstance() / other.toKMixedUnitInstance()).toKSpeedUnit()

/**
 * Multiplies a speed by a time to obtain the travelled length (`speed * time = length`).
 *
 * Example:
 * ```kotlin
 * val v = 10.metersPerSecond
 * (v * 60.seconds).valueAs(KLengthUnit.METER) // 600.0
 * ```
 */
operator fun KSpeedUnitInstance.times(other: KTimeUnitInstance): KLengthUnitInstance =
    (this.toKMixedUnitInstance() * other.toKMixedUnitInstance()).toKLengthUnit()

/**
 * Multiplies a time by a speed to obtain the travelled length (`time * speed = length`); the
 * commutative counterpart of [KSpeedUnitInstance.times].
 *
 * Example:
 * ```kotlin
 * (60.seconds * 10.metersPerSecond).valueAs(KLengthUnit.METER) // 600.0
 * ```
 */
operator fun KTimeUnitInstance.times(other: KSpeedUnitInstance): KLengthUnitInstance =
    (this.toKMixedUnitInstance() * other.toKMixedUnitInstance()).toKLengthUnit()

/**
 * Divides a length by a speed to obtain the required time (`length / speed = time`).
 *
 * The left operand must be a **pure length** (exponent 1); otherwise the result is not a plain time
 * and [toKTimeUnit] throws [IllegalStateException].
 *
 * @throws IllegalStateException if the left operand is not a pure length (exponent 1).
 *
 * Example:
 * ```kotlin
 * val v = 10.metersPerSecond
 * (600.meters / v).valueAs(KTimeUnit.SECOND) // 60.0
 * ```
 */
operator fun KLengthUnitInstance.div(other: KSpeedUnitInstance): KTimeUnitInstance =
    (this.toKMixedUnitInstance() / other.toKMixedUnitInstance()).toKTimeUnit()
