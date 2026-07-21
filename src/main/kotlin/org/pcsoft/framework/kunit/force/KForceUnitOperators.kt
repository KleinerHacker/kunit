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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.acceleration.toAcceleration
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.toMass

// Cross-group operators that let mass and acceleration combine *directly* into a strongly typed force,
// and back, without the caller ever handling a raw KMixedUnitInstance. They live in the force package
// because it may depend on mass/acceleration (the reverse must never happen), and each simply delegates
// to the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Multiplies a mass by an acceleration to obtain a [KForceUnitInstance] (`mass * acceleration = force`;
 * Newton's second law).
 *
 * Example:
 * ```kotlin
 * val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
 * ```
 */
operator fun KMassUnitInstance.times(other: KAccelerationUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/**
 * Multiplies an acceleration by a mass to obtain a force (`acceleration * mass = force`); the
 * commutative counterpart of [KMassUnitInstance.times].
 */
operator fun KAccelerationUnitInstance.times(other: KMassUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/**
 * Divides a force by a mass to obtain the acceleration (`force / mass = acceleration`).
 *
 * Example:
 * ```kotlin
 * ((10 of newtons) / (2 of kilo.grams)) // KAccelerationUnitInstance, 5 m/s²
 * ```
 */
operator fun KForceUnitInstance.div(other: KMassUnitInstance): KAccelerationUnitInstance =
    (this.toUnit() / other.toUnit()).toAcceleration()

/**
 * Divides a force by an acceleration to obtain the mass (`force / acceleration = mass`).
 */
operator fun KForceUnitInstance.div(other: KAccelerationUnitInstance): KMassUnitInstance =
    (this.toUnit() / other.toUnit()).toMass()
