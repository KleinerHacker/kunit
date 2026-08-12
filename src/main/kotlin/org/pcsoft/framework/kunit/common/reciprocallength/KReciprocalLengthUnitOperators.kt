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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf

// Cross-group operators for the decomposition of the reciprocal length - `count / length` - plus its
// inverses. They live in the reciprocal length package because it may depend on distance (the reverse must
// never happen). The shape mirrors the frequency group, which is the reciprocal of time in the same way.

/**
 * Divides a scalar count by a length to obtain a typed [KReciprocalLengthUnitInstance]
 * (`count / length = reciprocal length`). This is the direct, strongly typed way to build a refractive
 * power from a focal length (`1 / (0.4 of meters)` = 2.5 dpt) or a wavenumber from a wavelength, and is
 * more specific than the generic reciprocal `Number.div(KUnitMeasurable)`.
 *
 * Example:
 * ```kotlin
 * val d = 1 / (0.4 of meters) // KReciprocalLengthUnitInstance, 2.5 dpt
 * ```
 */
operator fun Number.div(length: KLengthUnitInstance): KReciprocalLengthUnitInstance =
    reciprocalLengthInstanceOf(this.toDouble() / length.value)

/**
 * Divides a scalar count by a reciprocal length to obtain the corresponding length
 * (`count / reciprocal length = length`) - the inverse of [Number.div] against a length, e.g.
 * `1 / (2.5 of dioptres)` = 0.4 m (the focal length).
 *
 * Example:
 * ```kotlin
 * val f = 1 / (2.5 of dioptres) // KLengthUnitInstance, 0.4 m
 * ```
 */
operator fun Number.div(reciprocalLength: KReciprocalLengthUnitInstance): KLengthUnitInstance =
    lengthOf(this.toDouble() / reciprocalLength.value)

/**
 * Multiplies a reciprocal length by a length to obtain the **dimensionless count**
 * (`reciprocal length * length = count`, `m⁻¹ · m = 1`).
 *
 * Example:
 * ```kotlin
 * val cycles = (2 of reciprocalMeters) * (3 of meters) // 6.0
 * ```
 */
operator fun KReciprocalLengthUnitInstance.times(length: KLengthUnitInstance): Double = value * length.value

/**
 * Multiplies a length by a reciprocal length to obtain the dimensionless count; the commutative
 * counterpart of [KReciprocalLengthUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(reciprocalLength: KReciprocalLengthUnitInstance): Double =
    value * reciprocalLength.value
