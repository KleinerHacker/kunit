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

// Bare, value-1 voltage tokens (each = 1 unit, normalized to volts). Vocabulary for building
// (`10 of volts`) and reading (`v into statvolts`); combine with the prefix builders (`kilo.volts`).
// Prefixed forms live in KVoltageUnitExtensions.kt.

/** 1 volt ([KVoltageUnit.VOLT]). */
val volts: KVoltageUnitInstance = voltageInstanceOf(KVoltageUnit.VOLT.baseValue)

/** 1 statvolt ([KVoltageUnit.STATVOLT], CGS-ESU, 299.792458 V). */
val statvolts: KVoltageUnitInstance = voltageInstanceOf(KVoltageUnit.STATVOLT.baseValue)

/** 1 abvolt ([KVoltageUnit.ABVOLT], CGS-EMU, 10⁻⁸ V). */
val abvolts: KVoltageUnitInstance = voltageInstanceOf(KVoltageUnit.ABVOLT.baseValue)

/** 1 Weston standard cell ([KVoltageUnit.WESTON_CELL], 1.0183 V). */
val westonCells: KVoltageUnitInstance = voltageInstanceOf(KVoltageUnit.WESTON_CELL.baseValue)

/** 1 Daniell cell ([KVoltageUnit.DANIELL], 1.1 V). */
val daniells: KVoltageUnitInstance = voltageInstanceOf(KVoltageUnit.DANIELL.baseValue)
