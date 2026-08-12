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

package org.pcsoft.framework.kunit.thermo.concentration

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 concentration templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `5 of milli.molesPerLiter`.

private fun prefixedConcentration(
    builder: KPrefixBuilder,
    unit: KConcentrationUnit
): KConcentrationUnitInstance =
    concentrationInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed moles per cubic meter, e.g. `kilo.molesPerCubicMeter`. */
val KPrefixBuilder.molesPerCubicMeter: KConcentrationUnitInstance
    get() = prefixedConcentration(this, KConcentrationUnit.MOLES_PER_CUBIC_METER)

/** Prefixed moles per liter, e.g. `milli.molesPerLiter` (mmol/l), `micro.molesPerLiter`. */
val KPrefixBuilder.molesPerLiter: KConcentrationUnitInstance
    get() = prefixedConcentration(this, KConcentrationUnit.MOLES_PER_LITER)

/** Prefixed molar, e.g. `milli.molar` - the classical spelling of `milli.molesPerLiter`. */
val KPrefixBuilder.molar: KConcentrationUnitInstance
    get() = prefixedConcentration(this, KConcentrationUnit.MOLES_PER_LITER)

/** Prefixed millimoles per liter, e.g. `kilo.millimolesPerLiter`. */
val KPrefixBuilder.millimolesPerLiter: KConcentrationUnitInstance
    get() = prefixedConcentration(this, KConcentrationUnit.MILLIMOLES_PER_LITER)
