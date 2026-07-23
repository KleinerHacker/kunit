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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 mass templates: one property per mass unit on the prefix builder (e.g.
// `kilo.grams` = 1000 g, `milli.grams` = 0.001 g). Mass accepts *any* magnitude, so the properties
// hang on the common base [KPrefixBuilder]. The kilogram is exactly `kilo.grams`. Use with `of`/`into`,
// e.g. `2 of kilo.grams`, `v into milli.grams`.

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

// --- Metric / SI ---------------------------------------------------------------------------------

/** Prefixed grams, e.g. `kilo.grams` = 1000 g (the kilogram), `milli.grams` = 0.001 g. */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** Prefixed tonnes, e.g. `kilo.tonnes`. */
val KPrefixBuilder.tonnes: KMassUnitInstance get() = prefixedMass(this, KMassUnit.TONNE)

/** Prefixed carats, e.g. `milli.carats`. */
val KPrefixBuilder.carats: KMassUnitInstance get() = prefixedMass(this, KMassUnit.CARAT)

// --- Avoirdupois ---------------------------------------------------------------------------------

/** Prefixed grains, e.g. `milli.grains`. */
val KPrefixBuilder.grains: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAIN)

/** Prefixed drams, e.g. `milli.drams`. */
val KPrefixBuilder.drams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.DRAM)

/** Prefixed ounces, e.g. `kilo.ounces`. */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)

/** Prefixed pounds, e.g. `kilo.pounds`. */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** Prefixed stones, e.g. `kilo.stones`. */
val KPrefixBuilder.stones: KMassUnitInstance get() = prefixedMass(this, KMassUnit.STONE)

/** Prefixed US hundredweights, e.g. `kilo.hundredweightsUS`. */
val KPrefixBuilder.hundredweightsUS: KMassUnitInstance get() = prefixedMass(this, KMassUnit.HUNDREDWEIGHT_US)

/** Prefixed UK hundredweights, e.g. `kilo.hundredweightsUK`. */
val KPrefixBuilder.hundredweightsUK: KMassUnitInstance get() = prefixedMass(this, KMassUnit.HUNDREDWEIGHT_UK)

/** Prefixed short tons, e.g. `kilo.shortTons`. */
val KPrefixBuilder.shortTons: KMassUnitInstance get() = prefixedMass(this, KMassUnit.SHORT_TON)

/** Prefixed long tons, e.g. `kilo.longTons`. */
val KPrefixBuilder.longTons: KMassUnitInstance get() = prefixedMass(this, KMassUnit.LONG_TON)

/** Prefixed slugs, e.g. `kilo.slugs`. */
val KPrefixBuilder.slugs: KMassUnitInstance get() = prefixedMass(this, KMassUnit.SLUG)

// --- Troy / apothecary ---------------------------------------------------------------------------

/** Prefixed pennyweights, e.g. `milli.pennyweights`. */
val KPrefixBuilder.pennyweights: KMassUnitInstance get() = prefixedMass(this, KMassUnit.PENNYWEIGHT)

/** Prefixed troy ounces, e.g. `kilo.troyOunces`. */
val KPrefixBuilder.troyOunces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.TROY_OUNCE)

/** Prefixed troy pounds, e.g. `kilo.troyPounds`. */
val KPrefixBuilder.troyPounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.TROY_POUND)

// --- Historical / regional -----------------------------------------------------------------------

/** Prefixed German pounds, e.g. `kilo.germanPounds`. */
val KPrefixBuilder.germanPounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GERMAN_POUND)

/** Prefixed Zentner, e.g. `kilo.zentners`. */
val KPrefixBuilder.zentners: KMassUnitInstance get() = prefixedMass(this, KMassUnit.ZENTNER)

/** Prefixed Lot, e.g. `milli.lots`. */
val KPrefixBuilder.lots: KMassUnitInstance get() = prefixedMass(this, KMassUnit.LOT)

/** Prefixed jin / catty, e.g. `kilo.jin`. */
val KPrefixBuilder.jin: KMassUnitInstance get() = prefixedMass(this, KMassUnit.JIN)

/** Prefixed liang / tael, e.g. `kilo.liang`. */
val KPrefixBuilder.liang: KMassUnitInstance get() = prefixedMass(this, KMassUnit.LIANG)

/** Prefixed momme, e.g. `milli.momme`. */
val KPrefixBuilder.momme: KMassUnitInstance get() = prefixedMass(this, KMassUnit.MOMME)

/** Prefixed kan / kanme, e.g. `kilo.kan`. */
val KPrefixBuilder.kan: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KAN)

// --- Scientific ----------------------------------------------------------------------------------

/** Prefixed daltons, e.g. `kilo.daltons`. */
val KPrefixBuilder.daltons: KMassUnitInstance get() = prefixedMass(this, KMassUnit.DALTON)
