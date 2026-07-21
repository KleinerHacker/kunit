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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 pressure templates: one property per named pressure unit on the prefix builder.
// This is how the hectopascal (`hecto.pascals`), kilopascal (`kilo.pascals`) and megapascal
// (`mega.pascals` = N/mm²) are expressed. Use with `of`/`into`, e.g. `1013 of hecto.pascals`.

private fun prefixedPressure(builder: KPrefixBuilder, unit: KPressureUnit): KPressureUnitInstance =
    pressureUnitInstanceOf(builder.prefix.factor * unit.baseValue * PA_IN_BASE)

/** Prefixed pascals, e.g. `hecto.pascals` (hPa), `kilo.pascals` (kPa), `mega.pascals` (MPa = N/mm²). */
val KPrefixBuilder.pascals: KPressureUnitInstance get() = prefixedPressure(this, KPressureUnit.PASCAL)

/** Prefixed bars, e.g. `milli.bars` (mbar), `hecto.bars`. */
val KPrefixBuilder.bars: KPressureUnitInstance get() = prefixedPressure(this, KPressureUnit.BAR)
