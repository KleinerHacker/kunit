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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 luminous energy templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `2 of kilo.lumenSeconds`.

private fun prefixedLuminousEnergy(
    builder: KPrefixBuilder,
    unit: KLuminousEnergyUnit
): KLuminousEnergyUnitInstance =
    luminousEnergyInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed lumen seconds, e.g. `kilo.lumenSeconds`, `milli.lumenSeconds`. */
val KPrefixBuilder.lumenSeconds: KLuminousEnergyUnitInstance
    get() = prefixedLuminousEnergy(this, KLuminousEnergyUnit.LUMEN_SECOND)

/** Prefixed talbots, e.g. `milli.talbots` - the classical spelling of `milli.lumenSeconds`. */
val KPrefixBuilder.talbots: KLuminousEnergyUnitInstance
    get() = prefixedLuminousEnergy(this, KLuminousEnergyUnit.LUMEN_SECOND)

/** Prefixed lumen hours, e.g. `kilo.lumenHours`. */
val KPrefixBuilder.lumenHours: KLuminousEnergyUnitInstance
    get() = prefixedLuminousEnergy(this, KLuminousEnergyUnit.LUMEN_HOUR)
