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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 electric flux templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `5 of kilo.voltMeters`.

private fun prefixedElectricFlux(
    builder: KPrefixBuilder,
    unit: KElectricFluxUnit
): KElectricFluxUnitInstance = electricFluxInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed volt meters, e.g. `kilo.voltMeters`, `milli.voltMeters`. */
val KPrefixBuilder.voltMeters: KElectricFluxUnitInstance
    get() = prefixedElectricFlux(this, KElectricFluxUnit.VOLT_METER)

/** Prefixed volt centimeters, e.g. `kilo.voltCentimeters`. */
val KPrefixBuilder.voltCentimeters: KElectricFluxUnitInstance
    get() = prefixedElectricFlux(this, KElectricFluxUnit.VOLT_CENTIMETER)
