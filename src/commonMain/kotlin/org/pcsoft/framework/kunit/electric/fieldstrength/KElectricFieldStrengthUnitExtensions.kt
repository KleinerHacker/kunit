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

package org.pcsoft.framework.kunit.electric.fieldstrength

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 electric field strength templates: one property per unit on the prefix builder (e.g.
// `kilo.voltsPerMeter` = 1000 V/m, `mega.voltsPerMeter` = 1e6 V/m). Electric field strength accepts *any*
// magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `2 of kilo.voltsPerMeter`, `e into mega.voltsPerMeter`.

private fun prefixedElectricFieldStrength(
    builder: KPrefixBuilder,
    unit: KElectricFieldStrengthUnit,
): KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed volts per meter, e.g. `kilo.voltsPerMeter` = 1000 V/m. */
val KPrefixBuilder.voltsPerMeter: KElectricFieldStrengthUnitInstance
    get() = prefixedElectricFieldStrength(this, KElectricFieldStrengthUnit.VOLT_PER_METER)

/** Prefixed volts per centimeter, e.g. `kilo.voltsPerCentimeter` = 1e5 V/m. */
val KPrefixBuilder.voltsPerCentimeter: KElectricFieldStrengthUnitInstance
    get() = prefixedElectricFieldStrength(this, KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER)

/** Prefixed statvolts per centimeter, e.g. `milli.statvoltsPerCentimeter`. */
val KPrefixBuilder.statvoltsPerCentimeter: KElectricFieldStrengthUnitInstance
    get() = prefixedElectricFieldStrength(this, KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER)
