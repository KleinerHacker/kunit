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

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 mass tokens (each = 1 unit, normalized to grams). Vocabulary for building
// (`10 of grams`) and reading (`v into pounds`); combine with the prefix builders (`kilo.grams`).
// Prefixed forms live in KMassUnitExtensions.kt.

// --- Metric / SI ---------------------------------------------------------------------------------

/** 1 gram ([KMassUnit.GRAM]). */
val grams: KMassUnitInstance = massOf(KMassUnit.GRAM.baseValue, KUnitDisplay(KMassUnit.GRAM))

/** 1 tonne / metric ton ([KMassUnit.TONNE]). */
val tonnes: KMassUnitInstance = massOf(KMassUnit.TONNE.baseValue, KUnitDisplay(KMassUnit.TONNE))

/** 1 metric carat ([KMassUnit.CARAT]). */
val carats: KMassUnitInstance = massOf(KMassUnit.CARAT.baseValue, KUnitDisplay(KMassUnit.CARAT))

// --- Avoirdupois ---------------------------------------------------------------------------------

/** 1 grain ([KMassUnit.GRAIN]). */
val grains: KMassUnitInstance = massOf(KMassUnit.GRAIN.baseValue, KUnitDisplay(KMassUnit.GRAIN))

/** 1 dram, avoirdupois ([KMassUnit.DRAM]). */
val drams: KMassUnitInstance = massOf(KMassUnit.DRAM.baseValue, KUnitDisplay(KMassUnit.DRAM))

/** 1 ounce, avoirdupois ([KMassUnit.OUNCE]). */
val ounces: KMassUnitInstance = massOf(KMassUnit.OUNCE.baseValue, KUnitDisplay(KMassUnit.OUNCE))

/** 1 pound, avoirdupois ([KMassUnit.POUND]). */
val pounds: KMassUnitInstance = massOf(KMassUnit.POUND.baseValue, KUnitDisplay(KMassUnit.POUND))

/** 1 stone ([KMassUnit.STONE]). */
val stones: KMassUnitInstance = massOf(KMassUnit.STONE.baseValue, KUnitDisplay(KMassUnit.STONE))

/** 1 hundredweight, US / short ([KMassUnit.HUNDREDWEIGHT_US]). */
val hundredweightsUS: KMassUnitInstance = massOf(KMassUnit.HUNDREDWEIGHT_US.baseValue, KUnitDisplay(KMassUnit.HUNDREDWEIGHT_US))

/** 1 hundredweight, UK / long ([KMassUnit.HUNDREDWEIGHT_UK]). */
val hundredweightsUK: KMassUnitInstance = massOf(KMassUnit.HUNDREDWEIGHT_UK.baseValue, KUnitDisplay(KMassUnit.HUNDREDWEIGHT_UK))

/** 1 short ton, US ([KMassUnit.SHORT_TON]). */
val shortTons: KMassUnitInstance = massOf(KMassUnit.SHORT_TON.baseValue, KUnitDisplay(KMassUnit.SHORT_TON))

/** 1 long ton, UK ([KMassUnit.LONG_TON]). */
val longTons: KMassUnitInstance = massOf(KMassUnit.LONG_TON.baseValue, KUnitDisplay(KMassUnit.LONG_TON))

/** 1 slug ([KMassUnit.SLUG]). */
val slugs: KMassUnitInstance = massOf(KMassUnit.SLUG.baseValue, KUnitDisplay(KMassUnit.SLUG))

// --- Troy / apothecary ---------------------------------------------------------------------------

/** 1 pennyweight ([KMassUnit.PENNYWEIGHT]). */
val pennyweights: KMassUnitInstance = massOf(KMassUnit.PENNYWEIGHT.baseValue, KUnitDisplay(KMassUnit.PENNYWEIGHT))

/** 1 troy ounce ([KMassUnit.TROY_OUNCE]). */
val troyOunces: KMassUnitInstance = massOf(KMassUnit.TROY_OUNCE.baseValue, KUnitDisplay(KMassUnit.TROY_OUNCE))

/** 1 troy pound ([KMassUnit.TROY_POUND]). */
val troyPounds: KMassUnitInstance = massOf(KMassUnit.TROY_POUND.baseValue, KUnitDisplay(KMassUnit.TROY_POUND))

// --- Historical / regional -----------------------------------------------------------------------

/** 1 German pound ([KMassUnit.GERMAN_POUND]). */
val germanPounds: KMassUnitInstance = massOf(KMassUnit.GERMAN_POUND.baseValue, KUnitDisplay(KMassUnit.GERMAN_POUND))

/** 1 Zentner ([KMassUnit.ZENTNER]). */
val zentners: KMassUnitInstance = massOf(KMassUnit.ZENTNER.baseValue, KUnitDisplay(KMassUnit.ZENTNER))

/** 1 Lot ([KMassUnit.LOT]). */
val lots: KMassUnitInstance = massOf(KMassUnit.LOT.baseValue, KUnitDisplay(KMassUnit.LOT))

/** 1 jin / catty ([KMassUnit.JIN]). */
val jin: KMassUnitInstance = massOf(KMassUnit.JIN.baseValue, KUnitDisplay(KMassUnit.JIN))

/** 1 liang / tael ([KMassUnit.LIANG]). */
val liang: KMassUnitInstance = massOf(KMassUnit.LIANG.baseValue, KUnitDisplay(KMassUnit.LIANG))

/** 1 momme ([KMassUnit.MOMME]). */
val momme: KMassUnitInstance = massOf(KMassUnit.MOMME.baseValue, KUnitDisplay(KMassUnit.MOMME))

/** 1 kan / kanme ([KMassUnit.KAN]). */
val kan: KMassUnitInstance = massOf(KMassUnit.KAN.baseValue, KUnitDisplay(KMassUnit.KAN))

// --- Scientific ----------------------------------------------------------------------------------

/** 1 dalton / unified atomic mass unit ([KMassUnit.DALTON]). */
val daltons: KMassUnitInstance = massOf(KMassUnit.DALTON.baseValue, KUnitDisplay(KMassUnit.DALTON))
