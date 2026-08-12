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

package org.pcsoft.framework.kunit.mechanic.angularacceleration

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.toTime
import org.pcsoft.framework.kunit.mechanic.angularvelocity.KAngularVelocityUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularvelocity.toAngularVelocity

// Cross-group operators that let angular velocity and time combine *directly* into a strongly typed
// angular acceleration, and back. They live in the angular-acceleration package because it may depend on
// the angular-velocity and time groups (the reverse must never happen).
//
// The second, fully native decomposition `angle / time²` needs no operator of its own: it stays a generic
// mixed unit and is converted through [toAngularAcceleration].

/**
 * Divides an angular velocity by a time to obtain an angular acceleration
 * (`angularvelocity / time = angularacceleration`).
 *
 * Example:
 * ```kotlin
 * val alpha = (1 of revolutionsPerSecond) / (2 of seconds) // ≈ 3.1416 rad/s²
 * ```
 */
operator fun KAngularVelocityUnitInstance.div(other: KTimeUnitInstance): KAngularAccelerationUnitInstance =
    (this.toUnit() / other.toUnit()).toAngularAcceleration()

/**
 * Multiplies an angular acceleration by a time to obtain the gained angular velocity
 * (`angularacceleration * time = angularvelocity`).
 */
operator fun KAngularAccelerationUnitInstance.times(other: KTimeUnitInstance): KAngularVelocityUnitInstance =
    (this.toUnit() * other.toUnit()).toAngularVelocity()

/**
 * Multiplies a time by an angular acceleration to obtain the gained angular velocity
 * (`time * angularacceleration = angularvelocity`); the commutative counterpart of
 * [KAngularAccelerationUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KAngularAccelerationUnitInstance): KAngularVelocityUnitInstance =
    (this.toUnit() * other.toUnit()).toAngularVelocity()

/**
 * Divides an angular velocity by an angular acceleration to obtain the required time
 * (`angularvelocity / angularacceleration = time`).
 */
operator fun KAngularVelocityUnitInstance.div(other: KAngularAccelerationUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
