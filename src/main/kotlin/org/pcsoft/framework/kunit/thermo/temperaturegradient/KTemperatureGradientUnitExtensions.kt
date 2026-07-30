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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 temperature gradient templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `25 of milli.kelvinPerMeter`.

private fun prefixedTemperatureGradient(
    builder: KPrefixBuilder,
    unit: KTemperatureGradientUnit,
): KTemperatureGradientUnitInstance = temperatureGradientInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed kelvin per meter, e.g. `milli.kelvinPerMeter` (mK/m), `kilo.kelvinPerMeter`. */
val KPrefixBuilder.kelvinPerMeter: KTemperatureGradientUnitInstance
    get() = prefixedTemperatureGradient(this, KTemperatureGradientUnit.KELVIN_PER_METER)

/** Prefixed kelvin per kilometer, e.g. `milli.kelvinPerKilometer`. */
val KPrefixBuilder.kelvinPerKilometer: KTemperatureGradientUnitInstance
    get() = prefixedTemperatureGradient(this, KTemperatureGradientUnit.KELVIN_PER_KILOMETER)

/** Prefixed degrees Fahrenheit per foot, e.g. `milli.fahrenheitPerFoot`. */
val KPrefixBuilder.fahrenheitPerFoot: KTemperatureGradientUnitInstance
    get() = prefixedTemperatureGradient(this, KTemperatureGradientUnit.FAHRENHEIT_PER_FOOT)
