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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molar mass templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `18 of milli.kilogramsPerMole`.

private fun prefixedMolarMass(builder: KPrefixBuilder, unit: KMolarMassUnit): KMolarMassUnitInstance =
    molarMassInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed grams per mole, e.g. `kilo.gramsPerMole` (kg/mol), `milli.gramsPerMole` (mg/mol). */
val KPrefixBuilder.gramsPerMole: KMolarMassUnitInstance
    get() = prefixedMolarMass(this, KMolarMassUnit.GRAM_PER_MOLE)

/** Prefixed kilograms per mole, e.g. `milli.kilogramsPerMole` (= g/mol). */
val KPrefixBuilder.kilogramsPerMole: KMolarMassUnitInstance
    get() = prefixedMolarMass(this, KMolarMassUnit.KILOGRAM_PER_MOLE)

/** Prefixed pounds per pound-mole, e.g. `kilo.poundsPerPoundMole`. */
val KPrefixBuilder.poundsPerPoundMole: KMolarMassUnitInstance
    get() = prefixedMolarMass(this, KMolarMassUnit.POUND_PER_POUND_MOLE)

/** Prefixed daltons per entity, e.g. `kilo.daltonsPerEntity` (kDa per particle). */
val KPrefixBuilder.daltonsPerEntity: KMolarMassUnitInstance
    get() = prefixedMolarMass(this, KMolarMassUnit.DALTON_PER_ENTITY)
