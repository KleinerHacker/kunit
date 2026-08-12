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

package org.pcsoft.framework.kunit.thermo.heatcapacity

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.energyInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators that let energy and a temperature difference combine *directly* into a strongly
// typed heat capacity, and back. They live in the heat capacity package because it may depend on
// energy/temperature (the reverse must never happen). Each computes on the already normalized values
// (J, K, J/K), which is exact and needs no dimension bridge.

/**
 * Divides an energy by a temperature difference to obtain a [KHeatCapacityUnitInstance]
 * (`energy / temperatureDifference = heat capacity`).
 *
 * Example:
 * ```kotlin
 * val c = (4184 of joules) / KTemperatureDifference.ofKelvin(1) // 4184 J/K
 * ```
 */
operator fun KEnergyUnitInstance.div(other: KTemperatureDifferenceUnitInstance): KHeatCapacityUnitInstance =
    heatCapacityInstanceOf(value / other.value)

/**
 * Multiplies a heat capacity by a temperature difference to obtain the energy required
 * (`heat capacity * temperatureDifference = energy`).
 */
operator fun KHeatCapacityUnitInstance.times(other: KTemperatureDifferenceUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * other.value)

/**
 * Multiplies a temperature difference by a heat capacity to obtain the energy; the commutative
 * counterpart of [KHeatCapacityUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(other: KHeatCapacityUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * other.value)

/**
 * Divides an energy by a heat capacity to obtain the resulting temperature difference
 * (`energy / heat capacity = temperatureDifference`).
 */
operator fun KEnergyUnitInstance.div(other: KHeatCapacityUnitInstance): KTemperatureDifferenceUnitInstance =
    temperatureDifferenceOf(value / other.value)
