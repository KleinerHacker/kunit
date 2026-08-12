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

package org.pcsoft.framework.kunit.electric.mobility

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 electric mobility templates: one property per unit on the prefix builder (e.g.
// `milli.squareMetersPerVoltSecond` = 1e-3 m²/(V·s)). Electric mobility accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`.

private fun prefixedElectricMobility(
    builder: KPrefixBuilder,
    unit: KElectricMobilityUnit,
): KElectricMobilityUnitInstance = electricMobilityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed square meters per volt second, e.g. `milli.squareMetersPerVoltSecond` = 1e-3 m²/(V·s). */
val KPrefixBuilder.squareMetersPerVoltSecond: KElectricMobilityUnitInstance
    get() = prefixedElectricMobility(this, KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND)

/** Prefixed square centimeters per volt second, e.g. `kilo.squareCentimetersPerVoltSecond` = 0.1 m²/(V·s). */
val KPrefixBuilder.squareCentimetersPerVoltSecond: KElectricMobilityUnitInstance
    get() = prefixedElectricMobility(this, KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND)
