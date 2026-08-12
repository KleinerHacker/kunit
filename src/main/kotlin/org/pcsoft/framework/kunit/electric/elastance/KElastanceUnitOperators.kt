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

package org.pcsoft.framework.kunit.electric.elastance

import org.pcsoft.framework.kunit.electric.capacitance.KCapacitanceUnitInstance
import org.pcsoft.framework.kunit.electric.capacitance.capacitanceInstanceOf
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.voltageInstanceOf

// Cross-group operators for the decomposition of the elastance - `voltage / charge` - plus its inverses
// and the reciprocal relation to the capacitance. They live in the elastance package because it may depend
// on voltage/charge/capacitance (the reverse must never happen).

/**
 * Divides a voltage by a charge to obtain a [KElastanceUnitInstance] (`voltage / charge = elastance`).
 *
 * Example:
 * ```kotlin
 * val s = (10 of volts) / (10 of milli.coulombs) // 1000 F⁻¹
 * ```
 */
operator fun KVoltageUnitInstance.div(other: KChargeUnitInstance): KElastanceUnitInstance =
    elastanceInstanceOf(value / other.value)

/**
 * Multiplies an elastance by a charge to obtain the voltage it develops
 * (`elastance * charge = voltage`).
 */
operator fun KElastanceUnitInstance.times(other: KChargeUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * other.value)

/**
 * Multiplies a charge by an elastance to obtain the voltage; the commutative counterpart of
 * [KElastanceUnitInstance.times].
 */
operator fun KChargeUnitInstance.times(other: KElastanceUnitInstance): KVoltageUnitInstance =
    voltageInstanceOf(value * other.value)

/**
 * Divides a voltage by an elastance to obtain the stored charge (`voltage / elastance = charge`).
 */
operator fun KVoltageUnitInstance.div(other: KElastanceUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value / other.value)

/**
 * Inverts a capacitance into the corresponding elastance (`S = 1 / C`).
 *
 * Example:
 * ```kotlin
 * val s = 1 / (1 of milli.farads) // 1000 F⁻¹
 * ```
 */
operator fun Number.div(capacitance: KCapacitanceUnitInstance): KElastanceUnitInstance =
    elastanceInstanceOf(this.toDouble() / capacitance.value)

/**
 * Inverts an elastance into the corresponding capacitance (`C = 1 / S`) - the inverse of [Number.div]
 * against a capacitance.
 */
operator fun Number.div(elastance: KElastanceUnitInstance): KCapacitanceUnitInstance =
    capacitanceInstanceOf(this.toDouble() / elastance.value)
