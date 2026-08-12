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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.toTime
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.angle.toAngle

// Cross-group operators that let angle and time combine *directly* into a strongly typed angular
// velocity, and back. They live in the angular-velocity package because it may depend on angle/time (the
// reverse must never happen), and each delegates to the generic KMixedUnitInstance `*`/`/` engine and
// re-wraps the result through the group's form-recognition hook.

/**
 * Divides an angle by a time to obtain an angular velocity (`angle / time = angularvelocity`).
 *
 * Example:
 * ```kotlin
 * val w = (1 of turns) / (1 of seconds) // KAngularVelocityUnitInstance, 60 rpm
 * ```
 */
operator fun KAngleUnitInstance.div(other: KTimeUnitInstance): KAngularVelocityUnitInstance =
    (this.toUnit() / other.toUnit()).toAngularVelocity()

/**
 * Multiplies an angular velocity by a time to obtain the swept angle (`angularvelocity * time = angle`).
 */
operator fun KAngularVelocityUnitInstance.times(other: KTimeUnitInstance): KAngleUnitInstance =
    (this.toUnit() * other.toUnit()).toAngle()

/**
 * Multiplies a time by an angular velocity to obtain the swept angle (`time * angularvelocity = angle`);
 * the commutative counterpart of [KAngularVelocityUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KAngularVelocityUnitInstance): KAngleUnitInstance =
    (this.toUnit() * other.toUnit()).toAngle()

/**
 * Divides an angle by an angular velocity to obtain the required time (`angle / angularvelocity = time`).
 */
operator fun KAngleUnitInstance.div(other: KAngularVelocityUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
