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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.energyInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf
import org.pcsoft.framework.kunit.kinematic.distance.toLength
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.toForce

// Cross-group operators for the two equivalent decompositions of a force per length - `force / length`
// (stiffness / line load) and `energy / area` (surface tension) - plus their inverses. They live in the
// line-force package because it may depend on force/distance/energy (the reverse must never happen).
//
// The energy pair is computed on the normalized values instead of going through the generic engine: the
// energy group stores its value in *joules* while its unit terms are gram-based, so the two scales must be
// bridged explicitly by [NM_IN_BASE] (1 J/m² = 1 kg/s² = 1000 g/s²).

/**
 * Divides a force by a length to obtain the force per length - the stiffness / line-load reading
 * (`force / length = lineforce`).
 *
 * Example:
 * ```kotlin
 * val k = (200 of newtons) / (0.05 of meters) // 4000 N/m
 * ```
 */
operator fun KForceUnitInstance.div(other: KLengthUnitInstance): KLineForceUnitInstance =
    (this.toUnit() / other.toUnit()).toLineForce()

/** Multiplies a force per length by a length to obtain the force (`lineforce * length = force`). */
operator fun KLineForceUnitInstance.times(other: KLengthUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/**
 * Multiplies a length by a force per length to obtain the force (`length * lineforce = force`); the
 * commutative counterpart of [KLineForceUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(other: KLineForceUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/** Divides a force by a force per length to obtain the length (`force / lineforce = length`). */
operator fun KForceUnitInstance.div(other: KLineForceUnitInstance): KLengthUnitInstance =
    (this.toUnit() / other.toUnit()).toLength()

/**
 * Divides an energy by an area to obtain the surface tension (`energy / area = lineforce`, since
 * `1 J/m² = 1 N/m`) - the second decomposition of the very same quantity.
 *
 * Example:
 * ```kotlin
 * val sigma = (72 of milli.joules) / ((1 of meters) * (1 of meters)) // 72 mN/m
 * ```
 */
operator fun KEnergyUnitInstance.div(other: KAreaUnitInstance): KLineForceUnitInstance =
    lineForceInstanceOf(value / other.value * NM_IN_BASE)

/** Multiplies a surface tension by an area to obtain the surface energy (`lineforce * area = energy`). */
operator fun KLineForceUnitInstance.times(other: KAreaUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value / NM_IN_BASE * other.value)

/**
 * Multiplies an area by a surface tension to obtain the surface energy (`area * lineforce = energy`); the
 * commutative counterpart of [KLineForceUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KLineForceUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * other.value / NM_IN_BASE)

/** Divides an energy by a surface tension to obtain the area (`energy / lineforce = area`). */
operator fun KEnergyUnitInstance.div(other: KLineForceUnitInstance): KAreaUnitInstance =
    areaOf(value / other.value * NM_IN_BASE)
