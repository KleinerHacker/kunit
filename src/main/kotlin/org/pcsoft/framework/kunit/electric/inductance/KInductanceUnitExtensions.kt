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

package org.pcsoft.framework.kunit.electric.inductance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 inductance templates: one property per inductance unit on the prefix builder (e.g.
// `milli.henries` = mH, `micro.henries` = µH). Inductance accepts *any* magnitude, so the properties hang
// on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `470 of micro.henries`,
// `l into milli.henries`.

private fun prefixedInductance(builder: KPrefixBuilder, unit: KInductanceUnit): KInductanceUnitInstance =
    inductanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed henries, e.g. `milli.henries` = 0.001 H, `micro.henries` = 1e-6 H. */
val KPrefixBuilder.henries: KInductanceUnitInstance get() = prefixedInductance(this, KInductanceUnit.HENRY)

/** Prefixed webers per ampere, e.g. `milli.webersPerAmpere`. */
val KPrefixBuilder.webersPerAmpere: KInductanceUnitInstance get() = prefixedInductance(this, KInductanceUnit.WEBER_PER_AMPERE)

/** Prefixed abhenries, e.g. `kilo.abhenries`. */
val KPrefixBuilder.abhenries: KInductanceUnitInstance get() = prefixedInductance(this, KInductanceUnit.ABHENRY)

/** Prefixed stathenries, e.g. `micro.stathenries`. */
val KPrefixBuilder.stathenries: KInductanceUnitInstance get() = prefixedInductance(this, KInductanceUnit.STATHENRY)
