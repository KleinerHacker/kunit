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

package org.pcsoft.framework.kunit.voltage

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 voltage templates: one property per voltage unit on the prefix builder (e.g.
// `kilo.volts` = 1000 V, `milli.volts` = 0.001 V). Voltage accepts *any* magnitude, so the properties
// hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of kilo.volts`,
// `v into milli.volts`.

private fun prefixedVoltage(builder: KPrefixBuilder, unit: KVoltageUnit): KVoltageUnitInstance =
    voltageInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed volts, e.g. `kilo.volts` = 1000 V, `milli.volts` = 0.001 V. */
val KPrefixBuilder.volts: KVoltageUnitInstance get() = prefixedVoltage(this, KVoltageUnit.VOLT)

/** Prefixed statvolts, e.g. `kilo.statvolts`. */
val KPrefixBuilder.statvolts: KVoltageUnitInstance get() = prefixedVoltage(this, KVoltageUnit.STATVOLT)

/** Prefixed abvolts, e.g. `kilo.abvolts`. */
val KPrefixBuilder.abvolts: KVoltageUnitInstance get() = prefixedVoltage(this, KVoltageUnit.ABVOLT)

/** Prefixed Weston cells, e.g. `milli.westonCells`. */
val KPrefixBuilder.westonCells: KVoltageUnitInstance get() = prefixedVoltage(this, KVoltageUnit.WESTON_CELL)

/** Prefixed Daniell cells, e.g. `milli.daniells`. */
val KPrefixBuilder.daniells: KVoltageUnitInstance get() = prefixedVoltage(this, KVoltageUnit.DANIELL)
