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

package org.pcsoft.framework.kunit.optic.luminance

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.solidAngleOf
import org.pcsoft.framework.kunit.optic.illuminance.KIlluminanceUnitInstance
import org.pcsoft.framework.kunit.optic.illuminance.illuminanceInstanceOf
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.luminousintensity.luminousIntensityOf

// Cross-group operators for the two decompositions of the luminance - `luminousIntensity / area` and
// `illuminance / solidAngle` - plus their inverses. They live in the luminance package because it may
// depend on luminous-intensity/distance/illuminance/solid-angle (the reverse must never happen).

/**
 * Divides a luminous intensity by an area to obtain a [KLuminanceUnitInstance]
 * (`luminousIntensity / area = luminance`).
 *
 * Example:
 * ```kotlin
 * val l = (250 of candelas) / ((1 of meters) * (1 of meters)) // 250 cd/m²
 * ```
 */
operator fun KLuminousIntensityUnitInstance.div(other: KAreaUnitInstance): KLuminanceUnitInstance =
    luminanceInstanceOf(value / other.value)

/**
 * Divides an illuminance by a solid angle to obtain the luminance
 * (`illuminance / solidAngle = luminance`) - the second decomposition of this group, yielding the same
 * typed, value-equal result as `luminousIntensity / area`.
 */
operator fun KIlluminanceUnitInstance.div(other: KSolidAngleUnitInstance): KLuminanceUnitInstance =
    luminanceInstanceOf(value / other.value)

/**
 * Multiplies a luminance by an area to obtain the luminous intensity it emits
 * (`luminance * area = luminousIntensity`).
 */
operator fun KLuminanceUnitInstance.times(other: KAreaUnitInstance): KLuminousIntensityUnitInstance =
    luminousIntensityOf(value * other.value)

/**
 * Multiplies an area by a luminance to obtain the luminous intensity; the commutative counterpart of
 * [KLuminanceUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KLuminanceUnitInstance): KLuminousIntensityUnitInstance =
    luminousIntensityOf(value * other.value)

/**
 * Divides a luminous intensity by a luminance to obtain the emitting area
 * (`luminousIntensity / luminance = area`).
 */
operator fun KLuminousIntensityUnitInstance.div(other: KLuminanceUnitInstance): KAreaUnitInstance =
    areaOf(value / other.value)

/**
 * Multiplies a luminance by a solid angle to obtain the illuminance
 * (`luminance * solidAngle = illuminance`) - the inverse of the `illuminance / solidAngle` decomposition.
 */
operator fun KLuminanceUnitInstance.times(other: KSolidAngleUnitInstance): KIlluminanceUnitInstance =
    illuminanceInstanceOf(value * other.value)

/**
 * Multiplies a solid angle by a luminance to obtain the illuminance; the commutative counterpart of
 * [KLuminanceUnitInstance.times].
 */
operator fun KSolidAngleUnitInstance.times(other: KLuminanceUnitInstance): KIlluminanceUnitInstance =
    illuminanceInstanceOf(value * other.value)

/**
 * Divides an illuminance by a luminance to obtain the solid angle it is spread over
 * (`illuminance / luminance = solidAngle`).
 */
operator fun KIlluminanceUnitInstance.div(other: KLuminanceUnitInstance): KSolidAngleUnitInstance =
    solidAngleOf(value / other.value)
