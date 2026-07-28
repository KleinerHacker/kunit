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

package org.pcsoft.framework.kunit.electric.resistivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 resistivity templates: one property per resistivity unit on the prefix builder (e.g.
// `nano.ohmMeters` = 1e-9 Ω·m, `micro.ohmMeters` = 1e-6 Ω·m). Resistivity accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `17 of nano.ohmMeters`,
// `rho into micro.ohmMeters`.

private fun prefixedResistivity(builder: KPrefixBuilder, unit: KResistivityUnit): KResistivityUnitInstance =
    resistivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed ohm meters, e.g. `nano.ohmMeters` = 1e-9 Ω·m (copper ≈ 17 nΩ·m). */
val KPrefixBuilder.ohmMeters: KResistivityUnitInstance get() = prefixedResistivity(this, KResistivityUnit.OHM_METER)

/** Prefixed ohm centimeters, e.g. `milli.ohmCentimeters`. */
val KPrefixBuilder.ohmCentimeters: KResistivityUnitInstance
    get() = prefixedResistivity(this, KResistivityUnit.OHM_CENTIMETER)

/** Prefixed statohm centimeters, e.g. `micro.statohmCentimeters`. */
val KPrefixBuilder.statohmCentimeters: KResistivityUnitInstance
    get() = prefixedResistivity(this, KResistivityUnit.STATOHM_CENTIMETER)
