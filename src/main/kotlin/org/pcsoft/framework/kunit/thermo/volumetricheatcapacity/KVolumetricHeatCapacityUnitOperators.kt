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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.KGM3_IN_BASE
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.thermo.heatcapacity.KHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.heatcapacity.heatCapacityInstanceOf
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.specificHeatCapacityInstanceOf

// Cross-group operators for the two decompositions of the volumetric heat capacity -
// `heatCapacity / volume` and `specificHeatCapacity * density` - plus their inverses. They live in this
// package because it may depend on heat-capacity/distance/specific-heat-capacity/density (the reverse must
// never happen).
//
// The density stores its raw gram-based component value (g·m⁻³), so every operator pairing a density with
// a kilogram-based quantity divides or multiplies by KGM3_IN_BASE.

/**
 * Divides a heat capacity by a volume to obtain a [KVolumetricHeatCapacityUnitInstance]
 * (`heatCapacity / volume = volumetric heat capacity`).
 *
 * Example:
 * ```kotlin
 * val cv = (4184 of joulesPerKelvin) / (1 of liters) // ≈ 4.18 MJ/(m³·K)
 * ```
 */
operator fun KHeatCapacityUnitInstance.div(
    other: KVolumeUnitInstance
): KVolumetricHeatCapacityUnitInstance = volumetricHeatCapacityInstanceOf(value / other.value)

/**
 * Multiplies a specific heat capacity by a density to obtain the volumetric heat capacity
 * (`specificHeatCapacity * density = volumetric heat capacity`) - the second decomposition of this group,
 * yielding the same typed, value-equal result as `heatCapacity / volume`.
 */
operator fun KSpecificHeatCapacityUnitInstance.times(
    other: KDensityUnitInstance
): KVolumetricHeatCapacityUnitInstance =
    volumetricHeatCapacityInstanceOf(value * other.value / KGM3_IN_BASE)

/**
 * Multiplies a density by a specific heat capacity to obtain the volumetric heat capacity; the commutative
 * counterpart of [KSpecificHeatCapacityUnitInstance.times].
 */
operator fun KDensityUnitInstance.times(
    other: KSpecificHeatCapacityUnitInstance
): KVolumetricHeatCapacityUnitInstance =
    volumetricHeatCapacityInstanceOf(value * other.value / KGM3_IN_BASE)

/**
 * Multiplies a volumetric heat capacity by a volume to obtain the heat capacity of that volume
 * (`volumetric heat capacity * volume = heatCapacity`).
 */
operator fun KVolumetricHeatCapacityUnitInstance.times(
    other: KVolumeUnitInstance
): KHeatCapacityUnitInstance = heatCapacityInstanceOf(value * other.value)

/**
 * Multiplies a volume by a volumetric heat capacity to obtain the heat capacity; the commutative
 * counterpart of [KVolumetricHeatCapacityUnitInstance.times].
 */
operator fun KVolumeUnitInstance.times(
    other: KVolumetricHeatCapacityUnitInstance
): KHeatCapacityUnitInstance = heatCapacityInstanceOf(value * other.value)

/**
 * Divides a heat capacity by a volumetric heat capacity to obtain the volume it belongs to
 * (`heatCapacity / volumetric heat capacity = volume`).
 */
operator fun KHeatCapacityUnitInstance.div(
    other: KVolumetricHeatCapacityUnitInstance
): KVolumeUnitInstance = volumeOf(value / other.value)

/**
 * Divides a volumetric heat capacity by a density to obtain the specific heat capacity
 * (`volumetric heat capacity / density = specificHeatCapacity`) - the inverse of the
 * `specificHeatCapacity * density` decomposition.
 */
operator fun KVolumetricHeatCapacityUnitInstance.div(
    other: KDensityUnitInstance
): KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityInstanceOf(value / other.value * KGM3_IN_BASE)

/**
 * Divides a volumetric heat capacity by a specific heat capacity to obtain the density
 * (`volumetric heat capacity / specificHeatCapacity = density`).
 */
operator fun KVolumetricHeatCapacityUnitInstance.div(
    other: KSpecificHeatCapacityUnitInstance
): KDensityUnitInstance = densityUnitInstanceOf(value / other.value * KGM3_IN_BASE)
