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

package org.pcsoft.framework.kunit.ec

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 electric current templates: one property per current unit on the prefix builder
// (e.g. `milli.amperes` = 0.001 A, `kilo.amperes` = 1000 A). Electric current accepts *any* magnitude,
// so the properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `2 of milli.amperes`, `v into kilo.amperes`.

private fun prefixedElectricCurrent(builder: KPrefixBuilder, unit: KElectricCurrentUnit): KElectricCurrentUnitInstance =
    electricCurrentOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

// --- SI ------------------------------------------------------------------------------------------

/** Prefixed amperes, e.g. `milli.amperes` = 0.001 A, `kilo.amperes` = 1000 A. */
val KPrefixBuilder.amperes: KElectricCurrentUnitInstance get() = prefixedElectricCurrent(this, KElectricCurrentUnit.AMPERE)

// --- CGS -----------------------------------------------------------------------------------------

/** Prefixed biot / abampere, e.g. `milli.biot`. */
val KPrefixBuilder.biot: KElectricCurrentUnitInstance get() = prefixedElectricCurrent(this, KElectricCurrentUnit.BIOT)

/** Prefixed abamperes (alias spelling of the biot), e.g. `milli.abamperes`. */
val KPrefixBuilder.abamperes: KElectricCurrentUnitInstance get() = prefixedElectricCurrent(this, KElectricCurrentUnit.BIOT)

/** Prefixed statamperes, e.g. `kilo.statamperes`. */
val KPrefixBuilder.statamperes: KElectricCurrentUnitInstance get() = prefixedElectricCurrent(this, KElectricCurrentUnit.STATAMPERE)
