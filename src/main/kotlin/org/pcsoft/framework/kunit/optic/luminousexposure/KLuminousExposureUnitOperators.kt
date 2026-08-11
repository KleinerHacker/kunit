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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.optic.illuminance.KIlluminanceUnitInstance
import org.pcsoft.framework.kunit.optic.illuminance.illuminanceInstanceOf

// Cross-group operators for the decomposition of the luminous exposure - `illuminance * time` - plus its
// inverses. They live in the luminous exposure package because it may depend on illuminance/time (the
// reverse must never happen).

/**
 * Multiplies an illuminance by a time to obtain a [KLuminousExposureUnitInstance]
 * (`illuminance * time = luminous exposure`).
 *
 * Example:
 * ```kotlin
 * val h = (50 of lux) * (8 of hours) // 400 lx·h
 * ```
 */
operator fun KIlluminanceUnitInstance.times(other: KTimeUnitInstance): KLuminousExposureUnitInstance =
    luminousExposureInstanceOf(value * other.value)

/**
 * Multiplies a time by an illuminance to obtain the luminous exposure; the commutative counterpart of
 * [KIlluminanceUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KIlluminanceUnitInstance): KLuminousExposureUnitInstance =
    luminousExposureInstanceOf(value * other.value)

/**
 * Divides a luminous exposure by a time to obtain the average illuminance
 * (`luminous exposure / time = illuminance`).
 */
operator fun KLuminousExposureUnitInstance.div(other: KTimeUnitInstance): KIlluminanceUnitInstance =
    illuminanceInstanceOf(value / other.value)

/**
 * Divides a luminous exposure by an illuminance to obtain the exposure time
 * (`luminous exposure / illuminance = time`).
 */
operator fun KLuminousExposureUnitInstance.div(other: KIlluminanceUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / other.value)
