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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.temperatureDifferenceOf

// The thermal expansion coefficient is the reciprocal of a temperature difference, so its operators are
// the reciprocal pair plus the "relative change" product. The product is deliberately a plain Double: a
// relative length change is dimensionless, and as a Double it composes directly with the scalar
// `length * factor` operator (`beam * (steel * rise)`).

/**
 * Inverts a temperature difference into a [KThermalExpansionUnitInstance] (`α = 1 / ΔT`), returning a
 * **typed** result. This narrower overload wins over the group-agnostic `Number.div(KUnitMeasurable)`,
 * which would only produce a raw mixed unit.
 *
 * Example:
 * ```kotlin
 * val alpha = 1 / KTemperatureDifference.ofKelvin(1) // 1.0 1/K
 * ```
 */
operator fun Number.div(unit: KTemperatureDifferenceUnitInstance): KThermalExpansionUnitInstance =
    thermalExpansionInstanceOf(this.toDouble() / unit.value)

/**
 * Inverts a thermal expansion coefficient into the temperature difference that would double the quantity
 * (`ΔT = 1 / α`) - the counterpart of the [Number.div] above.
 */
operator fun Number.div(unit: KThermalExpansionUnitInstance): KTemperatureDifferenceUnitInstance =
    temperatureDifferenceOf(this.toDouble() / unit.value)

/**
 * Multiplies a thermal expansion coefficient by a temperature difference to obtain the **relative** change
 * (`α · ΔT`), e.g. `1.2e-5 · 50 K = 6e-4` - six parts in ten thousand. The result is a plain, dimensionless
 * [Double]. To turn it into an absolute length change use [elongationOf], which is independent of import
 * order (an explicitly imported generic `times` would shadow this operator).
 */
operator fun KThermalExpansionUnitInstance.times(other: KTemperatureDifferenceUnitInstance): Double =
    value * other.value

/**
 * Multiplies a temperature difference by a thermal expansion coefficient to obtain the relative change; the
 * commutative counterpart of [KThermalExpansionUnitInstance.times].
 */
operator fun KTemperatureDifferenceUnitInstance.times(other: KThermalExpansionUnitInstance): Double =
    value * other.value

/**
 * The **absolute** length change of [length] when its temperature changes by [difference]
 * (`Δl = α · l · ΔT`), returned as a typed [KLengthUnitInstance].
 *
 * This is the named counterpart of the dimensionless [times] product. It is a plain function rather than
 * an operator precisely so that it cannot be shadowed by an explicitly imported generic `times`.
 *
 * Example:
 * ```kotlin
 * val steel = 12 of ppmPerKelvin
 * val growth = steel.elongationOf(10 of meters, KTemperatureDifference.ofKelvin(50))
 * growth into milli.meters // 6.0 - a 10 m steel beam grows 6 mm over 50 K
 * ```
 */
fun KThermalExpansionUnitInstance.elongationOf(
    length: KLengthUnitInstance,
    difference: KTemperatureDifferenceUnitInstance,
): KLengthUnitInstance = lengthOf(value * length.value * difference.value)
