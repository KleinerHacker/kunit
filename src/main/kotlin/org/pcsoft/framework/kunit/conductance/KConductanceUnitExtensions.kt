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

package org.pcsoft.framework.kunit.conductance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 conductance templates: one property per conductance unit on the prefix builder (e.g.
// `milli.siemens` = 0.001 S, `kilo.siemens` = 1000 S). Conductance accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of milli.siemens`,
// `g into micro.siemens`.

private fun prefixedConductance(builder: KPrefixBuilder, unit: KConductanceUnit): KConductanceUnitInstance =
    conductanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed siemens, e.g. `milli.siemens` = 0.001 S, `kilo.siemens` = 1000 S. */
val KPrefixBuilder.siemens: KConductanceUnitInstance get() = prefixedConductance(this, KConductanceUnit.SIEMENS)

/** Prefixed mhos, e.g. `milli.mhos`. */
val KPrefixBuilder.mhos: KConductanceUnitInstance get() = prefixedConductance(this, KConductanceUnit.MHO)

/** Prefixed abmhos, e.g. `milli.abmhos`. */
val KPrefixBuilder.abmhos: KConductanceUnitInstance get() = prefixedConductance(this, KConductanceUnit.ABMHO)

/** Prefixed statmhos, e.g. `kilo.statmhos`. */
val KPrefixBuilder.statmhos: KConductanceUnitInstance get() = prefixedConductance(this, KConductanceUnit.STATMHO)
