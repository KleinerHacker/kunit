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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 heat transfer coefficient templates: one property per named unit on the prefix
// builder. Use with `of`/`into`, e.g. `1300 of milli.wattsPerSquareMeterKelvin`.

private fun prefixedHeatTransferCoefficient(
    builder: KPrefixBuilder,
    unit: KHeatTransferCoefficientUnit,
): KHeatTransferCoefficientUnitInstance =
    heatTransferCoefficientInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per square meter-kelvin, e.g. `milli.wattsPerSquareMeterKelvin`, `kilo.wattsPerSquareMeterKelvin`. */
val KPrefixBuilder.wattsPerSquareMeterKelvin: KHeatTransferCoefficientUnitInstance
    get() = prefixedHeatTransferCoefficient(this, KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN)

/** Prefixed Btu per hour-square foot-degree Fahrenheit, e.g. `milli.btusPerHourSquareFootFahrenheit`. */
val KPrefixBuilder.btusPerHourSquareFootFahrenheit: KHeatTransferCoefficientUnitInstance
    get() = prefixedHeatTransferCoefficient(this, KHeatTransferCoefficientUnit.BTU_PER_HOUR_SQUARE_FOOT_FAHRENHEIT)

/** Prefixed calories per second-square centimeter-kelvin, e.g. `milli.caloriesPerSecondSquareCentimeterKelvin`. */
val KPrefixBuilder.caloriesPerSecondSquareCentimeterKelvin: KHeatTransferCoefficientUnitInstance
    get() = prefixedHeatTransferCoefficient(
        this,
        KHeatTransferCoefficientUnit.CALORIE_PER_SECOND_SQUARE_CENTIMETER_KELVIN,
    )
