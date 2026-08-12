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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.electric.fieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electric.fieldstrength.electricFieldStrengthInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf

// Cross-group operators for the decomposition of the electric flux - `electricFieldStrength * area` - plus
// its inverses. They live in the electric flux package because it may depend on field-strength/distance
// (the reverse must never happen).

/**
 * Multiplies an electric field strength by the area it passes through to obtain a
 * [KElectricFluxUnitInstance] (`electricFieldStrength * area = electric flux`).
 *
 * Example:
 * ```kotlin
 * val phi = (1000 of voltsPerMeter) * ((0.1 of meters) * (0.05 of meters)) // 5 V·m
 * ```
 */
operator fun KElectricFieldStrengthUnitInstance.times(
    other: KAreaUnitInstance
): KElectricFluxUnitInstance = electricFluxInstanceOf(value * other.value)

/**
 * Multiplies an area by an electric field strength to obtain the electric flux; the commutative
 * counterpart of [KElectricFieldStrengthUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(
    other: KElectricFieldStrengthUnitInstance
): KElectricFluxUnitInstance = electricFluxInstanceOf(value * other.value)

/**
 * Divides an electric flux by the area to obtain the field strength
 * (`electric flux / area = electricFieldStrength`).
 */
operator fun KElectricFluxUnitInstance.div(
    other: KAreaUnitInstance
): KElectricFieldStrengthUnitInstance = electricFieldStrengthInstanceOf(value / other.value)

/**
 * Divides an electric flux by the field strength to obtain the area it passes through
 * (`electric flux / electricFieldStrength = area`).
 */
operator fun KElectricFluxUnitInstance.div(
    other: KElectricFieldStrengthUnitInstance
): KAreaUnitInstance = areaOf(value / other.value)
