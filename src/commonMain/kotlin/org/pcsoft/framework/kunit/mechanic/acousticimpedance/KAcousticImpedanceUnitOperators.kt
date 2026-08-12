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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.speedUnitInstanceOf
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pressureUnitInstanceOf

// Cross-group operators for the two decompositions of the acoustic impedance - `pressure / speed` and
// `density * speed` - plus their inverses. They live in this package because it may depend on
// pressure/speed/density (the reverse must never happen).
//
// Pressure, density and acoustic impedance all store their raw gram-based component value, so the
// operators work directly on `value` without any bridging factor.

/**
 * Divides a sound pressure by a particle velocity to obtain a [KAcousticImpedanceUnitInstance]
 * (`pressure / speed = acoustic impedance`).
 *
 * Example:
 * ```kotlin
 * val z = (413 of pascals) / ((1 of meters) / (1 of seconds)) // 413 Pa·s/m
 * ```
 */
operator fun KPressureUnitInstance.div(other: KSpeedUnitInstance): KAcousticImpedanceUnitInstance =
    acousticImpedanceInstanceOf(value / other.value)

/**
 * Multiplies a density by the speed of sound to obtain the characteristic acoustic impedance
 * (`density * speed = acoustic impedance`, `Z = ρ · c`) - the second decomposition of this group, yielding
 * the same typed, value-equal result as `pressure / speed`.
 */
operator fun KDensityUnitInstance.times(other: KSpeedUnitInstance): KAcousticImpedanceUnitInstance =
    acousticImpedanceInstanceOf(value * other.value)

/**
 * Multiplies a speed by a density to obtain the characteristic acoustic impedance; the commutative
 * counterpart of [KDensityUnitInstance.times].
 */
operator fun KSpeedUnitInstance.times(other: KDensityUnitInstance): KAcousticImpedanceUnitInstance =
    acousticImpedanceInstanceOf(value * other.value)

/**
 * Multiplies an acoustic impedance by a particle velocity to obtain the sound pressure
 * (`acoustic impedance * speed = pressure`).
 */
operator fun KAcousticImpedanceUnitInstance.times(other: KSpeedUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(value * other.value)

/**
 * Multiplies a particle velocity by an acoustic impedance to obtain the sound pressure; the commutative
 * counterpart of [KAcousticImpedanceUnitInstance.times].
 */
operator fun KSpeedUnitInstance.times(other: KAcousticImpedanceUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(value * other.value)

/**
 * Divides a sound pressure by an acoustic impedance to obtain the particle velocity
 * (`pressure / acoustic impedance = speed`).
 */
operator fun KPressureUnitInstance.div(other: KAcousticImpedanceUnitInstance): KSpeedUnitInstance =
    speedUnitInstanceOf(value / other.value)

/**
 * Divides an acoustic impedance by a speed to obtain the density
 * (`acoustic impedance / speed = density`) - the inverse of the `density * speed` decomposition.
 */
operator fun KAcousticImpedanceUnitInstance.div(other: KSpeedUnitInstance): KDensityUnitInstance =
    densityUnitInstanceOf(value / other.value)

/**
 * Divides an acoustic impedance by a density to obtain the speed of sound
 * (`acoustic impedance / density = speed`).
 */
operator fun KAcousticImpedanceUnitInstance.div(other: KDensityUnitInstance): KSpeedUnitInstance =
    speedUnitInstanceOf(value / other.value)
