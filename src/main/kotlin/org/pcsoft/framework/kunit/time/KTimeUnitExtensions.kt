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

package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 time templates: one property per time unit on the prefix builder (e.g.
// `milli.seconds` = 0.001 s, `micro.seconds` = 1e-6 s). Time accepts *any* magnitude, so the properties
// hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `10 of milli.seconds`,
// `v into micro.seconds`, or as a rate denominator `meters / milli.seconds`.

private fun prefixedTime(builder: KPrefixBuilder, unit: KTimeUnit): KTimeUnitInstance =
    timeUnitInstanceOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

/** Prefixed seconds, e.g. `milli.seconds` = 0.001 s, `kilo.seconds` = 1000 s. */
val KPrefixBuilder.seconds: KTimeUnitInstance get() = prefixedTime(this, KTimeUnit.SECOND)

/** Prefixed minutes, e.g. `kilo.minutes`. */
val KPrefixBuilder.minutes: KTimeUnitInstance get() = prefixedTime(this, KTimeUnit.MINUTE)

/** Prefixed hours, e.g. `kilo.hours`. */
val KPrefixBuilder.hours: KTimeUnitInstance get() = prefixedTime(this, KTimeUnit.HOUR)

/** Prefixed days, e.g. `kilo.days`. */
val KPrefixBuilder.days: KTimeUnitInstance get() = prefixedTime(this, KTimeUnit.DAY)
