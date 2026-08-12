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

package org.pcsoft.framework.kunit.kinematic.jerk

import org.pcsoft.framework.kunit.kinematic.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.kinematic.acceleration.accelerationUnitInstanceOf
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf

// Cross-group operators for the decomposition of the jerk - `acceleration / time` - plus its inverses.
// They live in the jerk package because it may depend on acceleration/time (the reverse must never
// happen).

/**
 * Divides an acceleration by a time to obtain a [KJerkUnitInstance]
 * (`acceleration / time = jerk`).
 *
 * Example:
 * ```kotlin
 * val j = (((1.2 of meters) / (1 of seconds)) / (1 of seconds)) / (2 of seconds) // 0.6 m/s³ (an acceleration of 1.2 m/s² built up over 2 s)
 * ```
 */
operator fun KAccelerationUnitInstance.div(other: KTimeUnitInstance): KJerkUnitInstance =
    jerkInstanceOf(value / other.value)

/**
 * Multiplies a jerk by a time to obtain the acceleration change it produces
 * (`jerk * time = acceleration`).
 */
operator fun KJerkUnitInstance.times(other: KTimeUnitInstance): KAccelerationUnitInstance =
    accelerationUnitInstanceOf(value * other.value)

/**
 * Multiplies a time by a jerk to obtain the acceleration change; the commutative counterpart of
 * [KJerkUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KJerkUnitInstance): KAccelerationUnitInstance =
    accelerationUnitInstanceOf(value * other.value)

/**
 * Divides an acceleration by a jerk to obtain the time the change takes
 * (`acceleration / jerk = time`).
 */
operator fun KAccelerationUnitInstance.div(other: KJerkUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / other.value)
