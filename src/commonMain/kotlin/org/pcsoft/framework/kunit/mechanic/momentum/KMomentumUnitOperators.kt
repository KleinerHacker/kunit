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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.toSpeed
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.toTime
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.toForce
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.toMass

// Cross-group operators for the two equivalent decompositions of momentum - `mass * speed` and the
// impulse form `force * time` - plus their inverses. They live in the momentum package because it may
// depend on mass/speed/force/time (the reverse must never happen), and each delegates to the generic
// KMixedUnitInstance `*`/`/` engine, re-wrapping through [toMomentum] so both decompositions end up in
// the very same canonical normal form.

/**
 * Multiplies a mass by a speed to obtain a momentum (`mass * speed = momentum`).
 *
 * Example:
 * ```kotlin
 * val p = (2 of kilo.grams) * (3 of meters / seconds) // 6 kg·m/s
 * ```
 */
operator fun KMassUnitInstance.times(other: KSpeedUnitInstance): KMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toMomentum()

/**
 * Multiplies a speed by a mass to obtain a momentum (`speed * mass = momentum`); the commutative
 * counterpart of [KMassUnitInstance.times].
 */
operator fun KSpeedUnitInstance.times(other: KMassUnitInstance): KMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toMomentum()

/**
 * Multiplies a force by a time to obtain the impulse, i.e. the momentum change
 * (`force * time = momentum`). This is the second decomposition of the very same quantity.
 *
 * Example:
 * ```kotlin
 * val p = (10 of newtons) * (3 of seconds) // 30 N·s = 30 kg·m/s
 * ```
 */
operator fun KForceUnitInstance.times(other: KTimeUnitInstance): KMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toMomentum()

/**
 * Multiplies a time by a force to obtain the impulse (`time * force = momentum`); the commutative
 * counterpart of [KForceUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KForceUnitInstance): KMomentumUnitInstance =
    (this.toUnit() * other.toUnit()).toMomentum()

/** Divides a momentum by its mass to obtain the speed (`momentum / mass = speed`). */
operator fun KMomentumUnitInstance.div(other: KMassUnitInstance): KSpeedUnitInstance =
    (this.toUnit() / other.toUnit()).toSpeed()

/** Divides a momentum by its speed to obtain the mass (`momentum / speed = mass`). */
operator fun KMomentumUnitInstance.div(other: KSpeedUnitInstance): KMassUnitInstance =
    (this.toUnit() / other.toUnit()).toMass()

/** Divides a momentum (impulse) by the time to obtain the average force (`momentum / time = force`). */
operator fun KMomentumUnitInstance.div(other: KTimeUnitInstance): KForceUnitInstance =
    (this.toUnit() / other.toUnit()).toForce()

/** Divides a momentum (impulse) by the force to obtain the acting time (`momentum / force = time`). */
operator fun KMomentumUnitInstance.div(other: KForceUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()
