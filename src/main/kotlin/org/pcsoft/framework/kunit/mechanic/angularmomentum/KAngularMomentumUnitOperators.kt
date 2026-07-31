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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.toLength
import org.pcsoft.framework.kunit.mechanic.angularvelocity.KAngularVelocityUnitInstance
import org.pcsoft.framework.kunit.mechanic.angularvelocity.angularVelocityInstanceOf
import org.pcsoft.framework.kunit.mechanic.inertia.KInertiaUnitInstance
import org.pcsoft.framework.kunit.mechanic.inertia.inertiaInstanceOf
import org.pcsoft.framework.kunit.mechanic.momentum.KMomentumUnitInstance
import org.pcsoft.framework.kunit.mechanic.momentum.toMomentum

// Cross-group operators for the two equivalent decompositions of angular momentum -
// `inertia * angularvelocity` and `momentum * length` - plus their inverses. They live in the
// angular-momentum package because it may depend on the inertia/angular-velocity/momentum/distance groups
// (the reverse must never happen).
//
// The `inertia * angularvelocity` pair is computed on the normalized component values instead of going
// through the generic engine: the radian is a dimensionless ratio, so it must not survive as a unit term
// of the canonical normal form (`g·m²·s⁻¹`). Both raw component scales are gram-based, hence the plain
// product is already the canonical component value.

/**
 * Multiplies a moment of inertia by an angular velocity to obtain the angular momentum
 * (`inertia * angularvelocity = angularmomentum`).
 *
 * Example:
 * ```kotlin
 * val l = (2 of kilogramMetersSquared) * (3 of radiansPerSecond) // 6 kg·m²/s
 * ```
 */
operator fun KInertiaUnitInstance.times(other: KAngularVelocityUnitInstance): KAngularMomentumUnitInstance =
    angularMomentumInstanceOf(value * other.value)

/**
 * Multiplies an angular velocity by a moment of inertia (`angularvelocity * inertia = angularmomentum`);
 * the commutative counterpart of [KInertiaUnitInstance.times].
 */
operator fun KAngularVelocityUnitInstance.times(other: KInertiaUnitInstance): KAngularMomentumUnitInstance =
    angularMomentumInstanceOf(value * other.value)

/**
 * Multiplies a momentum by the lever arm to obtain the angular momentum
 * (`momentum * length = angularmomentum`) - the second decomposition of the very same quantity.
 *
 * Example:
 * ```kotlin
 * val l = (6 of kilogramMetersPerSecond) * (2 of meters) // 12 kg·m²/s
 * ```
 */
operator fun KMomentumUnitInstance.times(other: KLengthUnitInstance): KAngularMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toAngularMomentum()

/**
 * Multiplies a lever arm by a momentum (`length * momentum = angularmomentum`); the commutative
 * counterpart of [KMomentumUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(other: KMomentumUnitInstance): KAngularMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toAngularMomentum()

/**
 * Divides an angular momentum by the moment of inertia to obtain the angular velocity
 * (`angularmomentum / inertia = angularvelocity`).
 */
operator fun KAngularMomentumUnitInstance.div(other: KInertiaUnitInstance): KAngularVelocityUnitInstance =
    angularVelocityInstanceOf(value / other.value)

/**
 * Divides an angular momentum by the angular velocity to obtain the moment of inertia
 * (`angularmomentum / angularvelocity = inertia`).
 */
operator fun KAngularMomentumUnitInstance.div(other: KAngularVelocityUnitInstance): KInertiaUnitInstance =
    inertiaInstanceOf(value / other.value)

/**
 * Divides an angular momentum by the lever arm to obtain the momentum
 * (`angularmomentum / length = momentum`).
 */
operator fun KAngularMomentumUnitInstance.div(other: KLengthUnitInstance): KMomentumUnitInstance =
    (this.toUnit() / other.toUnit()).toMomentum()

/**
 * Divides an angular momentum by the momentum to obtain the lever arm
 * (`angularmomentum / momentum = length`).
 */
operator fun KAngularMomentumUnitInstance.div(other: KMomentumUnitInstance): KLengthUnitInstance =
    (this.toUnit() / other.toUnit()).toLength()
