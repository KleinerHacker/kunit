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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.kinematic.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.kinematic.acceleration.accelerationUnitInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.forceUnitInstanceOf

// Cross-group operators for the two decompositions of the specific weight - `force / volume` and
// `density * acceleration` - plus their inverses. They live in this package because it may depend on
// force/distance/density/acceleration (the reverse must never happen).
//
// All four groups involved (force, density, specific weight) store their raw gram-based component value,
// so the operators work directly on `value` without any bridging factor.

/**
 * Divides a force by a volume to obtain a [KSpecificWeightUnitInstance]
 * (`force / volume = specific weight`).
 *
 * Example:
 * ```kotlin
 * val gamma = (9807 of newtons) / ((1 of meters) * (1 of meters) * (1 of meters)) // water
 * ```
 */
operator fun KForceUnitInstance.div(other: KVolumeUnitInstance): KSpecificWeightUnitInstance =
    specificWeightInstanceOf(value / other.value)

/**
 * Multiplies a density by an acceleration to obtain the specific weight
 * (`density * acceleration = specific weight`) - the second decomposition of this group, yielding the same
 * typed, value-equal result as `force / volume`. With the standard gravity this is the ordinary
 * `γ = ρ · g`.
 */
operator fun KDensityUnitInstance.times(
    other: KAccelerationUnitInstance
): KSpecificWeightUnitInstance = specificWeightInstanceOf(value * other.value)

/**
 * Multiplies an acceleration by a density to obtain the specific weight; the commutative counterpart of
 * [KDensityUnitInstance.times].
 */
operator fun KAccelerationUnitInstance.times(
    other: KDensityUnitInstance
): KSpecificWeightUnitInstance = specificWeightInstanceOf(value * other.value)

/**
 * Multiplies a specific weight by a volume to obtain the weight force of that volume
 * (`specific weight * volume = force`).
 */
operator fun KSpecificWeightUnitInstance.times(other: KVolumeUnitInstance): KForceUnitInstance =
    forceUnitInstanceOf(value * other.value)

/**
 * Multiplies a volume by a specific weight to obtain the weight force; the commutative counterpart of
 * [KSpecificWeightUnitInstance.times].
 */
operator fun KVolumeUnitInstance.times(other: KSpecificWeightUnitInstance): KForceUnitInstance =
    forceUnitInstanceOf(value * other.value)

/**
 * Divides a force by a specific weight to obtain the volume it belongs to
 * (`force / specific weight = volume`).
 */
operator fun KForceUnitInstance.div(other: KSpecificWeightUnitInstance): KVolumeUnitInstance =
    volumeOf(value / other.value)

/**
 * Divides a specific weight by an acceleration to obtain the density
 * (`specific weight / acceleration = density`) - the inverse of the `density * acceleration`
 * decomposition.
 */
operator fun KSpecificWeightUnitInstance.div(
    other: KAccelerationUnitInstance
): KDensityUnitInstance = densityUnitInstanceOf(value / other.value)

/**
 * Divides a specific weight by a density to obtain the acceleration
 * (`specific weight / density = acceleration`).
 */
operator fun KSpecificWeightUnitInstance.div(
    other: KDensityUnitInstance
): KAccelerationUnitInstance = accelerationUnitInstanceOf(value / other.value)
