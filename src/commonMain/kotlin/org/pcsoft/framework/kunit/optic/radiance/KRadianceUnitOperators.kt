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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf
import org.pcsoft.framework.kunit.optic.radiantintensity.KRadiantIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.radiantintensity.radiantIntensityInstanceOf

// Cross-group operators for the decomposition of the radiance - `radiantIntensity / area` - plus its
// inverses. They live in the radiance package because it may depend on radiant-intensity/distance (the
// reverse must never happen).

/**
 * Divides a radiant intensity by an area to obtain a [KRadianceUnitInstance]
 * (`radiantIntensity / area = radiance`).
 *
 * Example:
 * ```kotlin
 * val l = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters)) // 5 W/(sr·m²)
 * ```
 */
operator fun KRadiantIntensityUnitInstance.div(other: KAreaUnitInstance): KRadianceUnitInstance =
    radianceInstanceOf(value / other.value)

/**
 * Multiplies a radiance by an area to obtain the radiant intensity it emits
 * (`radiance * area = radiantIntensity`).
 */
operator fun KRadianceUnitInstance.times(other: KAreaUnitInstance): KRadiantIntensityUnitInstance =
    radiantIntensityInstanceOf(value * other.value)

/**
 * Multiplies an area by a radiance to obtain the radiant intensity; the commutative counterpart of
 * [KRadianceUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KRadianceUnitInstance): KRadiantIntensityUnitInstance =
    radiantIntensityInstanceOf(value * other.value)

/**
 * Divides a radiant intensity by a radiance to obtain the emitting area
 * (`radiantIntensity / radiance = area`).
 */
operator fun KRadiantIntensityUnitInstance.div(other: KRadianceUnitInstance): KAreaUnitInstance =
    areaOf(value / other.value)
