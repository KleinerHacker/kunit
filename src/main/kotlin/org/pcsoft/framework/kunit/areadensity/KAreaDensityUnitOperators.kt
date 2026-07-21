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

package org.pcsoft.framework.kunit.areadensity

import org.pcsoft.framework.kunit.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.density.toDensity
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.toArea
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.toMass

// Cross-group operators that let mass and area combine *directly* into a strongly typed area density,
// and back (plus the density·length bridge), without the caller ever handling a raw KMixedUnitInstance.
// They live in the areadensity package because it may depend on mass/distance/density (the reverse must
// never happen), and each simply delegates to the generic KMixedUnitInstance `*`/`/` engine.

/**
 * Divides a mass by an area to obtain a [KAreaDensityUnitInstance] (`mass / area = area density`).
 *
 * Example:
 * ```kotlin
 * val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
 * ```
 */
operator fun KMassUnitInstance.div(other: KAreaUnitInstance): KAreaDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toAreaDensity()

/**
 * Multiplies an area density by an area to obtain the mass (`area density * area = mass`).
 */
operator fun KAreaDensityUnitInstance.times(other: KAreaUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Multiplies an area by an area density to obtain the mass (`area * area density = mass`); the
 * commutative counterpart of [KAreaDensityUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KAreaDensityUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Divides a mass by an area density to obtain the area (`mass / area density = area`).
 */
operator fun KMassUnitInstance.div(other: KAreaDensityUnitInstance): KAreaUnitInstance =
    (this.toUnit() / other.toUnit()).toArea()

/**
 * Multiplies a density by a length to obtain the area density (`density * length = area density`); a
 * plate of the given material and thickness has this surface mass.
 */
operator fun KDensityUnitInstance.times(other: KLengthUnitInstance): KAreaDensityUnitInstance =
    (this.toUnit() * other.toUnit()).toAreaDensity()

/**
 * Divides an area density by a length to obtain the (volumetric) density (`area density / length =
 * density`); the inverse of [KDensityUnitInstance.times].
 */
operator fun KAreaDensityUnitInstance.div(other: KLengthUnitInstance): KDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toDensity()
