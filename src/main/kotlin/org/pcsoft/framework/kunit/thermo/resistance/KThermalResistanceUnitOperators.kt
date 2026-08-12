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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.powerInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the decomposition of the absolute thermal resistance -
// `temperatureDifference / power` - plus its inverses. They live in the thermal resistance package because
// it may depend on temperature/power (the reverse must never happen).

/**
 * Divides a temperature difference by a heat flow to obtain a [KThermalResistanceUnitInstance]
 * (`temperatureDifference / power = thermal resistance`).
 *
 * Example:
 * ```kotlin
 * val r = KTemperatureDifference.ofKelvin(30) / (12 of watts) // 2.5 K/W
 * ```
 */
operator fun KTemperatureDifferenceUnitInstance.div(
    other: KPowerUnitInstance
): KThermalResistanceUnitInstance = thermalResistanceInstanceOf(value / other.value)

/**
 * Multiplies a thermal resistance by a heat flow to obtain the temperature difference it sustains
 * (`thermal resistance * power = temperatureDifference`).
 */
operator fun KThermalResistanceUnitInstance.times(
    other: KPowerUnitInstance
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value * other.value)

/**
 * Multiplies a heat flow by a thermal resistance to obtain the temperature difference; the commutative
 * counterpart of [KThermalResistanceUnitInstance.times].
 */
operator fun KPowerUnitInstance.times(
    other: KThermalResistanceUnitInstance
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value * other.value)

/**
 * Divides a temperature difference by a thermal resistance to obtain the heat flow it drives
 * (`temperatureDifference / thermal resistance = power`).
 */
operator fun KTemperatureDifferenceUnitInstance.div(
    other: KThermalResistanceUnitInstance
): KPowerUnitInstance = powerInstanceOf(value / other.value)
