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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.toVolume
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.toMass

// Cross-group operators that let volume and mass combine *directly* into a strongly typed specific volume,
// and back - plus the reciprocal bridge to the density group. They live in the specific-volume package
// because it may depend on distance/mass/density (the reverse must never happen).
//
// The reciprocal operators are computed on the normalized component values: both groups store gram-based
// raw components (`m³·g⁻¹` and `g·m⁻³`), so they are exact reciprocals of one another.

/**
 * Divides a volume by a mass to obtain the specific volume (`volume / mass = specificvolume`).
 *
 * Example:
 * ```kotlin
 * val v = (2 of liters) / (1 of kilo.grams) // 0.002 m³/kg
 * ```
 */
operator fun KVolumeUnitInstance.div(other: KMassUnitInstance): KSpecificVolumeUnitInstance =
    (this.toUnit() / other.toUnit()).toSpecificVolume()

/** Multiplies a specific volume by a mass to obtain the volume (`specificvolume * mass = volume`). */
operator fun KSpecificVolumeUnitInstance.times(other: KMassUnitInstance): KVolumeUnitInstance =
    (this.toUnit() * other.toUnit()).toVolume()

/**
 * Multiplies a mass by a specific volume to obtain the volume (`mass * specificvolume = volume`); the
 * commutative counterpart of [KSpecificVolumeUnitInstance.times].
 */
operator fun KMassUnitInstance.times(other: KSpecificVolumeUnitInstance): KVolumeUnitInstance =
    (this.toUnit() * other.toUnit()).toVolume()

/** Divides a volume by a specific volume to obtain the mass (`volume / specificvolume = mass`). */
operator fun KVolumeUnitInstance.div(other: KSpecificVolumeUnitInstance): KMassUnitInstance =
    (this.toUnit() / other.toUnit()).toMass()

/**
 * Inverts a density into the specific volume (`1 / density = specificvolume`). This more specific overload
 * wins over the generic `Number.div(KUnitMeasurable)`, so the result stays typed.
 *
 * Example:
 * ```kotlin
 * val v = 1 / ((1000 of kilo.grams) / (1 of (meters pow 3))) // 0.001 m³/kg
 * ```
 */
operator fun Number.div(density: KDensityUnitInstance): KSpecificVolumeUnitInstance =
    specificVolumeInstanceOf(this.toDouble() / density.value)

/**
 * Inverts a specific volume into the density (`1 / specificvolume = density`) - the counterpart of the
 * density inversion above.
 */
operator fun Number.div(specificVolume: KSpecificVolumeUnitInstance): KDensityUnitInstance =
    densityUnitInstanceOf(this.toDouble() / specificVolume.value)
