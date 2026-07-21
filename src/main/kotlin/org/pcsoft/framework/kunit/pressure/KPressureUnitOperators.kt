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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.toArea
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.toForce

// Cross-group operators that let force and area combine *directly* into a strongly typed pressure, and
// back, without the caller ever handling a raw KMixedUnitInstance. They live in the pressure package
// because it may depend on force/distance (the reverse must never happen), and each simply delegates to
// the generic KMixedUnitInstance `*`/`/` engine and re-wraps the result.

/**
 * Divides a force by an area to obtain a [KPressureUnitInstance] (`force / area = pressure`).
 *
 * Example:
 * ```kotlin
 * val p = (100 of newtons) / ((2 of meters) * (1 of meters)) // KPressureUnitInstance, 50 Pa
 * ```
 */
operator fun KForceUnitInstance.div(other: KAreaUnitInstance): KPressureUnitInstance =
    (this.toUnit() / other.toUnit()).toPressure()

/**
 * Multiplies a pressure by an area to obtain the force (`pressure * area = force`).
 */
operator fun KPressureUnitInstance.times(other: KAreaUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/**
 * Multiplies an area by a pressure to obtain the force (`area * pressure = force`); the commutative
 * counterpart of [KPressureUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KPressureUnitInstance): KForceUnitInstance =
    (this.toUnit() * other.toUnit()).toForce()

/**
 * Divides a force by a pressure to obtain the area (`force / pressure = area`).
 */
operator fun KForceUnitInstance.div(other: KPressureUnitInstance): KAreaUnitInstance =
    (this.toUnit() / other.toUnit()).toArea()
