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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.toTime
import org.pcsoft.framework.kunit.kinematic.volumeflow.KVolumeFlowUnitInstance
import org.pcsoft.framework.kunit.kinematic.volumeflow.toVolumeFlow
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.toMass

// Cross-group operators for the two equivalent decompositions of mass flow - `mass / time` and
// `density * volumeflow` - plus their inverses. They live in the mass-flow package because it may depend
// on the mass/time/density/volume-flow groups (the reverse must never happen). Each delegates to the
// generic KMixedUnitInstance `*`/`/` engine and re-wraps through [toMassFlow], so both decompositions end
// up in the very same canonical normal form.

/**
 * Divides a mass by a time to obtain the mass flow rate (`mass / time = massflow`).
 *
 * Example:
 * ```kotlin
 * val m = (2 of kilo.grams) / (1 of seconds) // 2 kg/s
 * ```
 */
operator fun KMassUnitInstance.div(other: KTimeUnitInstance): KMassFlowUnitInstance =
    (this.toUnit() / other.toUnit()).toMassFlow()

/** Multiplies a mass flow by a time to obtain the transported mass (`massflow * time = mass`). */
operator fun KMassFlowUnitInstance.times(other: KTimeUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Multiplies a time by a mass flow to obtain the transported mass (`time * massflow = mass`); the
 * commutative counterpart of [KMassFlowUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KMassFlowUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/** Divides a mass by a mass flow to obtain the required time (`mass / massflow = time`). */
operator fun KMassUnitInstance.div(other: KMassFlowUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()

/**
 * Multiplies a density by a volumetric flow to obtain the mass flow
 * (`density * volumeflow = massflow`) - the second decomposition of the very same quantity.
 *
 * Example:
 * ```kotlin
 * val water = (1000 of kilo.grams) / (1 of (meters pow 3))
 * val m = water * (2 of cubicMetersPerSecond) // 2000 kg/s
 * ```
 */
operator fun KDensityUnitInstance.times(other: KVolumeFlowUnitInstance): KMassFlowUnitInstance =
    (this.toUnit() * other.toUnit()).toMassFlow()

/**
 * Multiplies a volumetric flow by a density to obtain the mass flow (`volumeflow * density = massflow`);
 * the commutative counterpart of [KDensityUnitInstance.times].
 */
operator fun KVolumeFlowUnitInstance.times(other: KDensityUnitInstance): KMassFlowUnitInstance =
    (this.toUnit() * other.toUnit()).toMassFlow()

/** Divides a mass flow by the density to obtain the volumetric flow (`massflow / density = volumeflow`). */
operator fun KMassFlowUnitInstance.div(other: KDensityUnitInstance): KVolumeFlowUnitInstance =
    (this.toUnit() / other.toUnit()).toVolumeFlow()

/** Divides a mass flow by the volumetric flow to obtain the density (`massflow / volumeflow = density`). */
operator fun KMassFlowUnitInstance.div(other: KVolumeFlowUnitInstance): KDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toDensity()
