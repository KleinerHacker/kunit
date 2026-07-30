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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 volumetric flow templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `250 of milli.litersPerSecond`.

private fun prefixedVolumeFlow(builder: KPrefixBuilder, unit: KVolumeFlowUnit): KVolumeFlowUnitInstance =
    volumeFlowInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed cubic meters per second, e.g. `milli.cubicMetersPerSecond`, `kilo.cubicMetersPerSecond`. */
val KPrefixBuilder.cubicMetersPerSecond: KVolumeFlowUnitInstance
    get() = prefixedVolumeFlow(this, KVolumeFlowUnit.CUBIC_METER_PER_SECOND)

/** Prefixed cubic meters per hour, e.g. `kilo.cubicMetersPerHour`. */
val KPrefixBuilder.cubicMetersPerHour: KVolumeFlowUnitInstance
    get() = prefixedVolumeFlow(this, KVolumeFlowUnit.CUBIC_METER_PER_HOUR)

/** Prefixed liters per second, e.g. `milli.litersPerSecond`, `hecto.litersPerSecond`. */
val KPrefixBuilder.litersPerSecond: KVolumeFlowUnitInstance
    get() = prefixedVolumeFlow(this, KVolumeFlowUnit.LITER_PER_SECOND)

/** Prefixed liters per minute, e.g. `milli.litersPerMinute`. */
val KPrefixBuilder.litersPerMinute: KVolumeFlowUnitInstance
    get() = prefixedVolumeFlow(this, KVolumeFlowUnit.LITER_PER_MINUTE)

/** Prefixed US gallons per minute, e.g. `kilo.usGallonsPerMinute`. */
val KPrefixBuilder.usGallonsPerMinute: KVolumeFlowUnitInstance
    get() = prefixedVolumeFlow(this, KVolumeFlowUnit.US_GALLON_PER_MINUTE)
