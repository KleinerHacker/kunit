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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.electricCurrentOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf

// Cross-group operators for the decomposition of the magnetic moment - `current * area` - plus its
// inverses. They live in the magnetic moment package because it may depend on current/distance (the
// reverse must never happen).

/**
 * Multiplies a current by the area of the loop it flows around to obtain a
 * [KMagneticMomentUnitInstance] (`current * area = magnetic moment`).
 *
 * Example:
 * ```kotlin
 * val m = (2 of amperes) * ((0.1 of meters) * (0.05 of meters)) // 0.01 A·m²
 * ```
 */
operator fun KElectricCurrentUnitInstance.times(other: KAreaUnitInstance): KMagneticMomentUnitInstance =
    magneticMomentInstanceOf(value * other.value)

/**
 * Multiplies an area by a current to obtain the magnetic moment; the commutative counterpart of
 * [KElectricCurrentUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KElectricCurrentUnitInstance): KMagneticMomentUnitInstance =
    magneticMomentInstanceOf(value * other.value)

/**
 * Divides a magnetic moment by the loop area to obtain the current
 * (`magnetic moment / area = current`).
 */
operator fun KMagneticMomentUnitInstance.div(other: KAreaUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value / other.value)

/**
 * Divides a magnetic moment by the current to obtain the loop area
 * (`magnetic moment / current = area`).
 */
operator fun KMagneticMomentUnitInstance.div(
    other: KElectricCurrentUnitInstance
): KAreaUnitInstance = areaOf(value / other.value)
