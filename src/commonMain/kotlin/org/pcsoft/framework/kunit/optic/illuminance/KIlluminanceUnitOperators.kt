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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.luminousFluxInstanceOf

// Cross-group operators for the decomposition of the illuminance - `luminousFlux / area` - plus its
// inverses. They live in the illuminance package because it may depend on luminous-flux/distance (the
// reverse must never happen).

/**
 * Divides a luminous flux by an area to obtain a [KIlluminanceUnitInstance]
 * (`luminousFlux / area = illuminance`).
 *
 * Example:
 * ```kotlin
 * val e = (1000 of lumens) / ((2 of meters) * (1 of meters)) // 500 lx
 * ```
 */
operator fun KLuminousFluxUnitInstance.div(other: KAreaUnitInstance): KIlluminanceUnitInstance =
    illuminanceInstanceOf(value / other.value)

/**
 * Multiplies an illuminance by an area to obtain the luminous flux falling onto it
 * (`illuminance * area = luminousFlux`).
 */
operator fun KIlluminanceUnitInstance.times(other: KAreaUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Multiplies an area by an illuminance to obtain the luminous flux; the commutative counterpart of
 * [KIlluminanceUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KIlluminanceUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Divides a luminous flux by an illuminance to obtain the area it covers
 * (`luminousFlux / illuminance = area`).
 */
operator fun KLuminousFluxUnitInstance.div(other: KIlluminanceUnitInstance): KAreaUnitInstance =
    areaOf(value / other.value)
