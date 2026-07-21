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

package org.pcsoft.framework.kunit.density

import org.pcsoft.framework.kunit.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.distance.toVolume
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.toMass

// Cross-group operators that let mass and volume combine *directly* into a strongly typed density, and
// back, without the caller ever handling a raw KMixedUnitInstance. They live in the density package
// because it may depend on mass/distance (the reverse must never happen), and each simply delegates to
// the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Divides a mass by a volume to obtain a [KDensityUnitInstance] (`mass / volume = density`).
 *
 * Example:
 * ```kotlin
 * val rho = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
 * ```
 */
operator fun KMassUnitInstance.div(other: KVolumeUnitInstance): KDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toDensity()

/**
 * Multiplies a density by a volume to obtain the mass (`density * volume = mass`).
 */
operator fun KDensityUnitInstance.times(other: KVolumeUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Multiplies a volume by a density to obtain the mass (`volume * density = mass`); the commutative
 * counterpart of [KDensityUnitInstance.times].
 */
operator fun KVolumeUnitInstance.times(other: KDensityUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Divides a mass by a density to obtain the volume (`mass / density = volume`).
 */
operator fun KMassUnitInstance.div(other: KDensityUnitInstance): KVolumeUnitInstance =
    (this.toUnit() / other.toUnit()).toVolume()
