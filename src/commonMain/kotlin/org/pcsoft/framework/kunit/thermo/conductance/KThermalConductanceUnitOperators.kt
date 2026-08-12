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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.powerInstanceOf
import org.pcsoft.framework.kunit.thermo.resistance.KThermalResistanceUnitInstance
import org.pcsoft.framework.kunit.thermo.resistance.thermalResistanceInstanceOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// Cross-group operators for the decomposition of the thermal conductance -
// `power / temperatureDifference` - plus its inverses and the reciprocal relation to the absolute thermal
// resistance. They live in the conductance package because it may depend on power/temperature/resistance
// (the reverse must never happen).

/**
 * Divides a heat flow by a temperature difference to obtain a [KThermalConductanceUnitInstance]
 * (`power / temperatureDifference = thermal conductance`).
 *
 * Example:
 * ```kotlin
 * val g = (12 of watts) / KTemperatureDifference.ofKelvin(30) // 0.4 W/K
 * ```
 */
operator fun KPowerUnitInstance.div(
    other: KTemperatureDifferenceUnitInstance
): KThermalConductanceUnitInstance = thermalConductanceInstanceOf(value / other.value)

/**
 * Multiplies a thermal conductance by a temperature difference to obtain the heat flow it carries
 * (`thermal conductance * temperatureDifference = power`).
 */
operator fun KThermalConductanceUnitInstance.times(
    other: KTemperatureDifferenceUnitInstance
): KPowerUnitInstance = powerInstanceOf(value * other.value)

/**
 * Multiplies a temperature difference by a thermal conductance to obtain the heat flow; the commutative
 * counterpart of [KThermalConductanceUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(
    other: KThermalConductanceUnitInstance
): KPowerUnitInstance = powerInstanceOf(value * other.value)

/**
 * Divides a heat flow by a thermal conductance to obtain the temperature difference it needs
 * (`power / thermal conductance = temperatureDifference`).
 */
operator fun KPowerUnitInstance.div(
    other: KThermalConductanceUnitInstance
): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value / other.value)

/**
 * Inverts an absolute thermal resistance into the corresponding thermal conductance (`G = 1 / R`).
 *
 * Example:
 * ```kotlin
 * val g = 1 / (2.5 of kelvinsPerWatt) // 0.4 W/K
 * ```
 */
operator fun Number.div(resistance: KThermalResistanceUnitInstance): KThermalConductanceUnitInstance =
    thermalConductanceInstanceOf(this.toDouble() / resistance.value)

/**
 * Inverts a thermal conductance into the corresponding absolute thermal resistance (`R = 1 / G`) - the
 * inverse of [Number.div] against a thermal resistance.
 */
operator fun Number.div(conductance: KThermalConductanceUnitInstance): KThermalResistanceUnitInstance =
    thermalResistanceInstanceOf(this.toDouble() / conductance.value)
