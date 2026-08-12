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

package org.pcsoft.framework.kunit.electric.conductivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 conductivity templates: one property per conductivity unit on the prefix builder (e.g.
// `mega.siemensPerMeter` = 1e6 S/m, `milli.siemensPerMeter` = 1e-3 S/m). Conductivity accepts *any*
// magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `58 of mega.siemensPerMeter`, `sigma into milli.siemensPerMeter`.

private fun prefixedConductivity(builder: KPrefixBuilder, unit: KConductivityUnit): KConductivityUnitInstance =
    conductivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed siemens per meter, e.g. `mega.siemensPerMeter` = 1e6 S/m (copper ≈ 58 MS/m). */
val KPrefixBuilder.siemensPerMeter: KConductivityUnitInstance
    get() = prefixedConductivity(this, KConductivityUnit.SIEMENS_PER_METER)

/** Prefixed siemens per centimeter, e.g. `milli.siemensPerCentimeter`. */
val KPrefixBuilder.siemensPerCentimeter: KConductivityUnitInstance
    get() = prefixedConductivity(this, KConductivityUnit.SIEMENS_PER_CENTIMETER)

/** Prefixed microsiemens per centimeter, e.g. `kilo.microsiemensPerCentimeter`. */
val KPrefixBuilder.microsiemensPerCentimeter: KConductivityUnitInstance
    get() = prefixedConductivity(this, KConductivityUnit.MICROSIEMENS_PER_CENTIMETER)

/** Prefixed megasiemens per meter, e.g. `milli.megasiemensPerMeter`. */
val KPrefixBuilder.megasiemensPerMeter: KConductivityUnitInstance
    get() = prefixedConductivity(this, KConductivityUnit.MEGASIEMENS_PER_METER)
