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

import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.toSpeed
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.toTime

// Cross-group operators that let speed and time combine *directly* into a strongly typed acceleration,
// and back, without the caller ever handling a raw KMixedUnitInstance. They live in the acceleration
// package because it may depend on speed/time (the reverse must never happen), and each simply delegates
// to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Divides a speed by a time to obtain an [KAccelerationUnitInstance] (`speed / time = acceleration`).
 *
 * Example:
 * ```kotlin
 * val a = (100 of meters / seconds) / (10 of seconds) // KAccelerationUnitInstance, 10 m/s²
 * ```
 */
operator fun KSpeedUnitInstance.div(other: KTimeUnitInstance): KAccelerationUnitInstance =
    (this.toUnit() / other.toUnit()).toAcceleration()

/**
 * Multiplies an acceleration by a time to obtain the gained speed (`acceleration * time = speed`).
 *
 * Example:
 * ```kotlin
 * ((9.81 of gals) * (2 of seconds)) // KSpeedUnitInstance
 * ```
 */
operator fun KAccelerationUnitInstance.times(other: KTimeUnitInstance): KSpeedUnitInstance =
    (this.toUnit() * other.toUnit()).toSpeed()

/**
 * Multiplies a time by an acceleration to obtain the gained speed (`time * acceleration = speed`); the
 * commutative counterpart of [KAccelerationUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KAccelerationUnitInstance): KSpeedUnitInstance =
    (this.toUnit() * other.toUnit()).toSpeed()

/**
 * Divides a speed by an acceleration to obtain the required time (`speed / acceleration = time`).
 *
 * Example:
 * ```kotlin
 * ((100 of meters / seconds) / (10 of meters / (seconds pow 2))) // KTimeUnitInstance, 10 s
 * ```
 */
operator fun KSpeedUnitInstance.div(other: KAccelerationUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
