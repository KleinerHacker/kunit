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

package org.pcsoft.framework.kunit.thermo.molarmass

// Value-1 molar mass templates for the named units of the group, used with `of`/`into`
// (`55.845 of gramsPerMole`, `m into kilogramsPerMole`). Prefixed forms live in
// KMolarMassUnitExtensions.kt.

/** 1 gram per mole ([KMolarMassUnit.GRAM_PER_MOLE]), the group's base unit. */
val gramsPerMole: KMolarMassUnitInstance = molarMassOfUnit(KMolarMassUnit.GRAM_PER_MOLE)

/** 1 kilogram per mole ([KMolarMassUnit.KILOGRAM_PER_MOLE], 1000 g/mol). */
val kilogramsPerMole: KMolarMassUnitInstance = molarMassOfUnit(KMolarMassUnit.KILOGRAM_PER_MOLE)

/** 1 pound per pound-mole ([KMolarMassUnit.POUND_PER_POUND_MOLE], numerically 1 g/mol). */
val poundsPerPoundMole: KMolarMassUnitInstance = molarMassOfUnit(KMolarMassUnit.POUND_PER_POUND_MOLE)

/** 1 dalton per elementary entity ([KMolarMassUnit.DALTON_PER_ENTITY], ≈ 1 g/mol). */
val daltonsPerEntity: KMolarMassUnitInstance = molarMassOfUnit(KMolarMassUnit.DALTON_PER_ENTITY)
