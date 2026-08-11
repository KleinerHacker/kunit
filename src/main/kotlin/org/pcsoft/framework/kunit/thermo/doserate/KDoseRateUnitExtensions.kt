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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 dose rate templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `0.1 of micro.sievertsPerHour`.

private fun prefixedDoseRate(builder: KPrefixBuilder, unit: KDoseRateUnit): KDoseRateUnitInstance =
    doseRateInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed grays per second, e.g. `milli.graysPerSecond`. */
val KPrefixBuilder.graysPerSecond: KDoseRateUnitInstance
    get() = prefixedDoseRate(this, KDoseRateUnit.GRAY_PER_SECOND)

/** Prefixed grays per hour, e.g. `milli.graysPerHour`. */
val KPrefixBuilder.graysPerHour: KDoseRateUnitInstance
    get() = prefixedDoseRate(this, KDoseRateUnit.GRAY_PER_HOUR)

/** Prefixed sieverts per second, e.g. `micro.sievertsPerSecond`. */
val KPrefixBuilder.sievertsPerSecond: KDoseRateUnitInstance
    get() = prefixedDoseRate(this, KDoseRateUnit.SIEVERT_PER_SECOND)

/** Prefixed sieverts per hour, e.g. `micro.sievertsPerHour` (the usual dose-rate meter reading). */
val KPrefixBuilder.sievertsPerHour: KDoseRateUnitInstance
    get() = prefixedDoseRate(this, KDoseRateUnit.SIEVERT_PER_HOUR)
