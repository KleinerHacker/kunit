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

package org.pcsoft.framework.kunit.thermo.temperaturegradient

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators that let a temperature difference and a length combine *directly* into a
// strongly typed temperature gradient, and back. Both operand values are already normalized (K, m).

/**
 * Divides a temperature difference by a length to obtain a [KTemperatureGradientUnitInstance]
 * (`temperatureDifference / length = temperature gradient`).
 *
 * Example:
 * ```kotlin
 * val g = KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters) // 0.025 K/m
 * ```
 */
operator fun KTemperatureDifferenceUnitInstance.div(other: KLengthUnitInstance): KTemperatureGradientUnitInstance =
    temperatureGradientInstanceOf(value / other.value)

/**
 * Multiplies a temperature gradient by a length to obtain the temperature difference across it
 * (`temperature gradient * length = temperatureDifference`).
 */
operator fun KTemperatureGradientUnitInstance.times(other: KLengthUnitInstance): KTemperatureDifferenceUnitInstance =
    temperatureDifferenceOf(value * other.value)

/**
 * Multiplies a length by a temperature gradient to obtain the temperature difference; the commutative
 * counterpart of [KTemperatureGradientUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(other: KTemperatureGradientUnitInstance): KTemperatureDifferenceUnitInstance =
    temperatureDifferenceOf(value * other.value)

/**
 * Divides a temperature difference by a temperature gradient to obtain the length it spans
 * (`temperatureDifference / temperature gradient = length`).
 */
operator fun KTemperatureDifferenceUnitInstance.div(
    other: KTemperatureGradientUnitInstance,
): KLengthUnitInstance = lengthOf(value / other.value)
