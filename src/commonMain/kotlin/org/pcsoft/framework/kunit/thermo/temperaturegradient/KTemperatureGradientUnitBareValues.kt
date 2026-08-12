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

// Value-1 temperature gradient templates for the named units of the group, used with `of`/`into`
// (`25 of kelvinPerKilometer`, `g into kelvinPerMeter`).

/** 1 kelvin per meter ([KTemperatureGradientUnit.KELVIN_PER_METER]), the group's base unit. */
val kelvinPerMeter: KTemperatureGradientUnitInstance =
    temperatureGradientOfUnit(KTemperatureGradientUnit.KELVIN_PER_METER)

/** 1 kelvin per kilometer ([KTemperatureGradientUnit.KELVIN_PER_KILOMETER], 0.001 K/m). */
val kelvinPerKilometer: KTemperatureGradientUnitInstance =
    temperatureGradientOfUnit(KTemperatureGradientUnit.KELVIN_PER_KILOMETER)

/** 1 degree Fahrenheit per foot ([KTemperatureGradientUnit.FAHRENHEIT_PER_FOOT], ≈ 1.8227 K/m). */
val fahrenheitPerFoot: KTemperatureGradientUnitInstance =
    temperatureGradientOfUnit(KTemperatureGradientUnit.FAHRENHEIT_PER_FOOT)
