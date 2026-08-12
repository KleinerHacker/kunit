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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.luminousFluxInstanceOf

// Cross-group operators for the decomposition of the luminous energy - `luminousFlux * time` - plus its
// inverses. They live in the luminous energy package because it may depend on luminous-flux/time (the
// reverse must never happen).

/**
 * Multiplies a luminous flux by a time to obtain a [KLuminousEnergyUnitInstance]
 * (`luminousFlux * time = luminous energy`).
 *
 * Example:
 * ```kotlin
 * val q = (800 of lumens) * (2 of hours) // 1600 lm·h
 * ```
 */
operator fun KLuminousFluxUnitInstance.times(other: KTimeUnitInstance): KLuminousEnergyUnitInstance =
    luminousEnergyInstanceOf(value * other.value)

/**
 * Multiplies a time by a luminous flux to obtain the luminous energy; the commutative counterpart of
 * [KLuminousFluxUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KLuminousFluxUnitInstance): KLuminousEnergyUnitInstance =
    luminousEnergyInstanceOf(value * other.value)

/**
 * Divides a luminous energy by a time to obtain the average luminous flux
 * (`luminous energy / time = luminousFlux`).
 */
operator fun KLuminousEnergyUnitInstance.div(other: KTimeUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value / other.value)

/**
 * Divides a luminous energy by a luminous flux to obtain the time it was emitted over
 * (`luminous energy / luminousFlux = time`).
 */
operator fun KLuminousEnergyUnitInstance.div(other: KLuminousFluxUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / other.value)
