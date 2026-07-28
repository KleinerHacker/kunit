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

package org.pcsoft.framework.kunit.reluctance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 reluctance templates: one property per unit on the prefix builder (e.g.
// `mega.amperesPerWeber` = 1e6 A/Wb). Reluctance accepts *any* magnitude, so the properties hang on the
// common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of mega.amperesPerWeber`,
// `rm into kilo.amperesPerWeber`.

private fun prefixedReluctance(builder: KPrefixBuilder, unit: KReluctanceUnit): KReluctanceUnitInstance =
    reluctanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed amperes per weber, e.g. `mega.amperesPerWeber` = 1e6 A/Wb. */
val KPrefixBuilder.amperesPerWeber: KReluctanceUnitInstance
    get() = prefixedReluctance(this, KReluctanceUnit.AMPERE_PER_WEBER)

/** Prefixed inverse henries, e.g. `mega.inverseHenries` = 1e6 H⁻¹. */
val KPrefixBuilder.inverseHenries: KReluctanceUnitInstance
    get() = prefixedReluctance(this, KReluctanceUnit.INVERSE_HENRY)

/** Prefixed ampere turns per weber, e.g. `mega.ampereTurnsPerWeber` = 1e6 At/Wb. */
val KPrefixBuilder.ampereTurnsPerWeber: KReluctanceUnitInstance
    get() = prefixedReluctance(this, KReluctanceUnit.AMPERE_TURN_PER_WEBER)
