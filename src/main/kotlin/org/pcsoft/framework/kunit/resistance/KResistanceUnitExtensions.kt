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

package org.pcsoft.framework.kunit.resistance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 resistance templates: one property per resistance unit on the prefix builder (e.g.
// `kilo.ohms` = 1000 Ω, `milli.ohms` = 0.001 Ω). Resistance accepts *any* magnitude, so the properties
// hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of kilo.ohms`,
// `r into milli.ohms`.

private fun prefixedResistance(builder: KPrefixBuilder, unit: KResistanceUnit): KResistanceUnitInstance =
    resistanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed ohms, e.g. `kilo.ohms` = 1000 Ω, `milli.ohms` = 0.001 Ω. */
val KPrefixBuilder.ohms: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.OHM)

/** Prefixed statohms, e.g. `kilo.statohms`. */
val KPrefixBuilder.statohms: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.STATOHM)

/** Prefixed abohms, e.g. `kilo.abohms`. */
val KPrefixBuilder.abohms: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.ABOHM)

/** Prefixed international ohms, e.g. `milli.internationalOhms`. */
val KPrefixBuilder.internationalOhms: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.INTERNATIONAL_OHM)

/** Prefixed legal ohms, e.g. `milli.legalOhms`. */
val KPrefixBuilder.legalOhms: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.LEGAL_OHM)

/** Prefixed Siemens units, e.g. `milli.siemensUnits`. */
val KPrefixBuilder.siemensUnits: KResistanceUnitInstance get() = prefixedResistance(this, KResistanceUnit.SIEMENS_UNIT)
