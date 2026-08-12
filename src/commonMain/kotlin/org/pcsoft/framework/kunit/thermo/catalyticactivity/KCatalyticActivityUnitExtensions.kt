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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 catalytic activity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `5 of micro.katals`.

private fun prefixedCatalyticActivity(
    builder: KPrefixBuilder,
    unit: KCatalyticActivityUnit
): KCatalyticActivityUnitInstance =
    catalyticActivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed katals, e.g. `micro.katals` (µkat), `nano.katals`. */
val KPrefixBuilder.katals: KCatalyticActivityUnitInstance
    get() = prefixedCatalyticActivity(this, KCatalyticActivityUnit.KATAL)

/** Prefixed enzyme units, e.g. `kilo.enzymeUnits`, `milli.enzymeUnits`. */
val KPrefixBuilder.enzymeUnits: KCatalyticActivityUnitInstance
    get() = prefixedCatalyticActivity(this, KCatalyticActivityUnit.ENZYME_UNIT)
