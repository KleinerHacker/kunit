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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 strain templates: one property per named unit on the prefix builder (e.g.
// `micro.ratio` = 1e-6, the microstrain). Strain accepts *any* magnitude, so the properties hang on the
// common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `5 of milli.ratio`.

private fun prefixedStrain(builder: KPrefixBuilder, unit: KStrainUnit): KStrainUnitInstance =
    strainOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

/** Prefixed plain ratio, e.g. `milli.ratio` (1e-3 = 1 ‰), `micro.ratio` (1e-6 = 1 µe). */
val KPrefixBuilder.ratio: KStrainUnitInstance get() = prefixedStrain(this, KStrainUnit.RATIO)

/** Prefixed percent, e.g. `milli.percent` (1e-5). */
val KPrefixBuilder.percent: KStrainUnitInstance get() = prefixedStrain(this, KStrainUnit.PERCENT)

/** Prefixed per mille, e.g. `milli.perMille` (1e-6). */
val KPrefixBuilder.perMille: KStrainUnitInstance get() = prefixedStrain(this, KStrainUnit.PER_MILLE)

/** Prefixed microstrain, e.g. `kilo.microstrain` (1e-3). */
val KPrefixBuilder.microstrain: KStrainUnitInstance get() = prefixedStrain(this, KStrainUnit.MICROSTRAIN)
