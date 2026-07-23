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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare temperature tokens: value-1 kelvin-term templates that additionally carry their construction
// unit for the affine `of`/`into` overloads. Vocabulary for building (`25 of celsius`) and reading
// (`t into fahrenheit`). The temperature group has no prefixes, so there are no prefixed forms.
//
// Each token carries a cosmetic [KUnitDisplay] so that `format` (which first converts the value into the
// target unit) can render `"°C"`/`"°F"`; this display never affects the kelvin-based `toString`, and it
// is deliberately not propagated to constructed values (see KTemperatureUnitInstance.scaledBy).

// Each token records its own unit as display metadata so `format` renders the target unit's symbol.
private fun bareTemperature(unit: KTemperatureUnit): KTemperatureUnitInstance =
    temperatureOf(unit.baseValue, unit, KUnitDisplay(unit))

/** Kelvin token ([KTemperatureUnit.KELVIN]); base unit, identity affine transform. */
val kelvin: KTemperatureUnitInstance = bareTemperature(KTemperatureUnit.KELVIN)

/** Degree Celsius token ([KTemperatureUnit.CELSIUS]); `K = °C + 273.15`. */
val celsius: KTemperatureUnitInstance = bareTemperature(KTemperatureUnit.CELSIUS)

/** Degree Fahrenheit token ([KTemperatureUnit.FAHRENHEIT]); `K = (°F − 32) · 5/9 + 273.15`. */
val fahrenheit: KTemperatureUnitInstance = bareTemperature(KTemperatureUnit.FAHRENHEIT)

/** Degree Rankine token ([KTemperatureUnit.RANKINE]); `K = °R · 5/9`. */
val rankine: KTemperatureUnitInstance = bareTemperature(KTemperatureUnit.RANKINE)
