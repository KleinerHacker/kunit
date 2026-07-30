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

// Value-1 amount-of-substance templates for the named units of the group, used with `of`/`into`
// (`5 of moles`, `n into poundMoles`).

/** 1 mole ([KAmountOfSubstanceUnit.MOLE]), the group's base unit. */
val moles: KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(KAmountOfSubstanceUnit.MOLE.baseValue)

/** 1 pound-mole ([KAmountOfSubstanceUnit.POUND_MOLE], 453.59237 mol). */
val poundMoles: KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(KAmountOfSubstanceUnit.POUND_MOLE.baseValue)
