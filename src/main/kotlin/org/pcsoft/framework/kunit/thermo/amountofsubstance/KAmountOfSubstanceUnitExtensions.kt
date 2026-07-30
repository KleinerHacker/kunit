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

package org.pcsoft.framework.kunit.thermo.amountofsubstance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 amount-of-substance templates: one property per named unit on the prefix builder.
// Amount of substance accepts *any* magnitude, so both the augmenting and the diminishing builders are
// served by the common KPrefixBuilder base. Use with `of`/`into`, e.g. `250 of milli.moles`.

private fun prefixedAmountOfSubstance(builder: KPrefixBuilder, unit: KAmountOfSubstanceUnit): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed moles, e.g. `milli.moles` (mmol), `micro.moles` (µmol), `kilo.moles` (kmol). */
val KPrefixBuilder.moles: KAmountOfSubstanceUnitInstance
    get() = prefixedAmountOfSubstance(this, KAmountOfSubstanceUnit.MOLE)

/** Prefixed pound-moles, e.g. `kilo.poundMoles`. */
val KPrefixBuilder.poundMoles: KAmountOfSubstanceUnitInstance
    get() = prefixedAmountOfSubstance(this, KAmountOfSubstanceUnit.POUND_MOLE)
