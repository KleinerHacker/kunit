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

package org.pcsoft.framework.kunit.capacitance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 capacitance templates: one property per capacitance unit on the prefix builder (e.g.
// `micro.farads` = 1e-6 F, `pico.farads` = 1e-12 F). Capacitance accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `470 of micro.farads`, `c into nano.farads`.

private fun prefixedCapacitance(builder: KPrefixBuilder, unit: KCapacitanceUnit): KCapacitanceUnitInstance =
    capacitanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed farads, e.g. `micro.farads` = 1e-6 F, `pico.farads` = 1e-12 F. */
val KPrefixBuilder.farads: KCapacitanceUnitInstance get() = prefixedCapacitance(this, KCapacitanceUnit.FARAD)

/** Prefixed abfarads, e.g. `nano.abfarads`. */
val KPrefixBuilder.abfarads: KCapacitanceUnitInstance get() = prefixedCapacitance(this, KCapacitanceUnit.ABFARAD)

/** Prefixed statfarads, e.g. `kilo.statfarads`. */
val KPrefixBuilder.statfarads: KCapacitanceUnitInstance get() = prefixedCapacitance(this, KCapacitanceUnit.STATFARAD)

/** Prefixed jars, e.g. `kilo.jars`. */
val KPrefixBuilder.jars: KCapacitanceUnitInstance get() = prefixedCapacitance(this, KCapacitanceUnit.JAR)
